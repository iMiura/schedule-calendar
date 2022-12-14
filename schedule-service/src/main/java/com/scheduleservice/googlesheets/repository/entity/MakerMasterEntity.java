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
 * メーカーマスタ
 * </p>
 *
 * @author keisho
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("maker_master")
public class MakerMasterEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * メーカーCD
     */
    @TableId(value = "maker_cd", type = IdType.INPUT)
    private Long makerCd;

    /**
     * メーカー
     */
    private String makerName;

    /**
     * Wﾒｰｶｰ_en
     */
    private String wMakerEn;

    /**
     * 先頭文字
     */
    private String firstChar;

    /**
     * 削除フラグ
     */
    private Integer deleteFlg;

    /**
     * 更新日時
     */
    private LocalDateTime updateDate;

    /**
     * 担当者コード
     */
    private String picCode;

    /**
     * 国名
     */
    private String country;


}
