package com.scheduleservice.googlesheets.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scheduleservice.googlesheets.repository.entity.SystemPropertyEntity;

/**
 * <p>
 * システムプロパティ 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
public interface ISystemPropertyService extends IService<SystemPropertyEntity> {

    /**
     * プロパティ値取得
     *
     * @param teamId チームID
     * @param propertyKey プロパティ名
     * @return String
     */
    String getProperty(Long teamId, String propertyKey);
}
