package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * チーム情報
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("team_info")
public class TeamInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * チームID
     */
    @TableId(value = "team_id", type = IdType.INPUT)
    private Long teamId;

    /**
     * チーム名
     */
    private String teamName;


}
