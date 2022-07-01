package com.scheduleservice.googlesheets.constant;

/**
 * @author :keisho
 */
public class CommonConstant {

    /**
     * token
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * token
     */
    public static final String BEARER = "Bearer ";

    /**
     * シートID
     */
    public static final String GID = "/edit#gid=";

    /**
     * システム運用開始年月度のプロパティ名
     */
    public static final String START_YM_KEY = "SYSTEM_OPERATION_START_YM";

    /**
     * APPLICATION_NAMEのプロパティ名
     */
    public static final String APPLICATION_NAME = "APPLICATION_NAME";

    /**
     * 進捗状況
     */
    public static final String[] PROGRESS_STATUS = {"開始", "再開", "一時停止", "終了"};

    /**
     * 画面名
     */
    public static final String[] RADIO_LABEL = {"タスクリスト", "業務フロー", "ｶﾚﾝﾀﾞｰ確定"};
}
