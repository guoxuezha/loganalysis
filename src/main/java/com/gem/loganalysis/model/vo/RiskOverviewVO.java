package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    @ApiModelProperty(value = "未修复")
    private Integer toBeRepairedNum;

    /**
     * 修复中
     */
    @ApiModelProperty(value = "修复中")
    private Integer repairingNum;

    /**
     * 已修复
     */
    @ApiModelProperty(value = "已修复")
    private Integer repairedNum;

    /**
     * 忽略
     */
    @ApiModelProperty(value = "忽略")
    private Integer ignoreNum;

    /**
     * 每日风险数量
     */
    @ApiModelProperty(value = "每日风险数量")
    private List<RiskNumDailyList> riskNumDaily;

    /**
     * 风险类型分布
     */
    @ApiModelProperty(value = "风险类型分布")
    private List<HashMap<String, Object>> riskTypeDistribution;

    /**
     * 各类型风险Top5
     */
    @ApiModelProperty(value = "各类型风险Top5")
    private HashMap<String, List<RiskOverviewRecordVO>> riskTop5ByType;

    public void setRiskNumDaily(Map<String, List<RiskOverviewVO.RiskNumDaily>> map) {
        ArrayList<RiskNumDailyList> list = new ArrayList<>();
        for (Map.Entry<String, List<RiskNumDaily>> entry : map.entrySet()) {
            list.add(new RiskNumDailyList(entry.getKey(), entry.getValue()));
        }
        this.riskNumDaily = list;
    }

    public void setRiskTypeDistribution(HashMap<String, Integer> map) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            HashMap<String, Object> m = new HashMap<>();
            m.put("riskType", entry.getKey());
            m.put("num", entry.getValue());
            result.add(m);
        }
        this.riskTypeDistribution = result;
    }

    @Data
    @AllArgsConstructor
    static class RiskNumDailyList {

        private String riskType;

        private List<RiskOverviewVO.RiskNumDaily> list;
    }

    @Data
    @AllArgsConstructor
    public static class RiskNumDaily {

        private String date;

        private Integer num;
    }

}
