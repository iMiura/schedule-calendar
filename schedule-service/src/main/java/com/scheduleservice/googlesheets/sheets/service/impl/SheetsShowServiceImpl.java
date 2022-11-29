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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.slack.api.SlackConfig;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;


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

    @SneakyThrows
    @Override
    public Map sendSlack(String message) throws ServiceException, IOException {



            log.debug("Slackへ送信 開始です");
            try {

                SlackConfig config = new SlackConfig();
                config.setStatsEnabled(false);
                Slack slack = Slack.getInstance(config);

                // 環境変数を読み込みます
                // トークンがボットトークンであれば `xoxb-`、ユーザートークンであれば `xoxp-` で始まっているはずです

                // Googleのアカウントと同じような感じで、トークンのローテーションが必要？
//                https://api.slack.com/authentication/rotation
//                https://qiita.com/seratch/items/610c14208772d49ac9e4
//                https://github.com/slackapi/java-slack-sdk/blob/v1.14.0/bolt-servlet/src/test/java/samples/OAuthSample.java

//              もしかするとマニフェストファイルの設定で、↑の処理要らなくなる可能性もあるけど・・・24時間後とかに分かりそう。

//                時々ボットが削除されてしまう・・・

                // このトークンはハードコーディングから外す
                String token = System.getenv("SLACK_TOKEN");
                token = "xoxb-4065614398534-4402810386176-JPvLn093YmAus3l7qB5mMMcI";

//                log.debug(token);

                ChatPostMessageResponse response = slack.methods(token).chatPostMessage(req -> req
                        .channel("#test") // チャンネル名を指定。
                        .threadTs("1668668610.604149") // スレッドに書き込む場合はスレッドを指定。必須項目ではない。
                        .text(message));
                if (response.isOk()) {
                    Message postedMessage = response.getMessage();
                    log.debug(String.valueOf(postedMessage));
                } else {
                    String errorCode = response.getError(); // 例: "invalid_auth", "channel_not_found"
                    log.debug(errorCode);
                }

                log.debug(response.getTs() + "←新規スレッド作成直後のthreadTsを、次回以降指定することでスレッドの特定ができる。");

            } catch (SlackApiException requestFailure) {
                // Slack API が 20x 以外の HTTP ステータスで応答した
                requestFailure.printStackTrace();
            } catch (IOException connectivityIssue) {
                // 何らかの接続の問題が発生した
                connectivityIssue.printStackTrace();
            } finally {
                log.debug("Slackへ送信 完了です");

        }
        return new HashMap();

    }


}

