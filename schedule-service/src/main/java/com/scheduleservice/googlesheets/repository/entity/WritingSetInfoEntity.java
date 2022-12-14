package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 書込定義情報
 * </p>
 *
 * @author keisho
 * @since 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("writing_set_info")
public class WritingSetInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * スプレッドシート種別 1：リリース情報
     */
    private String googleSheetsDiv;

    /**
     * 登録編集削除区分 0：登録、1：編集、2：削除
     */
    private String processDiv;

    /**
     * 連番 1から連番
     */
    private String seqNum;

    /**
     * 項目名 複数項目の場合「，」で連結
     */
    private String columnName;

    /**
     * 対象列
     */
    private String targetColumn;


}
