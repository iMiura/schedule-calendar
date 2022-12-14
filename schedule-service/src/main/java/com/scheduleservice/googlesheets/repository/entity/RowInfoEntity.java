package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 行数管理
 * </p>
 *
 * @author keisho
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("row_info")
public class RowInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * スプレッドシート種別 1：リリース情報
     */
    @TableId(value = "google_sheets_div", type = IdType.INPUT)
    private String googleSheetsDiv;

    /**
     * 行数
     */
    private Long rowNumber;


}
