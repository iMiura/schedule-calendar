package com.scheduleservice.googlesheets.release.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * リリース情報
 * </p>
 *
 * @author keisho
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReleaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * リリースNo.
     */
    private String releaseInfoId;

    /**
     * リリース区分
     */
    private String releaseCategoryId;
    private String releaseCategory;

    /**
     * 次回確認予定
     */
    private String nextConfirmDate;

    /**
     * 発表日
     */
    private String announcementDate;

    /**
     * 発売日
     */
    private String launchDate;

    /**
     * リリースURL
     */
    private String releaseUrl;

    /**
     * メーカーCD
     */
    private String makerCd;
    private String makerName;

    /**
     * 車種CD
     */
    private String carModelCd;
    private String carModelName;

    /**
     * 車種系統CD
     */
    private String carModelGroupCd;
    private String carModelGroupName;

    /**
     * 仮車種名
     */
    private String tempCarName;

    /**
     * 仮車種系統名
     */
    private String tempCarGroupName;

    /**
     * 販売区分（左側）
     */
    private String salesCategoryIdL;
    private String salesCategoryL;

    /**
     * 販売区分（右側）
     */
    private String salesCategoryIdR;
    private String salesCategoryR;

    /**
     * 販売区分の備考
     */
    private String salesCategoryNote;

    /**
     * 対応時期
     */
    private String supportPeriod;

    /**
     * 新車入力担当者ID
     */
    private String picId;
    private String userName;

    /**
     * 備考
     */
    private String note;

    /**
     * 更新者ID
     */
    private String loginUserName;

    /**
     * 更新日時
     */
    private String updateDate;

    /**
     * 削除フラグ
     */
    private String deleteFlg;

    private String triggerFlg;
}
