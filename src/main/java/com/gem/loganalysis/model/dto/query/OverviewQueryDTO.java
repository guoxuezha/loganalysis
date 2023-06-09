package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 风险/事件总览查询参数
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/23 10:44
 */
@Data
public class OverviewQueryDTO {

    /**
     * 总览周期
     * (1-周 2-月 3-季)
     */
    @ApiModelProperty(value = "总览周期(0-日 1-周 2-月 3-季)")
    private Integer cycle;

}
