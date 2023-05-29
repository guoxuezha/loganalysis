package com.gem.loganalysis.model.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gem.loganalysis.model.dto.edit.JobDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 调度任务-工作
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_job")
@NoArgsConstructor
public class SopJob implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工作ID
     */
    @TableId("JOB_ID")
    private String jobId;

    /**
     * 工作名称
     */
    @TableField("JOB_NAME")
    private String jobName;

    /**
     * 是否启动执行(0否 1是)
     */
    @TableField("EXECUTE_ON_START")
    private Integer executeOnStart;

    /**
     * 首次执行时间
     */
    @TableField("FIRST_FIRED_TIME")
    private String firstFiredTime;

    /**
     * 上次执行时间
     */
    @TableField("LAST_FIRED_TIME")
    private String lastFiredTime;

    /**
     * 下次执行时间
     */
    @TableField("NEXT_FIRED_TIME")
    private String nextFiredTime;

    /**
     * 执行周期定义(为遵循固定格式的字符串)
     */
    @TableField("CYCLE_DEFINE")
    private String cycleDefine;

    /**
     * 工作当前状态 -1: 未就绪 0: 就绪 1: 执行中
     */
    @TableField("CURRENT_STATUS")
    private Integer currentStatus;

    /**
     * 工作已执行次数
     */
    @TableField("COUNTS")
    private Integer counts;

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

    public SopJob(JobDTO dto, boolean insert) {
        BeanUtil.copyProperties(dto, this);
        if (insert) {
            this.jobId = IdUtil.fastSimpleUUID();
            this.currentStatus = -1;
            this.createTime = DateUtil.now();
        } else {
            this.updateTime = DateUtil.now();
        }
    }


}
