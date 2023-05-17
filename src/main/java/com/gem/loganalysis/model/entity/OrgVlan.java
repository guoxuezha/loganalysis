package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

/**
 * 安全管理资产 DO
 *
 * @author czw
 */
@TableName("SOP_ORG_VLAN")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgVlan implements Serializable {

    /**
     * 组织机构唯一编码(外键组织表)
     */
    @TableId(type = IdType.INPUT)
    private String orgId;
    /**
     * VLAN,形如[["192.168.0.0","192.168.0.255"]]的JSON二维数组
     */
    private String vlan;


}
