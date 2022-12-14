package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
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
@TableName("release_info")
public class ReleaseInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * リリースNo.
     */
    @TableId(value = "release_info_id", type = IdType.AUTO)
    private Integer releaseInfoId;

    /**
     * リリース区分
     */
    private Long releaseCategory;

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
    private Long makerCd;

    /**
     * 車種CD
     */
    private Long carModelCd;

    /**
     * 車種系統CD
     */
    private Long carModelGroupCd;

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
    private Long salesCategoryL;

    /**
     * 販売区分（右側）
     */
    private Long salesCategoryR;

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
    private Long picId;

    /**
     * 備考
     */
    private String note;

    /**
     * 行数
     */
    private Long rowNumber;

    /**
     * 登録者ID
     */
    private Long releaseCerateUserId;

    /**
     * 登録日時
     */
    private LocalDateTime cerateDate;

    /**
     * 更新者ID
     */
    private Long releaseUpdateUserId;

    /**
     * 更新日時
     */
    private LocalDateTime updateDate;

    /**
     * 削除フラグ
     */
    private Integer deleteFlg;


}
