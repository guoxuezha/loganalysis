package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 调度任务-任务执行记录
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_task_log")
public class SopTaskLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务执行记录流水号
     */
    @TableId(value = "TASK_LOG_ID", type = IdType.AUTO)
    private Integer taskLogId;

    /**
     * 任务ID
     */
    @TableField("TASK_ID")
    private String taskId;

    /**
     * 任务开始时间
     */
    @TableField("TASK_START_TIME")
    private String taskStartTime;

    /**
     * 任务结束时间
     */
    @TableField("TASK_END_TIME")
    private String taskEndTime;

    /**
     * 1成功 0失败
     */
    @TableField("EXECUTE_STATUS")
    private Integer executeStatus;

    /**
     * 执行消息
     */
    @TableField("EXECUTE_MESSAGE")
    private String executeMessage;

    /**
     * 工作ID(冗余)
     */
    @TableField("JOB_ID")
    private String jobId;


}
