package com.scheduleservice.googlesheets.operate.service.impl;

import cn.hutool.core.date.DateUnit;
import com.scheduleservice.googlesheets.config.ConstantPropertiesConfig;
import com.scheduleservice.googlesheets.constant.CommonConstant;
import com.scheduleservice.googlesheets.exception.ServiceException;
import com.scheduleservice.googlesheets.operate.service.OperateService;
import com.scheduleservice.googlesheets.repository.entity.GoogleSheetsInfoEntity;
import com.scheduleservice.googlesheets.repository.entity.TaskInfoEntity;
import com.scheduleservice.googlesheets.repository.entity.WorkInfoEntity;
import com.scheduleservice.googlesheets.repository.entity.WorkTimeManagementEntity;
import com.scheduleservice.googlesheets.repository.service.IGoogleSheetsInfoService;
import com.scheduleservice.googlesheets.repository.service.ISystemPropertyService;
import com.scheduleservice.googlesheets.repository.service.ITaskInfoService;
import com.scheduleservice.googlesheets.repository.service.IWorkInfoService;
import com.scheduleservice.googlesheets.repository.service.IWorkTimeManagementService;
import com.scheduleservice.googlesheets.security.session.SessionUtil;
import com.scheduleservice.googlesheets.util.DateUtil;
import com.scheduleservice.googlesheets.util.UpdateValues;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 作業時間情報 関連ビジネスロジックオブジェクト.
 */
@Service
@Slf4j
public class OperateServiceImpl implements OperateService {

    @Autowired
    private ITaskInfoService iTaskInfoService;
    @Autowired
    private IWorkInfoService iWorkInfoService;
    @Autowired
    private IWorkTimeManagementService iWorkTimeManagementService;
    @Autowired
    private IGoogleSheetsInfoService iGoogleSheetsInfoService;
    @Autowired
    private ISystemPropertyService iSystemPropertyService;
    @Autowired
    private ConstantPropertiesConfig constant;

    @Override
    public Map getWorkTime(Long teamId, String ym, Long taskId) throws ServiceException {

        Map resultMap = new HashMap();

        // タスク情報取得
        TaskInfoEntity taskInfoEntity = iTaskInfoService.getTaskInfo(teamId, taskId);
        if (taskInfoEntity != null) {
            // タスク名
            resultMap.put("taskName", taskInfoEntity.getTaskName());
            // 業務情報取得
            WorkInfoEntity workInfoEntity = iWorkInfoService.getWorkInfo(teamId, taskInfoEntity.getWorkId());
            // 業務名
            resultMap.put("workName", workInfoEntity.getWorkName());
        }

        // 作業時間管理情報取得
        List<WorkTimeManagementEntity> list = iWorkTimeManagementService.getWorkTime(teamId, ym, taskId);
        // 作業時間
        double times = 0;
        // 進捗状況 【1:着手中」「2:中断」「3:終了】
        int status = 0;
        // 作業時間ID
        long timeRecordId = 0;
        // 最終更新日時取得
        String finalChangeDate = "";
        for (WorkTimeManagementEntity item : list) {
            timeRecordId = item.getTimeRecordId();
            status = item.getProgressStatus();
            finalChangeDate = DateUtil.getMilli(item.getFinalChangerDate());
            // 作業時間を算出する
            if (item.getTimeRecordEnd() != null) {
                double difSec = (double) DateUtil.between(
                    Date.from(item.getTimeRecordStart().atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(item.getTimeRecordEnd().atZone(ZoneId.systemDefault()).toInstant()),
                    DateUnit.SECOND) / 60;
                times += difSec;
            }
        }
        if (0 < times && times < 1) {
            times = 1;
        } else {
            times = Math.ceil(times);
        }
        resultMap.put("timeRecordId", timeRecordId);
        resultMap.put("finalChangeDate", finalChangeDate);
        resultMap.put("workTimes", times);
        resultMap.put("status", status);

        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public boolean updateStart(String ym, Long taskId, String range, int status, String finalChangeDate) throws ServiceException {

        // チームID
        Long teamId = SessionUtil.getUserInfo().getTeamId();

        // 作業時間管理情報取得
        WorkTimeManagementEntity workTime = iWorkTimeManagementService.findWorkTimeNew(teamId, ym, taskId);
        if (workTime != null) {
            String finalChangeDateNew = DateUtil.getMilli(workTime.getFinalChangerDate());
            if (!StringUtils.hasLength(finalChangeDate)) {
                return false;
            } else if (finalChangeDateNew.compareTo(finalChangeDate) > 0) {
                return false;
            }
        }

        List valueList = new ArrayList();
        if (status == 0) {
            valueList.add(DateUtil.format(new java.util.Date(), "yyyy/MM/dd"));
        } else {
            valueList.add(null);
        }
        // 進捗状況 【1:着手中」「2:中断」「3:終了】
        valueList.add("1");

        // Google Sheetsの所定セルに値を投入
        updateSheet(teamId, ym, range, valueList);

        // 作業時間管理
        WorkTimeManagementEntity workTimeManagementEntity = new WorkTimeManagementEntity();
        // チームID
        workTimeManagementEntity.setTeamId(teamId);
        // カレンダー年月度
        workTimeManagementEntity.setCalendarYm(ym);
        // タスクID
        workTimeManagementEntity.setTaskId(taskId);
        // 進捗状況 【1:着手中」「2:中断」「3:終了】
        workTimeManagementEntity.setProgressStatus(1);
        // 開始時刻
        workTimeManagementEntity.setTimeRecordStart(LocalDateTime.now());
        // 最終更新日時
        workTimeManagementEntity.setFinalChangerDate(LocalDateTime.now());

        return iWorkTimeManagementService.updateStart(workTimeManagementEntity);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public boolean updateTimeInfo(String ym, Long timeRecordId, String range, int status, String finalChangeDate) throws ServiceException {

        // チームID
        Long teamId = SessionUtil.getUserInfo().getTeamId();

        // 作業時間管理情報
        WorkTimeManagementEntity workTimeManagementEntity = iWorkTimeManagementService.getById(timeRecordId);
        // 進捗状況 【1:着手中」「2:中断」「3:終了】
        workTimeManagementEntity.setProgressStatus(status);
        // 終了時刻
        workTimeManagementEntity.setTimeRecordEnd(LocalDateTime.now());
        // 最終更新日時
        workTimeManagementEntity.setFinalChangerDate(LocalDateTime.now());

        LocalDateTime localDateTime = DateUtil.parseUnixMilli(Long.parseLong(finalChangeDate));

        // 作業時間管理情報取得
        WorkTimeManagementEntity workTime = iWorkTimeManagementService.findWorkTimeNew(teamId, ym, workTimeManagementEntity.getTaskId());
        if (workTime != null) {
            String finalChangeDateNew = DateUtil.getMilli(workTime.getFinalChangerDate());
            if (!StringUtils.hasLength(finalChangeDate)) {
                return false;
            } else if (finalChangeDateNew.compareTo(finalChangeDate) > 0) {
                return false;
            }
        }

        List valueList = new ArrayList();
        if (status == 3) {
            valueList.add(DateUtil.format(new java.util.Date(), "yyyy/MM/dd"));
        } else {
            valueList.add(null);
        }
        valueList.add(String.valueOf(status));
        // Google Sheetsの所定セルに値を投入
        updateSheet(teamId, ym, range, valueList);

        return iWorkTimeManagementService.updateTimeInfo(workTimeManagementEntity, localDateTime);
    }

    /**
     * Google Sheetsの所定セルに値を投入
     *
     * @param teamId チームID
     * @param ym カレンダー年月度
     * @param range セル指定（例：A1:B1）
     * @param values 投入値（リスト）
     * @throws IOException
     */
    private void updateSheet(Long teamId, String ym, String range, List values) throws ServiceException {
        // スプレッドシート情報取得
        GoogleSheetsInfoEntity googleSheetsInfoEntity = iGoogleSheetsInfoService.getGoogleSheetsInfo(teamId, ym);

        // ファイルID
        String spreadsheetId = googleSheetsInfoEntity.getTaskListFileId();
        // シート名
        String sheetName = googleSheetsInfoEntity.getTaskListSheetName();

        String ranges[] = range.split(":");
        List rangeList = new ArrayList();
        for (String ran : ranges) {
            rangeList.add(sheetName + "!" + ran + ":" + ran);
        }

        // GOOGLE ユーザID
        String gUserId = SessionUtil.getUserInfo().getGUserId();

        String appName = iSystemPropertyService.getProperty(teamId, CommonConstant.APPLICATION_NAME);
        try {
            String credentialsFilePath = constant.getCredentialsFilePath();
            String tokensFilePath = constant.getTokensFilePath();
            UpdateValues.updateValues(gUserId, credentialsFilePath, tokensFilePath, appName, spreadsheetId, rangeList, "USER_ENTERED", values);
            log.debug("Google Sheets の情報（ファイルID：" + spreadsheetId + "、シート名：" + sheetName
                + "、設定行：" + range + "、設定値：" + values + "）");
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
