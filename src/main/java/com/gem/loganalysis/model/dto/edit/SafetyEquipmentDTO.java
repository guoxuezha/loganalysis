package com.gem.loganalysis.model.dto.edit;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 11:21
 */
@Data
public class SafetyEquipmentDTO {

    /**
     * 安全设备ID
     */
    private Integer equipId;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 设备类型
     */
    private String equipType;

    /**
     * 所属组织机构
     */
    private String orgId;

    /**
     * 设备名称
     */
    private String equipName;

    /**
     * 设备描述
     */
    private String equipDesc;

    /**
     * 远程登陆账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 设备状态
     */
    private Integer equipState;

    /**
     * 负责人
     */
    private String manager;
}
