package com.scheduleservice.googlesheets.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scheduleservice.googlesheets.repository.entity.GoogleSheetsInfoEntity;

/**
 * <p>
 * スプレッドシート情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
public interface IGoogleSheetsInfoService extends IService<GoogleSheetsInfoEntity> {

    /**
     * スプレッドシート情報取得
     *
     * @param teamId チームID
     * @param calendarYm カレンダー年月度
     */
    GoogleSheetsInfoEntity getGoogleSheetsInfo(Long teamId, String calendarYm);
}
