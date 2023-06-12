package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 资产日志预览
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_asset_log_preview")
public class SopAssetLogPreview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主机IP
     */
    @TableField("HOST")
    private String host;

    /**
     * 日志优先级
     */
    @TableField("SEVERITY")
    private String severity;

    /**
     * 日志发生的子系统
     */
    @TableField("FACILITY")
    private String facility;

    /**
     * 时间戳
     */
    @TableField("TIMESTAMP")
    private String timestamp;

    /**
     * 日志消息体
     */
    @TableField("MESSAGE")
    private String message;

    /**
     * 消息标签
     */
    @TableField("TAG")
    private String tag;


}
