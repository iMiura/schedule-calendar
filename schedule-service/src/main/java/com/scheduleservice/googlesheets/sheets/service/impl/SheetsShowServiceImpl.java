package com.scheduleservice.googlesheets.sheets.service.impl;

import com.scheduleservice.googlesheets.config.CustomMessageResource;
import com.scheduleservice.googlesheets.constant.CommonConstant;
import com.scheduleservice.googlesheets.exception.ServiceException;
import com.scheduleservice.googlesheets.repository.entity.CalendarDeployedManagementEntity;
import com.scheduleservice.googlesheets.repository.entity.FilterViewInfoEntity;
import com.scheduleservice.googlesheets.repository.entity.GoogleSheetsInfoEntity;
import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.scheduleservice.googlesheets.repository.entity.WorkYmInfoEntity;
import com.scheduleservice.googlesheets.repository.service.ICalendarDeployedManagementService;
import com.scheduleservice.googlesheets.repository.service.IFilterViewInfoService;
import com.scheduleservice.googlesheets.repository.service.IGoogleSheetsInfoService;
import com.scheduleservice.googlesheets.repository.service.ISystemPropertyService;
import com.scheduleservice.googlesheets.repository.service.IUserInfoService;
import com.scheduleservice.googlesheets.repository.service.IWorkYmInfoService;
import com.scheduleservice.googlesheets.security.session.SessionUtil;
import com.scheduleservice.googlesheets.sheets.service.SheetsShowService;
import com.scheduleservice.googlesheets.util.DateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

/**
 * ユーザー情報 関連ビジネスロジックオブジェクト.
 */
@Service
@Slf4j
public class SheetsShowServiceImpl implements SheetsShowService {

    @Autowired
    private ICalendarDeployedManagementService iCalendarDeployedManagementService;
    @Autowired
    private IGoogleSheetsInfoService iGoogleSheetsInfoService;
    @Autowired
    private ISystemPropertyService iSystemPropertyService;
    @Autowired
    private IWorkYmInfoService iWorkYmInfoService;
    @Autowired
    private IUserInfoService iUserInfoService;
    @Autowired
    private IFilterViewInfoService iFilterViewInfoService;
    @Autowired
    private CustomMessageResource messageSource;

    @Override
    public Map getGoogleSheetsInfo(String calendarYm, String urlFlg, String userId) throws ServiceException {
        // カレンダー年月度
        if (!StringUtils.hasLength(calendarYm)) {
            LocalDate date = LocalDate.now();
            WorkYmInfoEntity workYmInfoEntity = iWorkYmInfoService.findWorkYmInfo(DateTimeFormatter.ofPattern("yyyyMMdd").format(date));
            if (workYmInfoEntity != null) {
                calendarYm = workYmInfoEntity.getCalendarYm();
            } else {
                calendarYm = DateTimeFormatter.ofPattern("yyyyMM").format(date);
            }
        }

        // チームID
        Long teamId = SessionUtil.getUserInfo().getTeamId();

        // 権限 【オーナー:0 / 編集:1 / 閲覧:2】
        int permission = SessionUtil.getUserInfo().getUserPermission();

        // カレンダー展開最終年月
        String deployedYm = null;
        CalendarDeployedManagementEntity calendarDeployedManagement = iCalendarDeployedManagementService.getById(teamId);
        if (calendarDeployedManagement != null) {
            deployedYm = calendarDeployedManagement.getCalendarDeployedYm();
        }
        // システム運用開始年月度
        String startYm = iSystemPropertyService.getProperty(teamId, CommonConstant.START_YM_KEY);
        // カレンダー展開
        if ("2".equals(urlFlg)) {
            if (!StringUtils.hasLength(deployedYm)) {
                calendarYm = startYm;
            } else {
                LocalDate deployedDate = LocalDate.parse(deployedYm + "01", DateTimeFormatter.ofPattern("yyyyMMdd"));
                deployedDate = deployedDate.plusMonths(1);
                calendarYm = deployedDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
            }
        }

        Map result = new HashMap();
        result.put("calendarYm", calendarYm);
        result.put("permission", permission);
        result.put("deployedYm", deployedYm);
        result.put("startYm", startYm);

        // 最終更新日時取得
        String finalChangeDate = null;
        if (calendarDeployedManagement != null) {
            finalChangeDate = DateUtil.getMilli(calendarDeployedManagement.getFinalChangerDate());
        }
        result.put("finalChangeDate", finalChangeDate);

        // スプレッドシートファイル保存先URL
        String listUrl = null;

        String message = null;

        if (StringUtils.hasLength(deployedYm) && calendarYm.compareTo(deployedYm) <= 0) {
            // スプレッドシート情報取得
            GoogleSheetsInfoEntity googleSheetsInfoEntity = iGoogleSheetsInfoService.getGoogleSheetsInfo(teamId, calendarYm);
            if (googleSheetsInfoEntity != null) {
                // タスクリスト
                if ("0".equals(urlFlg)) {
                    listUrl =
                        googleSheetsInfoEntity.getGoogleSheetsFileUrl() + googleSheetsInfoEntity
                            .getTaskListFileId() + CommonConstant.GID + googleSheetsInfoEntity
                            .getTaskListSheetId();
                    log.debug("Google Sheets の情報（ファイルID：" + googleSheetsInfoEntity.getTaskListFileId()
                        + "、シート名：" + googleSheetsInfoEntity.getTaskListSheetName() + "）");
                    //業務フロー
                } else if ("1".equals(urlFlg)) {
                    listUrl =
                        googleSheetsInfoEntity.getGoogleSheetsFileUrl() + googleSheetsInfoEntity
                            .getWorkListFileId() + CommonConstant.GID + googleSheetsInfoEntity
                            .getWorkListSheetId();
                    log.debug("Google Sheets の情報（ファイルID：" + googleSheetsInfoEntity.getWorkListFileId()
                        + "、シート名：" + googleSheetsInfoEntity.getWorkListSheetName() + "）");
                    // カレンダー展開
                } else if ("2".equals(urlFlg)) {
                    listUrl =
                        googleSheetsInfoEntity.getGoogleSheetsFileUrl() + googleSheetsInfoEntity
                            .getCalendarListFileId() + CommonConstant.GID + googleSheetsInfoEntity
                            .getCalendarListSheetId();
                    log.debug("Google Sheets の情報（ファイルID：" + googleSheetsInfoEntity.getCalendarListFileId()
                        + "、シート名：" + googleSheetsInfoEntity.getCalendarListSheetName() + "）");
                }
            } else {
                // スプレッドシートが展開されていません
                message = messageSource.getMessage("errors.google_sheets_nodeploye");
                // カレンダー展開
                if ("2".equals(urlFlg)) {
                    // スプレッドシートデータを用意されていません
                    message = messageSource.getMessage("errors.google_sheets_nofile");
                }
            }
        } else {
            // スプレッドシートが展開されていません
            message = messageSource.getMessage("errors.google_sheets_nodeploye");
            // カレンダー展開
            if ("2".equals(urlFlg)) {
                // スプレッドシート情報取得
                GoogleSheetsInfoEntity googleSheetsInfoEntity = iGoogleSheetsInfoService.getGoogleSheetsInfo(teamId, calendarYm);
                if (googleSheetsInfoEntity != null) {
                    listUrl =
                        googleSheetsInfoEntity.getGoogleSheetsFileUrl() + googleSheetsInfoEntity
                            .getCalendarListFileId() + CommonConstant.GID + googleSheetsInfoEntity
                            .getCalendarListSheetId();
                    log.debug("Google Sheets の情報（ファイルID：" + googleSheetsInfoEntity.getCalendarListFileId()
                        + "、シート名：" + googleSheetsInfoEntity.getCalendarListSheetName() + "）");
                    message = null;
                } else {
                    // スプレッドシートデータを用意されていません
                    message = messageSource.getMessage("errors.google_sheets_nofile");
                }
            }
        }

        // フィルターの場合
        if (StringUtils.hasLength(userId)) {
            FilterViewInfoEntity filterViewInfoEntity = iFilterViewInfoService.getFilterViewInfo(teamId, Long.parseLong(userId), calendarYm);
            if (filterViewInfoEntity != null) {
                listUrl = listUrl + "&fvid=" + filterViewInfoEntity.getFilterViewId();
                log.debug("Google Sheets の情報（フィルターID：" + filterViewInfoEntity.getFilterViewId() + "）");
            }
        }

        result.put("listUrl", listUrl);
        result.put("message", message);

        return result;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public boolean updateCalendarDeployed(String deployedYm, String finalChangeDate) throws ServiceException {

        // チームID
        Long teamId = SessionUtil.getUserInfo().getTeamId();
        CalendarDeployedManagementEntity calendarDeployedManagement = new CalendarDeployedManagementEntity();
        calendarDeployedManagement.setTeamId(teamId);
        calendarDeployedManagement.setCalendarDeployedYm(deployedYm);
        calendarDeployedManagement.setFinalChangerDate(LocalDateTime.now());

        LocalDateTime localDateTime = DateUtil.parseUnixMilli(Long.parseLong(finalChangeDate));

        return iCalendarDeployedManagementService.updateCalendarDeployed(calendarDeployedManagement, localDateTime);
    }

    @Override
    public Map getUserList() throws ServiceException {
        Map result = new HashMap();
        List<UserInfoEntity> list = iUserInfoService.getUserList();
        List<Map> userList = new ArrayList<>();
        for (UserInfoEntity userInfoEntity : list) {
            Map user = new HashMap();
            user.put("label", userInfoEntity.getUserName());
            user.put("value", userInfoEntity.getUserId());
            userList.add(user);
        }
        result.put("userList", userList);

        return result;
    }

    @Override
    public Map sendSlack(String message) throws ServiceException, IOException {

        log.debug("Slackへ送信 開始です");
        Slack slack = Slack.getInstance();
        String webhookUrl = "https://hooks.slack.com/services/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";

        Payload payload = Payload.builder().text(message).build();

        WebhookResponse response = slack.send(webhookUrl, payload);

        log.debug("Slackへ送信 完了です");
        log.debug(String.valueOf(response));

        return new HashMap();


    }

}

