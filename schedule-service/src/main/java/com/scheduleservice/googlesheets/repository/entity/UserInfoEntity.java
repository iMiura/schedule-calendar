package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * ユーザ情報
 * </p>
 *
 * @author keisho
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_info")
public class UserInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ユーザID
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;

    /**
     * ユーザ名
     */
    private String userName;

    /**
     * チームID
     */
    private Long teamId;

    /**
     * メールアドレス
     */
    private String gmailAddress;

    /**
     * 権限 【オーナー:0 / 編集:1 / 閲覧:2】
     */
    private Integer userPermission;

    /**
     * 削除フラグ 【有効:0 / 削除:1】
     */
    private Integer delFlg;

    /**
     * GOOGLE ユーザID
     */
    @TableField(exist = false)
    private String gUserId;

}
