package com.gem.loganalysis.model.dto.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("资产模块 - 物理资产扫描规则新增/编辑 DTO ")
@Data
public class PhysicalAssetDiscoveryRuleDTO {

    @ApiModelProperty(value = "唯一编码")
    private String physicalAssetDiscoveryId;

    @ApiModelProperty(value = "community字符串")
    private String community;

    @ApiModelProperty(value = "多组OID")
    private List<String> oidList;



}
