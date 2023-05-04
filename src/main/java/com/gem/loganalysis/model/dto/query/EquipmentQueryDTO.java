package com.gem.loganalysis.model.dto.query;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 11:15
 */
@Data
public class EquipmentQueryDTO {

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

}
