package com.scheduleservice.googlesheets.login.service;

import com.scheduleservice.googlesheets.exception.ServiceException;
import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;

/**
 * @author :keisho
 */
public interface LoginService {

    /**
     * ログイン
     *
     * @param gmail メールアドレス
     */
    UserInfoEntity login(String userId, String gmail) throws ServiceException;

    /**
     * システムプロパティ情報取得
     *
     * @param gmail メールアドレス
     */
    String getClientId() throws ServiceException;
}
