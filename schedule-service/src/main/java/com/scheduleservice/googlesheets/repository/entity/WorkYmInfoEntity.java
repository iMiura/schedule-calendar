package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 年月度情報
 * </p>
 *
 * @author keisho
 * @since 2022-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("work_ym_info")
public class WorkYmInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * チームID
     */
    @MppMultiId
    private Long teamId;

    /**
     * カレンダー年月度
     */
    @MppMultiId
    private String calendarYm;

    /**
     * カレンダー年月度開始日
     */
    private String calendarYmStart;

    /**
     * カレンダー年月度終了日
     */
    private String calendarYmEnd;

}
