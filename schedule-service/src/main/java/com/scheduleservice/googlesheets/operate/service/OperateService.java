package com.scheduleservice.googlesheets.operate.service;

import com.scheduleservice.googlesheets.exception.ServiceException;
import java.util.Map;
import javax.servlet.ServletRequest;

/**
 * @author :keisho
 */
public interface OperateService {

    /**
     * 作業時間取得
     *
     * @param teamId チームID
     * @param ym カレンダー年月度
     * @param taskId タスクID
     */
    Map getWorkTime(Long teamId, String ym, Long taskId) throws ServiceException;

    /**
     * 作業時間管理 開始
     *
     * @param ym カレンダー年月度
     * @param taskId タスクID
     * @param range セル指定（例：A1:B1）
     * @param status 進捗状況 【1:着手中」「2:中断」「3:終了】
     * @param finalChangeDate 最終更新日時
     */
    boolean updateStart(String ym, Long taskId, String range, int status, String finalChangeDate) throws ServiceException;

    /**
     * 作業時間管理 開始
     *
     * @param ym カレンダー年月度
     * @param timeRecordId 作業時間ID
     * @param range セル指定（例：A1:B1）
     * @param status 進捗状況 【1:着手中」「2:中断」「3:終了】
     * @param finalChangeDate 最終更新日時
     */
    boolean updateTimeInfo(String ym, Long timeRecordId, String range, int status, String finalChangeDate) throws ServiceException;

    /**
     * Google Sheetsの所定セルに値を投入時の権限認証
     *
     * @param userId ユーザID
     * @param request ServletRequest
     * @return String
     * @throws ServiceException
     */
    String authorize(String userId, ServletRequest request) throws ServiceException;

    /**
     * Google Sheetsの所定セルに値を投入時の権限認証
     *
     * @param userId ユーザID
     * @param request ServletRequest
     * @param code 権限認証
     */
    void credential(String userId, String code, ServletRequest request);
}
