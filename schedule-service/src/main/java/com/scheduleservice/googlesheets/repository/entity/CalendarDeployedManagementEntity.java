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
 * カレンダー展開管理
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("calendar_deployed_management")
public class CalendarDeployedManagementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * チームID
     */
    @TableId(value = "team_id", type = IdType.INPUT)
    private Long teamId;

    /**
     * カレンダー展開最終年月度
     */
    private String calendarDeployedYm;

    /**
     * 最終更新日時 【排他制御用】
     */
    private LocalDateTime finalChangerDate;


}
