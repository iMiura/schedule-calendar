package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * タスク情報
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("task_info")
public class TaskInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * チームID
     */
    @MppMultiId
    private Long teamId;

    /**
     * タスクID
     */
    @MppMultiId
    private Long taskId;

    /**
     * 業務ID
     */
    private Long workId;

    /**
     * タスク名
     */
    private String taskName;

    /**
     * ソート順
     */
    private Integer sortOrder;


}
