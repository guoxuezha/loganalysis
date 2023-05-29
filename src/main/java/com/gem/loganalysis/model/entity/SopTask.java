package com.gem.loganalysis.model.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gem.loganalysis.model.dto.edit.TaskDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 调度任务-子任务
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_task")
@NoArgsConstructor
public class SopTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId("TASK_ID")
    private String taskId;

    /**
     * 工作ID
     */
    @TableField("JOB_ID")
    private String jobId;

    /**
     * 任务名称
     */
    @TableField("TASK_NAME")
    private String taskName;

    /**
     * 任务描述
     */
    @TableField("TASK_DESC")
    private String taskDesc;

    /**
     * 调用模式(空/STATIC/URL/SERVLET/WEBSERVICE)
     */
    @TableField("CALL_MODE")
    private String callMode;

    /**
     * 类名
     */
    @TableField("CLASS_NAME")
    private String className;

    /**
     * 方法名
     */
    @TableField("METHOD_NAME")
    private String methodName;

    /**
     * 调用路径
     */
    @TableField("URL")
    private String url;

    /**
     * ServiceContent
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 创建者
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 删除标记
     */
    @TableField("DELETE_STATE")
    private Integer deleteState;

    public SopTask(TaskDTO dto, boolean insert) {
        BeanUtil.copyProperties(dto, this);
        if (insert) {
            this.taskId = IdUtil.fastSimpleUUID();
            this.createTime = DateUtil.now();
        } else {
            this.updateTime = DateUtil.now();
        }
    }

}
