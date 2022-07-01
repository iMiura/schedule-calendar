package com.scheduleservice.googlesheets.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 日付型データを扱うユーティリティクラス
 *
 * @author :keisho
 */
public class DateUtil extends cn.hutool.core.date.DateUtil {

    /**
     * 日付の型変換（String⇒Date）
     *
     * @param strDate 日付（String）
     * @param format  フォーマット
     * @return 日付（Date）
     */
    public static LocalDateTime parseDate(String strDate, String format) {
        return LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 日付の型チェック
     *
     * @param strDate 日付（String）
     * @param format  フォーマット
     * @return boolean（True：日付である、False：日付でない）
     */
    public static boolean isDate(String strDate, String format) {

        try {
            parse(strDate, format);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 日付の型変換（LocalDateTime⇒Stirng）
     *
     * @param date LocalDateTime
     * @return 日付（String）
     */
    public static String formatDateTime(LocalDateTime date, String format) {
        return DateTimeFormatter.ofPattern(format).format(date);
    }

    /**
     * 現在時刻のタイムスタンプ
     *
     * @return タイムスタンプ（String）
     */
    public static String getMilli() {
        return String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    /**
     * 所定時間のタイムスタンプ ミリ秒
     *
     * @return タイムスタンプ（String）
     */
    public static String getMilli(LocalDateTime dateTime) {
        return String.valueOf(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    /**
     * 所定時間のタイムスタンプ 秒
     *
     * @return タイムスタンプ（String）
     */
    public static String getUnixMilli(LocalDateTime dateTime) {
        return String.valueOf(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000L);
    }

    /**
     * 所定時間のタイムスタンプ
     *
     * @return タイムスタンプ(long 形式)
     */
    public static long getUnixMilliLong(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000L;
    }

    /**
     * 所定時間のタイムスタンプ
     *
     * @return タイムスタンプ(Integer 形式)
     */
    public static Integer getUnixMilliInt(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Integer
            .parseInt(String.valueOf(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000));
    }

    /**
     * タイムスタンプを所定フォーマットの日付型（String） 秒
     *
     * @param timeMillis unixタイムスタンプ（10桁）
     * @param format     フォーマット
     * @return 日付（String）
     */
    public static String formatUnixMilli(Long timeMillis, String format) {
        if (timeMillis == null || timeMillis == 0) {
            return "";
        }
        LocalDateTime unixMilli = parseUnixMilli(timeMillis * 1000);
        formatDateTime(unixMilli, format);
        return formatDateTime(unixMilli, format);
    }

    /**
     * タイムスタンプを所定フォーマットの日付型（String） ミリ秒
     *
     * @param timeMillis unixタイムスタンプ（10桁）
     * @param format     フォーマット
     * @return 日付（String）
     */
    public static String formatUnixMilli(Integer timeMillis, String format) {
        if (timeMillis == null || timeMillis == 0) {
            return "";
        }
        return formatUnixMilli(Long.valueOf(timeMillis), format);
    }

    /**
     * タイムスタンプ（Long型）をLocalDateTimeに変換（ミリ秒）
     *
     * @return 日付（LocalDateTime）
     */
    public static LocalDateTime parseUnixMilli(Long timeMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault());
    }

    /**
     * タイムスタンプ（Integer型）をLocalDateTimeに変換（秒）
     *
     * @return 日付（LocalDateTime）
     */
    public static LocalDateTime parseUnixMilli(Integer timeMillis) {
        if (timeMillis == null || timeMillis == 0) {
            return null;
        }
        long timeMillisLong = timeMillis * 1000L;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillisLong), ZoneId.systemDefault());
    }

    /**
     * タイムスタンプを取得
     *
     * @return タイムスタンプ（Long型）
     */
    public static Long getLongUnixMilli() {
        return System.currentTimeMillis() / 1000L;
    }

    /**
     * タイムスタンプを取得（秒）
     *
     * @return タイムスタンプ（int型）
     */
    public static int getIntUnixMilli() {
        Long ret = System.currentTimeMillis() / 1000L;
        return ret.intValue();
    }

    /**
     * タイムスタンプを取得（ミリ秒）
     *
     * @return タイムスタンプ（int型）
     */
    public static int getUnixMilli() {
        return getLongUnixMilli().intValue();
    }

    /**
     * 所定日付（文字列）をタイムスタンプに変換
     * @param strDate 日付（String）
     * @param format  フォーマット
     *
     * @return タイムスタンプ（Long型）
     */
    public static Long parseLongUnixMilli(String strDate, String format) {
        LocalDateTime localDateTime = parseDate(strDate, format);
        return Long.valueOf(getUnixMilli(localDateTime));
    }

    /**
     * 所定日付（文字列）をタイムスタンプに変換
     * @param strDate 日付（String）
     * @param format  フォーマット
     *
     * @return タイムスタンプ（Integer型）
     */
    public static Integer parseUnixMilli(String strDate, String format) {
        LocalDateTime localDateTime = parseDate(strDate, format);
        return Integer.valueOf(getUnixMilli(localDateTime));
    }

    /**
     * システム年より指定年数までの年（YYYY）リスト
     *
     * @param limit 年数
     * @return 年List
     */
    public static List<Integer> getRecentYears(int limit) {
        int year = thisYear();
        List<Integer> yearList = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            yearList.add(year--);
        }
        return yearList;
    }

}
