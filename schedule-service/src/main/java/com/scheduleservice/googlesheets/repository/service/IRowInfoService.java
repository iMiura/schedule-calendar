package com.scheduleservice.googlesheets.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scheduleservice.googlesheets.repository.entity.RowInfoEntity;

/**
 * <p>
 * 行数管理 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-12-01
 */
public interface IRowInfoService extends IService<RowInfoEntity> {

    /**
     * 行数管理更新
     *
     * @param rowInfoEntity 行数管理Entity
     * @return boolean
     */
    boolean updateRowInfo(RowInfoEntity rowInfoEntity);
}
