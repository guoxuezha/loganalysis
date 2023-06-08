package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * MIB指令集设置
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_mib_config")
public class MibConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * MIB版本
     */
    @TableField("MIB_VERSION")
    private String mibVersion;

    /**
     * SNMP命令要发送的OID
     */
    @TableField("OID")
    private String oid;

    /**
     * 命令发送方式，SNMPGET/SNMPWALK
     */
    @TableField("SNMP_METHOD")
    private String snmpMethod;

    /**
     * 对应指标分类
     */
    @TableField("MEASURE_TYPE")
    private String measureType;


}
