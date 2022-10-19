package com.scheduleservice.googlesheets.repository.service;

import com.scheduleservice.googlesheets.repository.entity.UserInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

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

    /**
     * 作業時間管理情報取得
     *
     * @return List<UserInfoEntity>
     */
    List<UserInfoEntity> getUserList();
}
