package com.scheduleservice.googlesheets.sheets.service;

import com.scheduleservice.googlesheets.exception.ServiceException;
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
    Map getGoogleSheetsInfo(String calendarYm, String urlFlg, boolean filterFlg) throws ServiceException;

    /**
     * カレンダー展開最終年月更新
     *
     * @param deployedYm カレンダー展開最終年月
     * @param finalChangeDate 最終更新日時
     */
    boolean updateCalendarDeployed(String deployedYm, String finalChangeDate) throws ServiceException;
}
