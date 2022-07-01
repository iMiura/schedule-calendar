package com.scheduleservice.googlesheets.enums;

/**
 * httpEnum
 *
 * @author :keisho
 */
public enum ResultCodeEnum {

    /**
     * api success code.
     */
    RESULT_SUCCESS_CODE(200),

    /**
     * api success code.
     */
    RESULT_WARNING_CODE(900),

    /**
     * api failed code.
     */
    RESULT_FAILED_CODE(500),
    /**
     * api failed code.
     */
    FORBIDDEN_CODE(403),
    /**
     * 401（権限なし）
     */
    UNAUTHORIZED(401),
    /**
     * SESSION失効
     */
    SESSION_EXPIRE(-9999),
    /**
     * 認証コード失効
     */
    VERIFY_EXPIRE_ERROR(-2),
    /**
     * token失効
     */
    TOKEN_EXPIRE(-9999),
    /**
     * tokenリフレッシュ
     */
    TOKEN_REFRESH(3);
    ;

    private final int code;

    ResultCodeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
