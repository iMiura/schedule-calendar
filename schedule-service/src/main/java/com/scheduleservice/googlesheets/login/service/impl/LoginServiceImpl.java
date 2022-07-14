package com.scheduleservice.googlesheets.login.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.scheduleservice.googlesheets.config.ConstantPropertiesConfig;
import com.scheduleservice.googlesheets.config.CustomMessageResource;
import com.scheduleservice.googlesheets.exception.ServiceException;
import com.scheduleservice.googlesheets.login.service.LoginService;
import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.scheduleservice.googlesheets.repository.service.IUserInfoService;
import java.io.IOException;
import java.io.InputStreamReader;
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
}
