package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 作業時間管理
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("work_time_management")
public class WorkTimeManagementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 作業時間ID
     */
    @TableId(value = "time_record_id", type = IdType.AUTO)
    private Long timeRecordId;

    /**
     * チームID
     */
    private Long teamId;

    /**
     * カレンダー年月度
     */
    private String calendarYm;

    /**
     * タスクID
     */
    private Long taskId;

    /**
     * 進捗状況 【1:着手中」「2:中断」「3:終了】
     */
    private Integer progressStatus;

    /**
     * 開始時刻
     */
    private LocalDateTime timeRecordStart;

    /**
     * 終了時刻
     */
    private LocalDateTime timeRecordEnd;

    /**
     * 最終更新日時 【排他制御用】
     */
    private LocalDateTime finalChangerDate;


}
