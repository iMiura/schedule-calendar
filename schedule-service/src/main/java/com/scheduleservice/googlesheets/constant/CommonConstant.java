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

    /**
     * スプレッドシート種別 1：リリース情報
     */
    public static final String GOOGLE_SHEETS_DIV_1 = "1";

    /**
     * スプレッドシート・API区分 1：スプレッドシート情報
     */
    public static final String GOOGLE_SHEETS_API_DIV_1 = "1";

    /**
     * スプレッドシート・API区分 2：スプレッドAPI情報
     */
    public static final String GOOGLE_SHEETS_API_DIV_2 = "2";

    /**
     * スプレッドシート・API区分 3：スプレッドシート情報書込み用
     */
    public static final String GOOGLE_SHEETS_API_DIV_3 = "3";

    /**
     * フォーマット 「yyyy/MM/dd」
     */
    public static final String STRING_FORMAT_YMD = "yyyy/MM/dd";

    /**
     * フォーマット 「yyyyMMdd」
     */
    public static final String STRING_FORMAT_YMD_2 = "yyyyMMdd";

    /**
     * Slack文言 ・審議対象「MSG-001」
     */
    public static final String SLACK_MESSAGE_RELEASE_CATEGORY_0 = "@井上 恵一朗　@保坂和之　@青木 真一　@勝呂 春美\r\n";

    /**
     * Slack文言 ・情報のみ「MSG-004」
     */
    public static final String SLACK_MESSAGE_RELEASE_CATEGORY_3 = "@保坂和之\r\n";

    /**
     * Slack文言 「【」
     */
    public static final String SLACK_MESSAGE_RELEASE_CATEGORY_L = "【";

    /**
     * Slack文言 「】」
     */
    public static final String SLACK_MESSAGE_RELEASE_CATEGORY_R = "】";

    /**
     * Slack文言 「・」
     */
    public static final String SLACK_MESSAGE_CAR_NAME = "・";

    /**
     * Slack文言 「「」
     */
    public static final String SLACK_MESSAGE_SALES_CATEGORY_NOTE_L = "「";

    /**
     * Slack文言 「」」
     */
    public static final String SLACK_MESSAGE_SALES_CATEGORY_NOTE_R = "」";

    /**
     * Slack文言 「　」
     */
    public static final String SLACK_MESSAGE_SPACE = "　";

    /**
     * Slack文言 「\r\n」
     */
    public static final String SLACK_MESSAGE_NEW_LINE = "\r\n";
}
