package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 車種系統マスタ
 * </p>
 *
 * @author keisho
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("car_model_group_master")
public class CarModelGroupMasterEntity implements Serializable {

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
     * 車種系統CD
     */
    @MppMultiId
    private Long carModelGroupCd;

    /**
     * 車種系統名
     */
    private String carModelGroupName;

    /**
     * 系統開始日
     */
    private String carModelGroupStart;

    /**
     * 系統終了日
     */
    private String carModelGroupEnd;

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
     * 基本ファイル名
     */
    private String fileName;

    /**
     * ユーザージャンル1
     */
    private String userGenre1;

    /**
     * ユーザージャンル2
     */
    private String userGenre2;


}
