package com.scheduleservice.googlesheets.constant;

import java.util.HashMap;
import java.util.Map;

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
     * Slack文言 ・メッセージID「MSG-001、MSG-002、MSG-003、MSG-004」
     */
    public static final Map<String, String> SLACK_MEAAGE_ID_MAP = new HashMap();
    static {
        SLACK_MEAAGE_ID_MAP.put("0", "MSG-001");
        SLACK_MEAAGE_ID_MAP.put("1", "MSG-002");
        SLACK_MEAAGE_ID_MAP.put("2", "MSG-003");
        SLACK_MEAAGE_ID_MAP.put("3", "MSG-004");
    }

    /**
     * Slack文言変更KEY リリース区分「$1」
     */
    public static final String SLACK_MESSAGE_RELEASE_CATEGORY_KEY = "$1";
    /**
     * Slack文言変更KEY 車名「$2」
     */
    public static final String SLACK_MESSAGE_CAR_NAME_KEY = "$2";
    /**
     * Slack文言変更KEY 販売区分の備考「$3」
     */
    public static final String SLACK_MESSAGE_SALES_CATEGORY_NOTE_KEY = "$3";
    /**
     * Slack文言変更KEY 発売日「$4」
     */
    public static final String SLACK_MESSAGE_LAUNCH_DATE_KEY = "$4";
    /**
     * Slack文言変更KEY リリースURL「$5」
     */
    public static final String SLACK_MESSAGE_RELEASE_URL_KEY = "$5";
}
