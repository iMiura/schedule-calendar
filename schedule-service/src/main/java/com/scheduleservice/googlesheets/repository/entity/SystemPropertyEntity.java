package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * システムプロパティ
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("system_property")
public class SystemPropertyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * チームID
     */
    @MppMultiId
    private Long teamId;

    /**
     * プロパティ名
     */
    @MppMultiId
    private String propertyKey;

    /**
     * プロパティ値
     */
    private String propertyValue;

    /**
     * 説明
     */
    private String note;


}
