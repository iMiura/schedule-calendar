package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 役割情報
 * </p>
 *
 * @author keisho
 * @since 2022-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("role_info")
public class RoleInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 役割ID
     */
    @TableId(value = "role_id", type = IdType.INPUT)
    private Long roleId;

    /**
     * 役割名
     */
    private String roleName;


}
