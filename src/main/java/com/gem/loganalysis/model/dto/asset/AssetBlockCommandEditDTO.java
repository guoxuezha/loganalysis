package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/7 22:21
 */
@Data
public class AssetBlockCommandEditDTO {

    @ApiModelProperty(value = "资产ID", required = true)
    private String assetId;

    @ApiModelProperty(value = "封堵命令模板", required = true)
    private String blockCommand;

    @ApiModelProperty(value = "解封命令模板", required = true)
    private String deBlockCommand;

}
