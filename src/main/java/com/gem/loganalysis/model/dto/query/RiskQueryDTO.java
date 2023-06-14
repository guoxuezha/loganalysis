package com.gem.loganalysis.model.dto.query;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/13 22:00
 */
@Data
public class RiskQueryDTO {

    private String assetId;

    private String riskType;

    private String riskLevel;

    private String handleStatus;

}
