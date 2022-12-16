package com.scheduleservice.googlesheets.release.service.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.script.Script;
import com.google.api.services.script.ScriptScopes;
import com.google.api.services.script.model.ExecutionRequest;
import com.google.api.services.script.model.Operation;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.scheduleservice.googlesheets.config.ConstantPropertiesConfig;
import com.scheduleservice.googlesheets.config.CustomMessageResource;
import com.scheduleservice.googlesheets.constant.CommonConstant;
import com.scheduleservice.googlesheets.exception.ServiceException;
import com.scheduleservice.googlesheets.login.controller.LoginController;
import com.scheduleservice.googlesheets.release.entity.ReleaseInfo;
import com.scheduleservice.googlesheets.release.service.ReleaseService;
import com.scheduleservice.googlesheets.repository.entity.CarModelGroupMasterEntity;
import com.scheduleservice.googlesheets.repository.entity.CarModelMasterEntity;
import com.scheduleservice.googlesheets.repository.entity.GoogleSheetsInfoMstEntity;
import com.scheduleservice.googlesheets.repository.entity.MakerMasterEntity;
import com.scheduleservice.googlesheets.repository.entity.MasterScheduleEntity;
import com.scheduleservice.googlesheets.repository.entity.ReleaseInfoEntity;
import com.scheduleservice.googlesheets.repository.entity.RowInfoEntity;
import com.scheduleservice.googlesheets.repository.entity.SlackDocumentMstEntity;
import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.scheduleservice.googlesheets.repository.entity.WritingSetInfoEntity;
import com.scheduleservice.googlesheets.repository.service.ICarModelGroupMasterService;
import com.scheduleservice.googlesheets.repository.service.ICarModelMasterService;
import com.scheduleservice.googlesheets.repository.service.IGoogleSheetsInfoMstService;
import com.scheduleservice.googlesheets.repository.service.IMakerMasterService;
import com.scheduleservice.googlesheets.repository.service.IMasterScheduleService;
import com.scheduleservice.googlesheets.repository.service.IReleaseInfoService;
import com.scheduleservice.googlesheets.repository.service.IRowInfoService;
import com.scheduleservice.googlesheets.repository.service.ISlackDocumentMstService;
import com.scheduleservice.googlesheets.repository.service.ISystemPropertyService;
import com.scheduleservice.googlesheets.repository.service.IUserInfoService;
import com.scheduleservice.googlesheets.repository.service.IWritingSetInfoService;
import com.scheduleservice.googlesheets.security.session.SessionUtil;
import com.scheduleservice.googlesheets.util.DateUtil;
import com.scheduleservice.googlesheets.util.UpdateValues;
import com.scheduleservice.googlesheets.util.VariableNameConversion;
import com.slack.api.Slack;
import com.slack.api.SlackConfig;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.Message;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * リリース情報 関連ビジネスロジックオブジェクト.
 */
@Service
@Slf4j
public class ReleaseServiceImpl implements ReleaseService {

    @Autowired
    private IMakerMasterService iMakerMasterService;
    @Autowired
    private ICarModelMasterService icarModelMasterService;
    @Autowired
    private ICarModelGroupMasterService icarModelGroupMasterService;
    @Autowired
    private IMasterScheduleService iMasterScheduleService;
    @Autowired
    private IUserInfoService iUserInfoService;
    @Autowired
    private IGoogleSheetsInfoMstService iGoogleSheetsInfoMstService;
    @Autowired
    private IReleaseInfoService iReleaseInfoService;
    @Autowired
    private IRowInfoService iRowInfoService;
    @Autowired
    private IWritingSetInfoService iWritingSetInfoService;
    @Autowired
    private ISlackDocumentMstService iSlackDocumentMstService;
    @Autowired
    private CustomMessageResource messageSource;
    @Autowired
    private ISystemPropertyService iSystemPropertyService;
    @Autowired
    private ConstantPropertiesConfig constant;

    private GoogleAuthorizationCodeFlow flow;
    /** Sheet service. */
    private Sheets sheetsService;
    /** Script service. */
    Script service;

    @Override
    public Map initReleaseSearch() throws ServiceException {

        Map resultMap = new HashMap();

        // メーカーマスタ情報取得
        List<MakerMasterEntity> makerMasterEntityList = iMakerMasterService.list();
        List<Map> makerList = new ArrayList<>();
        for (MakerMasterEntity makerMasterEntity : makerMasterEntityList) {
            Map makerMap = new HashMap();
            makerMap.put("label", makerMasterEntity.getMakerName());
            makerMap.put("value", makerMasterEntity.getMakerCd());
            makerList.add(makerMap);
        }
        resultMap.put("makerList", makerList);
        resultMap.put("carModelList", new ArrayList());
        resultMap.put("carModelGroupList", new ArrayList());

        // 対応時期リスト取得
        List<LocalDate> scheduleList = iMasterScheduleService.selectScheduleList();
        String supportPeriodStart = null;
        String supportPeriodEnd = null;
        for (LocalDate schedule : scheduleList) {
            String supportPeriod = DateTimeFormatter.ofPattern("yyyy年MM月").format(schedule);
            if (StringUtils.hasLength(supportPeriodStart) && !StringUtils.hasLength(supportPeriodEnd)) {
                supportPeriodEnd = supportPeriod;
            }
            if (!StringUtils.hasLength(supportPeriodStart)) {
                supportPeriodStart = supportPeriod;
            }
        }

        // スケジュール取得
        List<Map> masterScheduleList = new ArrayList<>();
        MasterScheduleEntity masterScheduleStart = iMasterScheduleService.getById(supportPeriodStart);
        MasterScheduleEntity masterScheduleEnd = iMasterScheduleService.getById(supportPeriodEnd);
        // 対応時期
        Map scheduleMap = new HashMap();
        scheduleMap.put("title", "対応時期");
        scheduleMap.put("dateStart", masterScheduleStart.getSupportPeriod());
        scheduleMap.put("dateEnd", masterScheduleEnd.getSupportPeriod());
        masterScheduleList.add(scheduleMap);
        // マスター締め日
        scheduleMap = new HashMap();
        scheduleMap.put("title", "マスター締め日");
        scheduleMap.put("dateStart", DateTimeFormatter.ofPattern("MM月dd日").format(masterScheduleStart.getMasterDeadline()));
        scheduleMap.put("dateEnd", DateTimeFormatter.ofPattern("MM月dd日").format(masterScheduleEnd.getMasterDeadline()));
        masterScheduleList.add(scheduleMap);
        // 提出日
        scheduleMap = new HashMap();
        scheduleMap.put("title", "提出日");
        scheduleMap.put("dateStart", DateTimeFormatter.ofPattern("MM月dd日").format(masterScheduleStart.getSubmitDate()));
        scheduleMap.put("dateEnd", DateTimeFormatter.ofPattern("MM月dd日").format(masterScheduleEnd.getSubmitDate()));
        masterScheduleList.add(scheduleMap);
        // 更新日
        scheduleMap = new HashMap();
        scheduleMap.put("title", "更新日");
        scheduleMap.put("dateStart", DateTimeFormatter.ofPattern("MM月dd日").format(masterScheduleStart.getUpdateDate()));
        scheduleMap.put("dateEnd", DateTimeFormatter.ofPattern("MM月dd日").format(masterScheduleEnd.getUpdateDate()));
        masterScheduleList.add(scheduleMap);
        resultMap.put("masterScheduleList", masterScheduleList);

        // 新車入力担当者情報取得
        List<UserInfoEntity> list = iUserInfoService.getPicList();
        List<Map> userList = new ArrayList<>();
        for (UserInfoEntity userInfoEntity : list) {
            Map userMap = new HashMap();
            userMap.put("label", userInfoEntity.getUserName());
            userMap.put("value", userInfoEntity.getUserId());
            userList.add(userMap);
        }
        resultMap.put("userList", userList);

        return resultMap;
    }

    @Override
    public Map getCarModelList(Long makerCd) throws ServiceException {
        Map result = new HashMap();
        // 車種情報取得
        List<CarModelMasterEntity> list = icarModelMasterService.getCarModelList(makerCd);
        List<Map> carModelList = new ArrayList<>();
        for (CarModelMasterEntity carModelMasterEntity : list) {
            Map carModelMap = new HashMap();
            carModelMap.put("label", carModelMasterEntity.getCarModelName());
            carModelMap.put("value", carModelMasterEntity.getCarModelCd());
            carModelList.add(carModelMap);
        }
        result.put("carModelList", carModelList);

        return result;
    }

    @Override
    public Map getCarModelGroupList(Long makerCd, Long carModelCd) throws ServiceException {
        Map result = new HashMap();
        // 車種系統情報取得
        List<CarModelGroupMasterEntity> list = icarModelGroupMasterService.getCarModelGroupList(makerCd, carModelCd);
        List<Map> carModelGroupList = new ArrayList<>();
        for (CarModelGroupMasterEntity carModelGroupMasterEntity : list) {
            Map carModelGroupMap = new HashMap();
            carModelGroupMap.put("label", carModelGroupMasterEntity.getCarModelGroupName());
            carModelGroupMap.put("value", carModelGroupMasterEntity.getCarModelGroupCd());
            carModelGroupList.add(carModelGroupMap);
        }
        result.put("carModelGroupList", carModelGroupList);

        return result;
    }

    @Override
    public boolean checkReleaseFile() {
        // スプレッドシート情報取得
        GoogleSheetsInfoMstEntity googleSheetsInfoMstEntity = iGoogleSheetsInfoMstService.getGoogleSheetsInfo(CommonConstant.GOOGLE_SHEETS_DIV_1, CommonConstant.GOOGLE_SHEETS_API_DIV_3);
        return googleSheetsInfoMstEntity == null;
    }

    @Override
    public Map getReleaseFile(ReleaseInfo releaseInfo, String checked) {
        // スプレッドシートファイル保存先URL
        String listUrl = "";
        String message = null;
        // スプレッドAPI情報取得
        GoogleSheetsInfoMstEntity googleSheetsInfoMstEntity = iGoogleSheetsInfoMstService.getGoogleSheetsInfo(CommonConstant.GOOGLE_SHEETS_DIV_1, CommonConstant.GOOGLE_SHEETS_API_DIV_2);
        if (googleSheetsInfoMstEntity != null) {
            // GASデプロイ時に発行されたID
            String deployId = googleSheetsInfoMstEntity.getGoogleSheetsFileUrl();

            String searchParam = "";
            String fvid = getFvidByAppScript(releaseInfo, checked, deployId);
            if (StringUtils.hasLength(fvid)) {
                searchParam = "fvid=" + fvid;
            }

            // スプレッドシート情報取得
            googleSheetsInfoMstEntity = iGoogleSheetsInfoMstService.getGoogleSheetsInfo(CommonConstant.GOOGLE_SHEETS_DIV_1, CommonConstant.GOOGLE_SHEETS_API_DIV_1);
            if (googleSheetsInfoMstEntity == null) {
                // スプレッドシートデータを用意されていません
                message = messageSource.getMessage("errors.google_sheets_nofile");
            } else {
                listUrl =
                    googleSheetsInfoMstEntity.getGoogleSheetsFileUrl() + googleSheetsInfoMstEntity
                        .getGoogleSheetsFileId() + CommonConstant.GID + googleSheetsInfoMstEntity
                        .getGoogleSheetsSheetId();
                if (StringUtils.hasLength(searchParam)) {
                    listUrl = listUrl + "?" + searchParam;
                }
                log.debug("Google Sheets の情報（ファイルID：" + googleSheetsInfoMstEntity.getGoogleSheetsFileId()
                    + "、シート名：" + googleSheetsInfoMstEntity.getGoogleSheetsSheetName() + "）");
            }
        } else {
            // スプレッドシートデータを用意されていません
            message = messageSource.getMessage("errors.google_sheets_nofile");
        }
        Map result = new HashMap();
        result.put("listUrl", listUrl);
        result.put("message", message);

        return result;
    }

    @Override
    public Map getReleaseInfo(String releaseInfoId) throws ServiceException {

        Map resultMap = new HashMap();

        // メーカーマスタ情報取得
        List<MakerMasterEntity> makerMasterEntityList = iMakerMasterService.list();
        List<Map> makerList = new ArrayList<>();
        for (MakerMasterEntity makerMasterEntity : makerMasterEntityList) {
            Map makerMap = new HashMap();
            makerMap.put("label", makerMasterEntity.getMakerName());
            makerMap.put("value", makerMasterEntity.getMakerCd());
            makerList.add(makerMap);
        }
        resultMap.put("makerList", makerList);
        resultMap.put("carModelList", new ArrayList());
        resultMap.put("carModelGroupList", new ArrayList());

        // 対応時期リスト取得
        List<LocalDate> scheduleList = iMasterScheduleService.selectScheduleList();
        List<Map> supportPeriodList = new ArrayList<>();
        for (LocalDate schedule : scheduleList) {
            String supportPeriodLabel = DateTimeFormatter.ofPattern("yyyy年MM月").format(schedule);
            String supportPeriodValue = DateTimeFormatter.ofPattern("yyyy/MM").format(schedule);
            Map supportPeriodMap = new HashMap();
            supportPeriodMap.put("label", supportPeriodLabel);
            supportPeriodMap.put("value", supportPeriodValue);
            supportPeriodList.add(supportPeriodMap);
        }
        resultMap.put("supportPeriodList", supportPeriodList);

        // 新車入力担当者情報取得
        List<UserInfoEntity> list = iUserInfoService.getPicList();
        List<Map> userList = new ArrayList<>();
        for (UserInfoEntity userInfoEntity : list) {
            Map userMap = new HashMap();
            userMap.put("label", userInfoEntity.getUserName());
            userMap.put("value", userInfoEntity.getUserId());
            userList.add(userMap);
        }
        resultMap.put("userList", userList);

        // リリース情報取得
        ReleaseInfoEntity releaseInfoEntity = iReleaseInfoService.getById(releaseInfoId);
        // 最終更新日時取得
        String finalChangeDate = "";
        if (releaseInfoEntity == null) {
            releaseInfoEntity = new ReleaseInfoEntity();
        } else {
            finalChangeDate = DateUtil.getMilli(releaseInfoEntity.getUpdateDate());
        }
        resultMap.put("releaseInfo", releaseInfoEntity);
        resultMap.put("finalChangeDate", finalChangeDate);

        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public boolean saveReleaseInfo(ReleaseInfo releaseInfo, String finalChangeDate) throws ServiceException {

        // システム日時
        LocalDateTime localDateTime = LocalDateTime.now();

        // 登録編集削除区分
        String processDiv = "";
        // 行数情報取得
        long rowNo = 1;
        // リリース情報取得
        ReleaseInfoEntity releaseInfoEntity = iReleaseInfoService.getById(releaseInfo.getReleaseInfoId());
        // 新規
        if (releaseInfoEntity == null) {
            releaseInfoEntity = new ReleaseInfoEntity();
            // 登録編集削除区分
            processDiv = "0";
            // 行数
            releaseInfoEntity.setRowNumber(rowNo);
            // 登録者ID
            releaseInfoEntity.setReleaseCerateUserId(SessionUtil.getUserInfo().getUserId());
            // 登録日時
            releaseInfoEntity.setCerateDate(localDateTime);
            // 削除フラグ [有効:0 / 削除:1]
            releaseInfoEntity.setDeleteFlg(0);
            RowInfoEntity rowInfoEntity = iRowInfoService.getById(CommonConstant.GOOGLE_SHEETS_DIV_1);
            if (rowInfoEntity != null) {
                rowNo = rowInfoEntity.getRowNumber() + 1;
                rowInfoEntity.setRowNumber(rowNo);
                // 行数情報更新
                iRowInfoService.updateRowInfo(rowInfoEntity);
            }
        } else {
            // 登録編集削除区分
            processDiv = "1";
            String finalChangeDateNew = DateUtil.getMilli(releaseInfoEntity.getUpdateDate());
            if (!StringUtils.hasLength(finalChangeDate)) {
                return false;
            } else if (finalChangeDateNew.compareTo(finalChangeDate) > 0) {
                return false;
            }
            rowNo = releaseInfoEntity.getRowNumber();
        }
        // データ設定
        copyProperties(releaseInfoEntity, releaseInfo);

        // 更新者ID
        releaseInfoEntity.setReleaseUpdateUserId(SessionUtil.getUserInfo().getUserId());
        // 更新日時
        releaseInfoEntity.setUpdateDate(localDateTime);
        boolean result;
        if (StringUtils.hasLength(finalChangeDate)) {
            LocalDateTime updateDate = DateUtil.parseUnixMilli(Long.parseLong(finalChangeDate));
            // リリース情報更新
            result = iReleaseInfoService.updateReleaseInfo(releaseInfoEntity, updateDate);
        } else {
            // リリース情報登録
            result = iReleaseInfoService.saveReleaseInfo(releaseInfoEntity);
        }

        if (!result) {
            return false;
        }

        // 書込定義情報取得
        List<WritingSetInfoEntity> writingSetInfoList =  iWritingSetInfoService.getWritingSetInfo(CommonConstant.GOOGLE_SHEETS_DIV_1, processDiv);

        // リリースNo.
        releaseInfo.setReleaseInfoId(String.valueOf(releaseInfoEntity.getReleaseInfoId()));
        // ログインユーザー名
        releaseInfo.setLoginUserName(SessionUtil.getUserInfo().getUserName());
        // 更新日
        releaseInfo.setUpdateDate(DateUtil.format(localDateTime, "MM/dd"));
        // トリガー起動指示フラグ
        releaseInfo.setTriggerFlg("1");
        List<String> ranges = new ArrayList();
        List<String> values = new ArrayList();
        try {
            // Google Sheetsファイルに書き込み値
            for (WritingSetInfoEntity writingSetInfoEntity : writingSetInfoList) {
                String column = writingSetInfoEntity.getColumnName();
                String targetColumn = writingSetInfoEntity.getTargetColumn();
                ranges.add(targetColumn + rowNo);
                String[] columns = column.split(",");
                String fieldValue = "";
                for (String item : columns) {
                    Field field = releaseInfo.getClass().getDeclaredField(
                        VariableNameConversion.lowerLineToHump(item));
                    field.setAccessible(true);
                    if (field.get(releaseInfo) != null) {
                        fieldValue += (String) field.get(releaseInfo);
                    }
                }
                values.add(fieldValue);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ServiceException(e.getMessage());
        }

        // Google Sheetsの所定セルに値を投入
        updateSheet(ranges, values);

        // Slack通知
        if (!StringUtils.hasLength(releaseInfo.getSalesCategoryL())) {
            if (!StringUtils.hasLength(finalChangeDate)
                || !releaseInfoEntity.getReleaseCategory().equals(releaseInfo.getReleaseCategoryId())) {
                sendSlack(releaseInfo);
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public boolean delReleaseInfo(String releaseInfoId, String finalChangeDate) throws ServiceException {

        // リリース情報取得
        ReleaseInfoEntity releaseInfoEntity = iReleaseInfoService.getById(releaseInfoId);
        if (releaseInfoEntity != null) {
            String finalChangeDateNew = DateUtil.getMilli(releaseInfoEntity.getUpdateDate());
            if (!StringUtils.hasLength(finalChangeDate)) {
                return false;
            } else if (finalChangeDateNew.compareTo(finalChangeDate) > 0) {
                return false;
            }
            LocalDateTime localDateTime = DateUtil.parseUnixMilli(Long.parseLong(finalChangeDate));
            // 最終更新日時
            releaseInfoEntity.setUpdateDate(LocalDateTime.now());
            // 削除フラグ [有効:0 / 削除:1]
            releaseInfoEntity.setDeleteFlg(1);
            boolean result = iReleaseInfoService.updateReleaseInfo(releaseInfoEntity, localDateTime);
            if (!result) {
                return false;
            }

            // 書込定義情報取得
            List<WritingSetInfoEntity> writingSetInfoList =  iWritingSetInfoService.getWritingSetInfo(CommonConstant.GOOGLE_SHEETS_DIV_1, "2");

            ReleaseInfo releaseInfo = new ReleaseInfo();
            // 削除フラグ [有効:0 / 削除:1]
            releaseInfo.setDeleteFlg("1");
            // トリガー起動指示フラグ
            releaseInfo.setTriggerFlg("1");
            List<String> ranges = new ArrayList();
            List<String> values = new ArrayList();
            try {
                // Google Sheetsファイルに書き込み値
                for (WritingSetInfoEntity writingSetInfoEntity : writingSetInfoList) {
                    String column = writingSetInfoEntity.getColumnName();
                    String targetColumn = writingSetInfoEntity.getTargetColumn();
                    ranges.add(targetColumn + releaseInfoEntity.getRowNumber());
                    String[] columns = column.split(",");
                    String fieldValue = "";
                    for (String item : columns) {
                        Field field = releaseInfo.getClass().getDeclaredField(
                            VariableNameConversion.lowerLineToHump(item));
                        field.setAccessible(true);
                        if (field.get(releaseInfo) != null) {
                            fieldValue += (String) field.get(releaseInfo);
                        }
                    }
                    values.add(fieldValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new ServiceException(e.getMessage());
            }
            // Google Sheetsの所定セルに値を投入
            updateSheet(ranges, values);
        }
        return false;
    }

    /**
     * データ設定
     *
     * @param releaseInfoEntity リリース情報
     * @param releaseInfo 画面からリリース情報
     * @throws IOException
     */
    private void copyProperties(ReleaseInfoEntity releaseInfoEntity, ReleaseInfo releaseInfo) {
        // リリース区分
        if (StringUtils.hasLength(releaseInfo.getReleaseCategoryId())) {
            releaseInfoEntity.setReleaseCategory(Long.parseLong(releaseInfo.getReleaseCategoryId()));
        } else {
            releaseInfoEntity.setReleaseCategory(null);
        }
        // 次回確認予定
        if (StringUtils.hasLength(releaseInfo.getNextConfirmDate())) {
            LocalDateTime nextConfirmDate = DateUtil.parseUnixMilli(Long.parseLong(releaseInfo.getNextConfirmDate()) * 1000);
            releaseInfo.setNextConfirmDate(DateUtil.format(nextConfirmDate, CommonConstant.STRING_FORMAT_YMD));
            releaseInfoEntity.setNextConfirmDate(DateUtil.format(nextConfirmDate, CommonConstant.STRING_FORMAT_YMD_2));
        } else {
            releaseInfoEntity.setNextConfirmDate(null);
        }
        // 発表日
        if (StringUtils.hasLength(releaseInfo.getAnnouncementDate())) {
            LocalDateTime announcementDate = DateUtil.parseUnixMilli(Long.parseLong(releaseInfo.getAnnouncementDate()) * 1000);
            releaseInfo.setAnnouncementDate(DateUtil.format(announcementDate, CommonConstant.STRING_FORMAT_YMD));
            releaseInfoEntity.setAnnouncementDate(DateUtil.format(announcementDate, CommonConstant.STRING_FORMAT_YMD_2));
        }
        // 発売日
        if (StringUtils.hasLength(releaseInfo.getLaunchDate())) {
            LocalDateTime launchDate = DateUtil.parseUnixMilli(Long.parseLong(releaseInfo.getLaunchDate()) * 1000);
            releaseInfo.setLaunchDate(DateUtil.format(launchDate, CommonConstant.STRING_FORMAT_YMD));
            releaseInfoEntity.setLaunchDate(DateUtil.format(launchDate, CommonConstant.STRING_FORMAT_YMD_2));
        }
        // リリースURL
        releaseInfoEntity.setReleaseUrl(releaseInfo.getReleaseUrl());
        // ーカーCD
        releaseInfoEntity.setMakerCd(Long.parseLong(releaseInfo.getMakerCd()));
        // 車種CD
        if (StringUtils.hasLength(releaseInfo.getCarModelCd())) {
            releaseInfoEntity.setCarModelCd(Long.parseLong(releaseInfo.getCarModelCd()));
        }
        // 車種系統CD
        if (StringUtils.hasLength(releaseInfo.getCarModelGroupCd())) {
            releaseInfoEntity.setCarModelGroupCd(Long.parseLong(releaseInfo.getCarModelGroupCd()));
        }
        // 仮車種名
        releaseInfoEntity.setTempCarName(releaseInfo.getTempCarName());
        // 仮車種系統名
        releaseInfoEntity.setTempCarGroupName(releaseInfo.getTempCarGroupName());
        // 販売区分（左側）
        if (StringUtils.hasLength(releaseInfo.getSalesCategoryIdL())) {
            releaseInfoEntity.setSalesCategoryL(Long.parseLong(releaseInfo.getSalesCategoryIdL()));
        } else {
            releaseInfoEntity.setSalesCategoryL(null);
        }
        // 販売区分（右側）
        if (StringUtils.hasLength(releaseInfo.getSalesCategoryIdR())) {
            releaseInfoEntity.setSalesCategoryR(Long.parseLong(releaseInfo.getSalesCategoryIdR()));
        } else {
            releaseInfoEntity.setSalesCategoryR(null);
        }
        // 販売区分の備考
        releaseInfoEntity.setSalesCategoryNote(releaseInfo.getSalesCategoryNote());
        // 対応時期
        if (StringUtils.hasLength(releaseInfo.getSupportPeriod())) {
            releaseInfoEntity.setSupportPeriod(releaseInfo.getSupportPeriod().replaceAll("/", ""));
        } else {
            releaseInfoEntity.setSupportPeriod(null);
        }
        // 新車入力担当者ID
        if (StringUtils.hasLength(releaseInfo.getPicId())) {
            releaseInfoEntity.setPicId(Long.parseLong(releaseInfo.getPicId()));
        } else {
            releaseInfoEntity.setPicId(null);
        }
        // 備考
        releaseInfoEntity.setNote(releaseInfo.getNote());
    }

    /**
     * Google Sheetsの所定セルに値を投入
     *
     * @param ranges セル指定（例：A1:B1）
     * @param values 投入値（リスト）
     * @throws IOException
     */
    private void updateSheet(List<String> ranges, List values) throws ServiceException {
        // スプレッドシート情報取得
        GoogleSheetsInfoMstEntity googleSheetsInfoMstEntity = iGoogleSheetsInfoMstService.getGoogleSheetsInfo(CommonConstant.GOOGLE_SHEETS_DIV_1, CommonConstant.GOOGLE_SHEETS_API_DIV_3);

        // ファイルID
        String spreadsheetId = googleSheetsInfoMstEntity.getGoogleSheetsFileId();
        // シート名
        String sheetName = googleSheetsInfoMstEntity.getGoogleSheetsSheetName();

        List rangeList = new ArrayList();
        for (String ran : ranges) {
            rangeList.add(sheetName + "!" + ran + ":" + ran);
        }

        try {
            if (sheetsService == null) {
                getService();
            }
            UpdateValues
                .updateValues(sheetsService, spreadsheetId, rangeList, "USER_ENTERED", values);
            log.debug("Google Sheets の情報（ファイルID：" + spreadsheetId + "、シート名：" + sheetName
                + "、設定行：" + ranges + "、設定値：" + values + "）");
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void getService() throws ServiceException {
        JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, ScriptScopes.SCRIPT_PROJECTS);
        String CREDENTIALS_FILE_PATH = constant.getCredentialsFilePath();
        String TOKENS_FILE_PATH = constant.getTokensFilePath();
        String userId = SessionUtil.getUserInfo().getGUserId();


        try {
            // load client secrets
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(LoginController.class.getResourceAsStream(CREDENTIALS_FILE_PATH)));
            // set up authorization code flow
            FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(new java.io.File(TOKENS_FILE_PATH));
            flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(dataStoreFactory)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .addRefreshListener(new DataStoreCredentialRefreshListener(userId, dataStoreFactory))
                .build();
            Credential credential = flow.loadCredential(userId);
            // チームID
            Long teamId = SessionUtil.getUserInfo().getTeamId();
            String appName = iSystemPropertyService.getProperty(teamId, CommonConstant.APPLICATION_NAME);
            sheetsService = new Sheets.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(), credential)
                .setApplicationName(appName)
                .build();
            service = new Script.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(), credential)
                .setApplicationName(appName)
                .build();

        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Fvid取得
     *
     * @param releaseInfo リリース情報検索条件
     * @param checked 検索条件CheckBox
     * @param deployId GASデプロイ ID
     */
    private String getFvidByAppScript(ReleaseInfo releaseInfo, String checked, String deployId) {
        ExecutionRequest request = new ExecutionRequest()
            .setFunction("doGet");
        Map parameter = new HashMap();
        Map param = new HashMap();
        if (StringUtils.hasLength(releaseInfo.getSupportPeriod())) {
            param.put("support_period", releaseInfo.getSupportPeriod());
        }
        if (StringUtils.hasLength(releaseInfo.getSalesCategoryL())) {
            param.put("sales_category_l", releaseInfo.getSalesCategoryL());
        }
        if (StringUtils.hasLength(releaseInfo.getSalesCategoryR())) {
            param.put("sales_category_r", releaseInfo.getSalesCategoryR());
        }
        if (StringUtils.hasLength(releaseInfo.getPicId())) {
            param.put("pic_user", releaseInfo.getPicId());
        }
        if (StringUtils.hasLength(releaseInfo.getMakerCd())) {
            param.put("maker_name", releaseInfo.getMakerCd());
        }
        if (StringUtils.hasLength(releaseInfo.getCarModelCd())) {
            param.put("car_model_name", releaseInfo.getCarModelCd());
        }
        if (StringUtils.hasLength(releaseInfo.getCarModelGroupCd())) {
            param.put("car_model_group_name", releaseInfo.getCarModelGroupCd());
        }
        if (StringUtils.hasLength(releaseInfo.getReleaseCategory())) {
            param.put("release_category", releaseInfo.getReleaseCategory());
        }
        if (StringUtils.hasLength(checked)) {
            param.put("check", checked.toUpperCase());
        }
        if (param.keySet().size() == 0) {
            return null;
        }

        parameter.put("parameter", param);
        List paramList = new ArrayList();
        paramList.add(parameter);
        request.setParameters(paramList);
        try {
            if (service == null) {
                getService();
            }

            // Make the API request.
            Operation op =
                service.scripts().run(deployId, request).execute();

            // Print results of request.
            if (op.getError() == null) {
                // The result provided by the API needs to be cast into
                // the correct type, based upon what types the Apps
                // Script function returns. Here, the function returns
                // an Apps Script Object with String keys and values,
                // so must be cast into a Java Map (folderSet).
                Map<String, String> folderSet =
                    (Map<String, String>)(op.getResponse().get("result"));
                if (folderSet.size() > 0) {
                    for (String id: folderSet.keySet()) {
                        return folderSet.get(id);
                    }
                }
            }
        } catch (GoogleJsonResponseException e) {
            // The API encountered a problem before the script was called.
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Slack通知
     *
     * @param releaseInfo リリース情報
     */
    @SneakyThrows
    private Map sendSlack(ReleaseInfo releaseInfo) throws ServiceException {
        log.debug("Slackへ送信 開始です");
        try {
            // Slack文言 ・メッセージID
            String messageId = CommonConstant.SLACK_MEAAGE_ID_MAP.get(releaseInfo.getReleaseCategoryId());
            // Slack文言情報取得
            SlackDocumentMstEntity slackDocumentMstEntity = iSlackDocumentMstService.getById(messageId);
            // メッセージ固定文言
            String message = slackDocumentMstEntity.getMessageFixedWord();
            // リリース区分
            message = message.replace(CommonConstant.SLACK_MESSAGE_RELEASE_CATEGORY_KEY, releaseInfo.getReleaseCategory());
            // 車名
            message = message.replace(CommonConstant.SLACK_MESSAGE_CAR_NAME_KEY, releaseInfo.getCarModelName());
            // 販売区分の備考
            message = message.replace(CommonConstant.SLACK_MESSAGE_SALES_CATEGORY_NOTE_KEY, releaseInfo.getSalesCategoryNote());
            // 発売日
            message = message.replace(CommonConstant.SLACK_MESSAGE_LAUNCH_DATE_KEY, releaseInfo.getLaunchDate());
            // リリースURL
            message = message.replace(CommonConstant.SLACK_MESSAGE_RELEASE_URL_KEY, releaseInfo.getReleaseUrl());
            String slackText = message;

            SlackConfig config = new SlackConfig();
            config.setStatsEnabled(false);
            Slack slack = Slack.getInstance(config);

            // 環境変数を読み込みます
            // トークンがボットトークンであれば `xoxb-`、ユーザートークンであれば `xoxp-` で始まっているはずです
            String token = constant.getSlackToken();

            ChatPostMessageResponse response = slack.methods(token).chatPostMessage(req -> req
                .channel(slackDocumentMstEntity.getChannelInfo()) // チャンネル名を指定。
                .threadTs(slackDocumentMstEntity.getThreadTs()) // スレッドに書き込む場合はスレッドを指定。必須項目ではない。
                .text(slackText));
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
