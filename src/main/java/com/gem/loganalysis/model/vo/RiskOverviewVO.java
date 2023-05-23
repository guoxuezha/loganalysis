package com.gem.loganalysis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/23 14:24
 */
@Data
@NoArgsConstructor
public class RiskOverviewVO {

    /**
     * 未修复
     */
    private Integer toBeRepairedNum;

    /**
     * 修复中
     */
    private Integer repairingNum;

    /**
     * 已修复
     */
    private Integer repairedNum;

    /**
     * 忽略
     */
    private Integer ignoreNum;

    /**
     * 每日风险数量
     */
    private Map<String, List<RiskNumDaily>> riskNumDaily;

    /**
     * 风险类型分布
     */
    private HashMap<String, Integer> riskTypeDistribution;

    /**
     * 各类型风险Top5
     */
    private HashMap<String, List<RiskOverviewRecordVO>> riskTop5ByType;

    @Data
    @AllArgsConstructor
    public static class RiskNumDaily {

        private String date;

        private Integer num;
    }

}
