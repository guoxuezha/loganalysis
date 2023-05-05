package com.gem.loganalysis.model.vo;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/5 9:11
 */
@Data
public class EquipBlockRecordVO {

    private String equipId;

    private String severity;

    private String facility;

    private String blockOffTime;

    private String blockOffIp;

    private String blockStartTime;

    private String blockEndTime;

    private String blockState;

}
