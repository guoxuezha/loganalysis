package com.gem.loganalysis.model.dto.block;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/9 18:46
 */
@Data
@AllArgsConstructor
public class BlockExecuteHelper {

    private String operationAssetId;

    private String eventId;

    private String sourceIp;

    private String blockType;

    private String blockDuration;

}
