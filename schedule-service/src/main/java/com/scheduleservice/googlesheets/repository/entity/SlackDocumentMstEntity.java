package com.scheduleservice.googlesheets.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * Slack文言マスタ
 * </p>
 *
 * @author keisho
 * @since 2022-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("slack_document_mst")
public class SlackDocumentMstEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * メッセージID
     */
    @TableId(value = "meaage_id", type = IdType.INPUT)
    private String meaageId;

    /**
     * チャンネル
     */
    private String channelInfo;

    /**
     * 返信スレッド
     */
    private String threadTs;

    /**
     * メッセージ固定文言
     */
    private String messageFixedWord;


}
