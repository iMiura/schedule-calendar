package com.scheduleservice.googlesheets.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scheduleservice.googlesheets.repository.entity.WritingSetInfoEntity;
import java.util.List;

/**
 * <p>
 * 書込定義情報 関連ビジネスロジックオブジェクト
 * </p>
 *
 * @author keisho
 * @since 2022-12-08
 */
public interface IWritingSetInfoService extends IService<WritingSetInfoEntity> {

    /**
     * 書込定義情報取得
     *
     * @param googleSheetsDiv スプレッドシート種別
     * @param processDiv 登録編集削除区分
     * @return List<WritingSetInfoEntity>
     */
    List<WritingSetInfoEntity> getWritingSetInfo(String googleSheetsDiv, String processDiv);
}
