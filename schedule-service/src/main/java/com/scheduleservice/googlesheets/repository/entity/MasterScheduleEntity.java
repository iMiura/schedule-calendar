package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * マスタスケジュール
 * </p>
 *
 * @author keisho
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("master_schedule")
public class MasterScheduleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 対応時期
     */
    @TableId(value = "support_period", type = IdType.INPUT)
    private String supportPeriod;

    /**
     * マスター締日
     */
    private LocalDateTime masterDeadline;

    /**
     * 提出日
     */
    private LocalDateTime submitDate;

    /**
     * 更新日
     */
    private LocalDateTime updateDate;


}
