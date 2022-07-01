package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 業務情報
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("work_info")
public class WorkInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * チームID
     */
    @MppMultiId
    private Long teamId;

    /**
     * 業務ID
     */
    @MppMultiId
    private Long workId;

    /**
     * 業務名
     */
    private String workName;

    /**
     * ソート順
     */
    private Integer sortOrder;


}
