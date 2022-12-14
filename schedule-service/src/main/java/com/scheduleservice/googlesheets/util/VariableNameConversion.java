package com.scheduleservice.googlesheets.util;

/**
 * @author :keisho
 */
public class VariableNameConversion {

    /**
     * Stringフォーマット(例：release_info_id → releaseInfoId)
     *
     * @param lowerLineAndUppercaseStr
     * @return String
     */
    public static String lowerLineToHump(String lowerLineAndUppercaseStr) {
        String[] eachStr = lowerLineAndUppercaseStr.split("_");
        StringBuilder resStr = new StringBuilder();
        String firstStr = "";
        String tempStr = "";
        for (int i = 0; i < eachStr.length; i++) {
            if (i == 0) {
                firstStr = eachStr[0].toLowerCase();
                resStr.append(firstStr);
            } else {
                tempStr = capitalizeTheFirstLetter(eachStr[i]);
                resStr.append(tempStr);
            }
        }

        return resStr.toString();
    }

    /**
     * Stringフォーマット(例：releaseInfoId → ReleaseInfoId)
     *
     * @param str
     * @return String
     */
    public static String capitalizeTheFirstLetter(String str) {
        char firstChar = str.toUpperCase().charAt(0);
        String nextStr = str.toLowerCase().substring(1);
        return firstChar + nextStr;
    }
}
