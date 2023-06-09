package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author czw
 * @version 1.0
 * @date 2023/6/12 17:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)//链式
@ApiModel("首页总览VO Response ")
public class HomeOverviewVO {

    @ApiModelProperty(value = "资产总数")
    private Integer assetTotalCount;

    @ApiModelProperty(value = "资产总评分")
    private Double assetTotalScore;

    @ApiModelProperty(value = "低危漏洞个数")
    private Integer lowVulnerabilityCount;

    @ApiModelProperty(value = "中危险漏洞个数")
    private Integer mediumVulnerabilityCount;

    @ApiModelProperty(value = "高危漏洞个数")
    private Integer highVulnerabilityCount;

    @ApiModelProperty(value = "安全设备全部数量")
    private Integer securityDeviceTotalCount;

    @ApiModelProperty(value = "安全设备存活数量")
    private Integer securityDeviceAliveCount;

    @ApiModelProperty(value = "IT设备全部数量")
    private Integer itDeviceTotalCount;

    @ApiModelProperty(value = "IT设备存活数量")
    private Integer itDeviceAliveCount;

    @ApiModelProperty(value = "网络设备全部数量")
    private Integer networkDeviceTotalCount;

    @ApiModelProperty(value = "网络设备存活数量")
    private Integer networkDeviceAliveCount;

    @ApiModelProperty(value = "逻辑资产数量")
    private Integer logicalAssetCount;

    @ApiModelProperty(value = "安全漏洞数量")
    private Integer securityVulnerabilityCount;

    @ApiModelProperty(value = "合规漏洞数量")
    private Integer complianceVulnerabilityCount;

    @ApiModelProperty(value = "云资产数量")
    private Integer cloudAssetRiskCount;

    @ApiModelProperty(value = "基线风险数量")
    private Integer baselineRiskCount;

    @ApiModelProperty(value = "风险总数")
    private Integer totalRiskCount;

    @ApiModelProperty(value = "网络设备事件总次数")
    private Integer networkDeviceEventsResolvedCount;

    @ApiModelProperty(value = "网络设备事件未处理次数")
    private Integer networkDeviceEventsPendingCount;

    @ApiModelProperty(value = "安全设备事件总次数")
    private Integer securityDeviceEventsResolvedCount;

    @ApiModelProperty(value = "安全设备事件未处理次数")
    private Integer securityDeviceEventsPendingCount;

    @ApiModelProperty(value = "IT设备事件总次数")
    private Integer itDeviceEventsResolvedCount;

    @ApiModelProperty(value = "IT设备事件未处理次数")
    private Integer itDeviceEventsPendingCount;

    @ApiModelProperty(value = "终端设备总数")
    private Integer endpointDeviceTotalCount;

    @ApiModelProperty(value = "终端设备在线数量")
    private Integer endpointDeviceOnlineCount;

    @ApiModelProperty(value = "近七天日期集合")
    private List<String> dateList;

    @ApiModelProperty(value = "近七天低风险集合")
    private List<Integer> lowRiskCount;

    @ApiModelProperty(value = "近七天中风险集合")
    private List<Integer> mediumRiskCount;

    @ApiModelProperty(value = "近七天高风险集合")
    private List<Integer> highRiskCount;

    @ApiModelProperty(value = "近七天出口设备负荷(流量)集合")
    private List<Double> exportDeviceLoad;

    @ApiModelProperty(value = "近七天安全设备资产评分集合")
    private List<Double> securityDeviceAssetScore;

    @ApiModelProperty(value = "近七天网络设备资产评分集合")
    private List<Double> networkDeviceAssetScore;

    @ApiModelProperty(value = "近七天IT设备资产评分集合")
    private List<Double> itDeviceAssetScore;

    @ApiModelProperty(value = "近七天逻辑资产评分集合")
    private List<Double> logicalAssetScore;

    @ApiModelProperty(value = "非终端风险资产排行")
    private List<RiskAssetRankingVO> nonEndpointRiskAssetRanking;

    @ApiModelProperty(value = "终端风险资产排行")
    private List<RiskAssetRankingVO> endpointRiskAssetRanking;


}
