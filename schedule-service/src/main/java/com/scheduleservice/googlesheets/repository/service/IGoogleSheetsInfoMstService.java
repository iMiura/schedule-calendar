package com.scheduleservice.googlesheets.repository.service;

import com.scheduleservice.googlesheets.repository.entity.GoogleSheetsInfoMstEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * スプレッドシート情報管理マスタ 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-11-30
 */
public interface IGoogleSheetsInfoMstService extends IService<GoogleSheetsInfoMstEntity> {

    /**
     * スプレッドシート情報取得
     *
     * @param googleSheetsDiv スプレッドシート種別
     * @param googleSheetsApiDiv スプレッドシート・API区分
     * @return GoogleSheetsInfoMstEntity
     */
    GoogleSheetsInfoMstEntity getGoogleSheetsInfo(String googleSheetsDiv, String googleSheetsApiDiv);
}
