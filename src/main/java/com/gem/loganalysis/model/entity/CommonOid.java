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
 * 通用MIB库OID列表设置
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_common_oid")
public class CommonOid implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * MIB版本
     */
    @TableField("MIB_VERSION")
    private String mibVersion;

    /**
     * OID
     */
    @TableField("OID")
    private String oid;

    /**
     * OID名称
     */
    @TableField("OID_NAME")
    private String oidName;

    /**
     * 对应指标名称
     */
    @TableField("MEASURE_NAME")
    private String measureName;

    /**
     * 数据类型
     */
    @TableField("OID_DATATYPE")
    private String oidDatatype;

    /**
     * OID说明
     */
    @TableField("OID_DESC")
    private String oidDesc;


}
