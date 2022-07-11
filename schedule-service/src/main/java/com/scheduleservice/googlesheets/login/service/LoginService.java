package com.scheduleservice.googlesheets.login.service;

import com.scheduleservice.googlesheets.exception.ServiceException;
import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import javax.servlet.ServletRequest;

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
     */
    String getClientId() throws ServiceException;

    /**
     * Google Sheetsの所定セルに値を投入時の権限認証
     *
     * @param userId ユーザID
     * @param request ServletRequest
     * @return String
     * @throws ServiceException
     */
    String authorize(String userId, ServletRequest request) throws ServiceException;

    /**
     * Google Sheetsの所定セルに値を投入時の権限認証
     *
     * @param userId ユーザID
     * @param request ServletRequest
     * @param code 権限認証
     */
    void credential(String userId, String code, ServletRequest request);
}
