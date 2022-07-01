package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * スプレッドシート情報
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("google_sheets_info")
public class GoogleSheetsInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * カレンダー年月度
     */
    @MppMultiId
    private String calendarYm;

    /**
     * チームID
     */
    @MppMultiId
    private Long teamId;

    /**
     * スプレッドシートファイル保存先URL
     */
    private String googleSheetsFileUrl;

    /**
     * タスクリストファイルID
     */
    private String taskListFileId;

    /**
     * タスクリストシートID
     */
    private String taskListSheetId;

    /**
     * タスクリストシート名
     */
    private String taskListSheetName;

    /**
     * 業務フローファイルID
     */
    private String workListFileId;

    /**
     * 業務フローシートID
     */
    private String workListSheetId;

    /**
     * 業務フローシート名
     */
    private String workListSheetName;

    /**
     * カレンダーファイルID
     */
    private String calendarListFileId;

    /**
     * カレンダーシートID
     */
    private String calendarListSheetId;

    /**
     * カレンダーシート名
     */
    private String calendarListSheetName;


}
