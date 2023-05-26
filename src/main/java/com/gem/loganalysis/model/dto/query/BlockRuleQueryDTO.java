package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 21:48
 */
@Data
public class BlockRuleQueryDTO {

    /**
     * 封堵类型（0临时封堵/1永久封堵）
     */
    @ApiModelProperty("封堵类型（0临时封堵/1永久封堵）")
    private Integer blockType;

}
