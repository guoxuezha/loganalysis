package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/5 9:11
 */
@ApiModel("资产模块 - 安全资产VO Response ")
@Data
public class AssetAccountRespVO {

    @ApiModelProperty(value = "网管账号")
    private String nmAccount;

    @ApiModelProperty(value = "网管密码")
    private String nmPassword;

}
