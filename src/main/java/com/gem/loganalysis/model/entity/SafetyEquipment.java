package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 安全设备
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SafetyEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 安全设备ID
     */
    @TableField("EQUIP_ID")
    private Integer equipId;

    /**
     * IP地址
     */
    @TableId("IP")
    private String ip;

    /**
     * 端口
     */
    @TableField("PORT")
    private String port;

    /**
     * 设备类型
     */
    @TableField("EQUIP_TYPE")
    private String equipType;

    /**
     * 所属组织机构
     */
    @TableField("ORG_ID")
    private String orgId;

    /**
     * 设备名称
     */
    @TableField("EQUIP_NAME")
    private String equipName;

    /**
     * 设备描述
     */
    @TableField("EQUIP_DESC")
    private String equipDesc;

    /**
     * 远程登陆账号
     */
    @TableField("ACCOUNT")
    private String account;

    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 设备状态
     */
    @TableField("EQUIP_STATE")
    private Integer equipState;

    /**
     * 负责人
     */
    @TableField("MANAGER")
    private String manager;

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


}
