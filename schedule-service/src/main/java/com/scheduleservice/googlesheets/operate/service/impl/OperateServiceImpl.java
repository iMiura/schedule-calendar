package com.scheduleservice.googlesheets.operate.service.impl;

import cn.hutool.core.date.DateUnit;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.scheduleservice.googlesheets.config.ConstantPropertiesConfig;
import com.scheduleservice.googlesheets.constant.CommonConstant;
import com.scheduleservice.googlesheets.exception.ServiceException;
import com.scheduleservice.googlesheets.login.controller.LoginController;
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
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * ?????????????????? ????????????????????????????????????????????????.
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

    private GoogleAuthorizationCodeFlow flow;
    /** Sheet service. */
    private Sheets sheetsService;

    @Override
    public Map getWorkTime(Long teamId, String ym, Long taskId) throws ServiceException {

        Map resultMap = new HashMap();

        // ?????????????????????
        TaskInfoEntity taskInfoEntity = iTaskInfoService.getTaskInfo(teamId, taskId);
        if (taskInfoEntity != null) {
            // ????????????
            resultMap.put("taskName", taskInfoEntity.getTaskName());
            // ??????????????????
            WorkInfoEntity workInfoEntity = iWorkInfoService.getWorkInfo(teamId, taskInfoEntity.getWorkId());
            // ?????????
            resultMap.put("workName", workInfoEntity.getWorkName());
        }

        // ??????????????????????????????
        List<WorkTimeManagementEntity> list = iWorkTimeManagementService.getWorkTime(teamId, ym, taskId);
        // ????????????
        double times = 0;
        // ???????????? ???1:???????????????2:????????????3:?????????
        int status = 0;
        // ????????????ID
        long timeRecordId = 0;
        // ????????????????????????
        String finalChangeDate = "";
        for (WorkTimeManagementEntity item : list) {
            timeRecordId = item.getTimeRecordId();
            status = item.getProgressStatus();
            finalChangeDate = DateUtil.getMilli(item.getFinalChangerDate());
            // ???????????????????????????
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

        // ?????????ID
        Long teamId = SessionUtil.getUserInfo().getTeamId();

        // ??????????????????????????????
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
        // ???????????? ???1:???????????????2:????????????3:?????????
        valueList.add("1");

        // Google Sheets??????????????????????????????
        updateSheet(teamId, ym, range, valueList);

        // ??????????????????
        WorkTimeManagementEntity workTimeManagementEntity = new WorkTimeManagementEntity();
        // ?????????ID
        workTimeManagementEntity.setTeamId(teamId);
        // ????????????????????????
        workTimeManagementEntity.setCalendarYm(ym);
        // ?????????ID
        workTimeManagementEntity.setTaskId(taskId);
        // ???????????? ???1:???????????????2:????????????3:?????????
        workTimeManagementEntity.setProgressStatus(1);
        // ????????????
        workTimeManagementEntity.setTimeRecordStart(LocalDateTime.now());
        // ??????????????????
        workTimeManagementEntity.setFinalChangerDate(LocalDateTime.now());

        return iWorkTimeManagementService.updateStart(workTimeManagementEntity);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public boolean updateTimeInfo(String ym, Long timeRecordId, String range, int status, String finalChangeDate) throws ServiceException {

        // ?????????ID
        Long teamId = SessionUtil.getUserInfo().getTeamId();

        // ????????????????????????
        WorkTimeManagementEntity workTimeManagementEntity = iWorkTimeManagementService.getById(timeRecordId);
        // ???????????? ???1:???????????????2:????????????3:?????????
        workTimeManagementEntity.setProgressStatus(status);
        // ????????????
        workTimeManagementEntity.setTimeRecordEnd(LocalDateTime.now());
        // ??????????????????
        workTimeManagementEntity.setFinalChangerDate(LocalDateTime.now());

        LocalDateTime localDateTime = DateUtil.parseUnixMilli(Long.parseLong(finalChangeDate));

        // ??????????????????????????????
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
        // Google Sheets??????????????????????????????
        updateSheet(teamId, ym, range, valueList);

        return iWorkTimeManagementService.updateTimeInfo(workTimeManagementEntity, localDateTime);
    }

    /**
     * Google Sheets??????????????????????????????
     *
     * @param teamId ?????????ID
     * @param ym ????????????????????????
     * @param range ?????????????????????A1:B1???
     * @param values ????????????????????????
     * @throws IOException
     */
    private void updateSheet(Long teamId, String ym, String range, List values) throws ServiceException {
        // ????????????????????????????????????
        GoogleSheetsInfoEntity googleSheetsInfoEntity = iGoogleSheetsInfoService.getGoogleSheetsInfo(teamId, ym);

        // ????????????ID
        String spreadsheetId = googleSheetsInfoEntity.getTaskListFileId();
        // ????????????
        String sheetName = googleSheetsInfoEntity.getTaskListSheetName();

        String ranges[] = range.split(":");
        List rangeList = new ArrayList();
        for (String ran : ranges) {
            rangeList.add(sheetName + "!" + ran + ":" + ran);
        }

        try {
            if (sheetsService == null) {
                getService();
            }
            UpdateValues.updateValues(sheetsService, spreadsheetId, rangeList, "USER_ENTERED", values);
            log.debug("Google Sheets ????????????????????????ID???" + spreadsheetId + "??????????????????" + sheetName
                + "???????????????" + range + "???????????????" + values + "???");
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public String authorize(String userId, ServletRequest request) throws ServiceException {

        JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
        String CREDENTIALS_FILE_PATH = constant.getCredentialsFilePath();
        String TOKENS_FILE_PATH = constant.getTokensFilePath();

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
            if (credential == null) {
                String webName = "";
                String webURI = request.getServletContext().getContextPath();
                if (StringUtils.hasLength(webURI)) {
                    webName = webURI;
                    WebUtils.toHttp(request).getSession(true).setAttribute("URL", WebUtils.toHttp(request).getHeader("Referer"));
                }
                String localAddr = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                if (StringUtils.hasLength(webName)) {
                    localAddr = localAddr + webName + "/account";
                }
                localAddr = localAddr + "/credential";
                GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
                url.setRedirectUri(localAddr);
                log.debug(url.build());
                return url.build();
            }

            return "";
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void credential(String userId, String code, ServletRequest request) {
        try {
            Credential credential = flow.loadCredential(userId);
            if (credential == null) {
                GoogleAuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code);
                String webName = "";
                String webURI = request.getServletContext().getContextPath();
                if (StringUtils.hasLength(webURI)) {
                    webName = webURI;
                }
                String localAddr =
                    request.getScheme() + "://" + request.getServerName() + ":" + request
                        .getServerPort();
                if (StringUtils.hasLength(webName)) {
                    localAddr = localAddr + webName + "/account";
                }
                localAddr = localAddr + "/credential";
                tokenRequest.setRedirectUri(localAddr);
                credential = flow.createAndStoreCredential(tokenRequest.execute(), userId);
            }
            // ?????????ID
            Long teamId = SessionUtil.getUserInfo().getTeamId();
            String appName = iSystemPropertyService.getProperty(teamId, CommonConstant.APPLICATION_NAME);
            sheetsService = new Sheets.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(), credential)
                .setApplicationName(appName)
                .build();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void getService() throws ServiceException {
        JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
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
            // ?????????ID
            Long teamId = SessionUtil.getUserInfo().getTeamId();
            String appName = iSystemPropertyService.getProperty(teamId, CommonConstant.APPLICATION_NAME);
            sheetsService = new Sheets.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(), credential)
                .setApplicationName(appName)
                .build();

        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
