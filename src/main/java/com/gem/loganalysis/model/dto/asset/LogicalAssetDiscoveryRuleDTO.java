package com.gem.loganalysis.model.dto.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("资产模块 - 逻辑资产扫描规则新增/编辑 DTO ")
@Data
public class LogicalAssetDiscoveryRuleDTO {

    @ApiModelProperty(value = "唯一编码")
    private String logicalAssetDiscoveryId;

    @ApiModelProperty(value = "端口号")
    private Integer port;

    @ApiModelProperty(value = "网络协议")
    private String networkProtocol;

    @ApiModelProperty(value = "资产类型")
    private String assetType;


}
