package com.gem.loganalysis.model.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gem.loganalysis.model.dto.edit.EventTypeInsertDTO;
import com.gem.loganalysis.model.dto.edit.EventTypeUpdateDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 事件类型
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EventType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件类型
     */
    @TableId("EVENT_TYPE")
    private String eventType;

    /**
     * 事件等级
     */
    @TableField("EVENT_CLASS")
    private String eventClass;

    /**
     * 封堵规则编码
     */
    @TableField("BLOCK_RULE_ID")
    private String blockRuleId;

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

    public EventType(EventTypeInsertDTO dto) {
        BeanUtil.copyProperties(dto, this);
        this.createTime = DateUtil.now();
    }

    public EventType(EventTypeUpdateDTO dto) {
        BeanUtil.copyProperties(dto, this);
        this.updateTime = DateUtil.now();
    }


}
