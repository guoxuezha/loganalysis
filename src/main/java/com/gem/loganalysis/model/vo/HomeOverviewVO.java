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
    private Integer assetTotalScore;

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

    @ApiModelProperty(value = "网络设备事件已处理次数")
    private Integer networkDeviceEventsResolvedCount;

    @ApiModelProperty(value = "网络设备事件未处理次数")
    private Integer networkDeviceEventsPendingCount;

    @ApiModelProperty(value = "安全设备事件已处理次数")
    private Integer securityDeviceEventsResolvedCount;

    @ApiModelProperty(value = "安全设备事件未处理次数")
    private Integer securityDeviceEventsPendingCount;

    @ApiModelProperty(value = "IT设备事件已处理次数")
    private Integer itDeviceEventsResolvedCount;

    @ApiModelProperty(value = "IT设备事件未处理次数")
    private Integer itDeviceEventsPendingCount;

    @ApiModelProperty(value = "终端设备总数")
    private Integer endpointDeviceTotalCount;

    @ApiModelProperty(value = "终端设备在线数量")
    private Integer endpointDeviceOnlineCount;

    @ApiModelProperty(value = "近六天日期集合")
    private List<String> dateList;

    @ApiModelProperty(value = "近六天低风险集合")
    private List<Integer> lowRiskCount;

    @ApiModelProperty(value = "近六天中风险集合")
    private List<Integer> mediumRiskCount;

    @ApiModelProperty(value = "近六天高风险集合")
    private List<Integer> highRiskCount;

    @ApiModelProperty(value = "近六天出口设备负荷(流量)集合")
    private List<Integer> exportDeviceLoad;

    @ApiModelProperty(value = "近六天安全设备资产评分集合")
    private List<Integer> securityDeviceAssetScore;

    @ApiModelProperty(value = "近六天网络设备资产评分集合")
    private List<Integer> networkDeviceAssetScore;

    @ApiModelProperty(value = "近六天IT设备资产评分集合")
    private List<Integer> itDeviceAssetScore;

    @ApiModelProperty(value = "近六天逻辑资产评分集合")
    private List<Integer> logicalAssetScore;

    @ApiModelProperty(value = "非终端风险资产排行")
    private List<RiskAssetRankingVO> nonEndpointRiskAssetRanking;

    @ApiModelProperty(value = "终端风险资产排行")
    private List<RiskAssetRankingVO> endpointRiskAssetRanking;


}
