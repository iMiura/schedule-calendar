package com.scheduleservice.googlesheets.util;

import com.alibaba.fastjson.JSON;
import com.scheduleservice.googlesheets.enums.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author keisho
 */
@Data
@ApiModel(value = "JsonResult", description = "データオブジェクトの返却")
public class JsonResult implements Serializable {

    private static final long serialVersionUID = 7130801760271010160L;

    @ApiModelProperty(required = true, value = "コード(200：正常，500：サーバ内部エラー，403：アクセス不可，401：アクセス権限無し，900：警告，-9999：コネクション失効)", dataType = "int", example = "200", position = 0)
    private int code = ResultCodeEnum.RESULT_SUCCESS_CODE.getCode();
    @ApiModelProperty(required = true, value = "レスポンス内容", dataType = "string", example = "ok", position = 1)
    private String msg;
    @ApiModelProperty(required = true, value = "レスポンス結果", dataType = "object", example = "[]", position = 2)
    private Object result;

    private JsonResult() {

    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public static JsonResult success() {
        JsonResult result = new JsonResult();
        result.setCode(ResultCodeEnum.RESULT_SUCCESS_CODE.getCode());
        result.setResult(null);
        return result;
    }

    public static JsonResult success(String message) {
        JsonResult result = new JsonResult();
        result.setCode(ResultCodeEnum.RESULT_SUCCESS_CODE.getCode());
        result.setMessage(message);
        result.setResult(null);
        return result;
    }

    public static JsonResult success(String message, Object data) {
        JsonResult result = new JsonResult();
        result.setCode(ResultCodeEnum.RESULT_SUCCESS_CODE.getCode());
        result.setMessage(message);
        result.setResult(data);
        return result;
    }

    public static JsonResult success(Object data) {
        JsonResult result = new JsonResult();
        result.setCode(ResultCodeEnum.RESULT_SUCCESS_CODE.getCode());
        result.setResult(data);
        return result;
    }

    public static JsonResult failed() {
        JsonResult result = new JsonResult();
        result.setCode(ResultCodeEnum.RESULT_FAILED_CODE.getCode());
        result.setResult(null);
        return result;
    }

    public static JsonResult failed(String message) {
        JsonResult result = new JsonResult();


        result.setCode(ResultCodeEnum.RESULT_FAILED_CODE.getCode());
        result.setMessage(message);
        result.setResult(null);
        return result;
    }

    public static JsonResult failed(String message, Object data) {
        JsonResult result = new JsonResult();
        result.setCode(ResultCodeEnum.RESULT_FAILED_CODE.getCode());
        result.setMessage(message);
        result.setResult(data);
        return result;
    }

    public static JsonResult failedAccessDenied(String message) {
        JsonResult result = new JsonResult();


        result.setCode(ResultCodeEnum.UNAUTHORIZED.getCode());
        result.setMessage(message);
        result.setResult(null);
        return result;
    }

    public static JsonResult failedWithCode(String message, int codeStatus) {
        JsonResult result = new JsonResult();


        result.setCode(codeStatus);
        result.setMessage(message);
        result.setResult(null);
        return result;
    }

    public static JsonResult resultWithCode(Object data, String message, int codeStatus) {
        JsonResult result = new JsonResult();


        result.setCode(codeStatus);
        result.setMessage(message);
        result.setResult(data);
        return result;
    }

    public static JsonResult warn(String message, Object data) {
        JsonResult result = new JsonResult();


        result.setCode(ResultCodeEnum.RESULT_WARNING_CODE.getCode());
        result.setMessage(message);
        result.setResult(data);
        return result;
    }

    public static String toJson(JsonResult ret) {
        return JSON.toJSONString(ret);
    }

    @Override
    public String toString() {
        return "JsonResult[state=" + code + "]";
    }

}
