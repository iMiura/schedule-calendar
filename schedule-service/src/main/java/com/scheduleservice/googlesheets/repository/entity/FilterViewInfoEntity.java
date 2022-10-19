package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * フィルタ情報
 * </p>
 *
 * @author keisho
 * @since 2022-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("filter_view_info")
public class FilterViewInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * チームID
     */
    @MppMultiId
    private Long teamId;

    /**
     * ユーザID
     */
    @MppMultiId
    private Long userId;

    /**
     * カレンダー年月度
     */
    @MppMultiId
    private String calendarYm;

    /**
     * フィルタービューID
     */
    private String filterViewId;

}
