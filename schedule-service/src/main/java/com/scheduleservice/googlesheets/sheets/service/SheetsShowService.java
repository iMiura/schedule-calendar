package com.scheduleservice.googlesheets.sheets.service;

import com.scheduleservice.googlesheets.exception.ServiceException;

import java.io.IOException;
import java.util.Map;

/**
 * @author :keisho
 */
public interface SheetsShowService {

    /**
     * スプレッドシート情報取得
     *
     * @param calendarYm カレンダー年月度
     */
    Map getGoogleSheetsInfo(String calendarYm, String urlFlg, String userId) throws ServiceException;

    /**
     * カレンダー展開最終年月更新
     *
     * @param deployedYm カレンダー展開最終年月
     * @param finalChangeDate 最終更新日時
     */
    boolean updateCalendarDeployed(String deployedYm, String finalChangeDate) throws ServiceException;

    /**
     * フィルタ対象担当者プルダウンの選択肢取得
     */
    Map getUserList() throws ServiceException;

    /**
     * Slack送信
     */
    Map sendSlack(String slackText) throws ServiceException, IOException;

}
