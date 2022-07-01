package com.scheduleservice.googlesheets.security.session;

import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * session
 *
 * @author :keisho
 */
public class SessionUtil {

    /**
     * ユーザー情報取得
     *
     * @return ユーザー情報
     */
    public static UserInfoEntity getUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        return (UserInfoEntity) subject.getPrincipal();
    }

}
