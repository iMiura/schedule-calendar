package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 車種マスタ
 * </p>
 *
 * @author keisho
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("car_model_master")
public class CarModelMasterEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * メーカーCD
     */
    @MppMultiId
    private Long makerCd;

    /**
     * 車種CD
     */
    @MppMultiId
    private Long carModelCd;

    /**
     * 車種
     */
    private String carModelName;

    /**
     * W車種名称_en
     */
    private String wCarModelEn;

    /**
     * 国産外車
     */
    private String domesticOrForeign;

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


}
