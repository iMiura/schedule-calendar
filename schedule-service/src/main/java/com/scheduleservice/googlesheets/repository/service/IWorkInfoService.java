package com.scheduleservice.googlesheets.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scheduleservice.googlesheets.repository.entity.WorkInfoEntity;

/**
 * <p>
 * 業務情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
public interface IWorkInfoService extends IService<WorkInfoEntity> {

    /**
     * 業務情報取得
     *
     * @param teamId チームID
     * @param workId 業務ID
     * @return WorkInfoEntity
     */
    WorkInfoEntity getWorkInfo(Long teamId, Long workId);
}
