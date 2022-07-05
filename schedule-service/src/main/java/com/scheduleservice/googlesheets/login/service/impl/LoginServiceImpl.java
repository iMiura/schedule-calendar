package com.scheduleservice.googlesheets.login.service.impl;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.scheduleservice.googlesheets.config.ConstantPropertiesConfig;
import com.scheduleservice.googlesheets.config.CustomMessageResource;
import com.scheduleservice.googlesheets.exception.ServiceException;
import com.scheduleservice.googlesheets.login.controller.LoginController;
import com.scheduleservice.googlesheets.login.service.LoginService;
import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.scheduleservice.googlesheets.repository.service.IUserInfoService;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ユーザー情報 関連ビジネスロジックオブジェクト.
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private CustomMessageResource messageSource;
    @Autowired
    private IUserInfoService iUserInfoService;
    @Autowired
    private ConstantPropertiesConfig constant;

    @Override
    public UserInfoEntity login(String userId, String gmail) throws ServiceException {

        // ユーザ情報取得
        UserInfoEntity userInfo = iUserInfoService.getByGmail(gmail);
        if (userInfo == null) {
            throw new ServiceException(messageSource.getMessage("errors.check.login"));
        } else {
            userInfo.setGUserId(userId);
            // Google Sheetsの所定セルに値を投入時の権限認証
            authorize(userId);
        }

        return userInfo;
    }

    @Override
    public String getClientId() throws ServiceException {
        JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        String CREDENTIALS_FILE_PATH = constant.getCredentialsFilePath();
        try {
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(constant.getClass().getResourceAsStream(CREDENTIALS_FILE_PATH)));
            return clientSecrets.getDetails().getClientId();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Google Sheetsの所定セルに値を投入時の権限認証
     *
     * @param userId - ユーザID
     * @return Credential
     * @throws IOException
     */
    private Credential authorize(String userId) throws ServiceException {

        JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
        String CREDENTIALS_FILE_PATH = constant.getCredentialsFilePath();

        try {
            // load client secrets
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(LoginController.class.getResourceAsStream(CREDENTIALS_FILE_PATH)));
            // set up authorization code flow
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();

            // authorize
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(14200).setCallbackPath("/authorize").build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver) {
                protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) {
                    log.debug(authorizationUrl.build());
                }
            }.authorize(userId);
            return credential;
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
