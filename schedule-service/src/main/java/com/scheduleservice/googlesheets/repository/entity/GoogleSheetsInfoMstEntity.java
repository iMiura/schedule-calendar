package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * スプレッドシート情報管理マスタ
 * </p>
 *
 * @author keisho
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("google_sheets_info_mst")
public class GoogleSheetsInfoMstEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * スプレッドシート種別 1：リリース情報
     */
    @MppMultiId
    private String googleSheetsDiv;

    /**
     * スプレッドシート・API区分 1：スプレッドシート情報、2：スプレッドAPI情報
     */
    @MppMultiId
    private String googleSheetsApiDiv;

    /**
     * スプレッドシートファイル保存先URL
     */
    private String googleSheetsFileUrl;

    /**
     * スプレッドシートファイルID
     */
    private String googleSheetsFileId;

    /**
     * スプレッドシートシートID
     */
    private String googleSheetsSheetId;

    /**
     * スプレッドシートシート名
     */
    private String googleSheetsSheetName;


}
