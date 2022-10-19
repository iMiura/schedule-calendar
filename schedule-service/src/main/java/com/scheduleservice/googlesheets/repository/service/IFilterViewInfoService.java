package com.scheduleservice.googlesheets.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scheduleservice.googlesheets.repository.entity.FilterViewInfoEntity;

/**
 * <p>
 * フィルタ情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
public interface IFilterViewInfoService extends IService<FilterViewInfoEntity> {

    /**
     * フィルタ情報取得
     *
     * @param teamId チームID
     * @param userId ユーザID
     * @param calendarYm カレンダー年月度
     * @return FilterViewInfoEntity
     */
    FilterViewInfoEntity getFilterViewInfo(Long teamId, Long userId, String calendarYm);
}
