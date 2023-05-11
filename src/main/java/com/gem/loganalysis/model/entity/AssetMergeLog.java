package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 归并日志内容
 *
 * @author GuoChao
 * @since 2023-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_asset_merge_log")
public class AssetMergeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 归并日志唯一编码
     */
    @TableId("LOG_ID")
    private String logId;

    /**
     * 关联关系编码
     */
    @TableField("RULE_RELA_ID")
    private String ruleRelaId;

    /**
     * 归并字段属性值列表
     */
    @TableField("UNION_KEY")
    private String unionKey;

    /**
     * 归并窗口时序点（一天中的第几个归并窗口）
     */
    @TableField("LOG_PERIOD")
    private Integer logPeriod;

    /**
     * 归并日志事件发生次数
     */
    @TableField("EVENT_COUNT")
    private Integer eventCount;

    /**
     * 最新刷新时间
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 日志消息
     */
    @TableField("MESSAGE")
    private String message;

    /**
     * 日志标签
     */
    @TableField("TAG")
    private String tag;


}
