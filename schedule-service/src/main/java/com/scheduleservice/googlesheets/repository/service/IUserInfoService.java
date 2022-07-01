package com.scheduleservice.googlesheets.repository.service;

import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * ユーザ情報 関連ビジネスロジックオブジェクト.
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
public interface IUserInfoService extends IService<UserInfoEntity> {

    /**
     * ユーザ情報取得
     *
     * @param gmail メールアドレス
     * @return UserInfoEntity
     */
    UserInfoEntity getByGmail(String gmail);

}
