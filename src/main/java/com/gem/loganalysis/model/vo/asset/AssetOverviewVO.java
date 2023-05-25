package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.util.Comparator.comparingInt;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/24 14:24
 */
@Data
@NoArgsConstructor
@ApiModel("资产模块 - 资产总览VO Response ")
public class AssetOverviewVO {

    @ApiModelProperty(value = "物理资产总数")
    private Integer physicalAssetNum;

    @ApiModelProperty(value = "逻辑资产总数")
    private Integer logicalAssetNum;

    @ApiModelProperty(value = "逻辑资产类型分布")
    private List<HashMap<String, Object>> logicalAssetDistribution;

    @ApiModelProperty(value = "物理资产类型分布")
    private List<HashMap<String, Object>> physicalAssetDistribution;

    @ApiModelProperty(value = "资产状态分布")
    private List<HashMap<String, Object>> assetStatusDistribution;

    @ApiModelProperty(value = "最近新增资产(10条)")
    private List<NewAssetList> newAssetList;

    @ApiModelProperty(value = "最近资产发现(5条)")
    private List<NewAssetScanList> newAssetScanList;

    @ApiModelProperty(value = "主机开放端口Top5")
    private List<TypeNum> ipTop5;

    @ApiModelProperty(value = "主机端口Top5")
    private List<TypeNum> ipPortTop5;

    @ApiModelProperty(value = "资产趋势")
    private List<AssetTrendsList> assetTrendsList;

    @Data
    @AllArgsConstructor
    static class NewAssetList {
        @ApiModelProperty(value = "资产名称")
        private String assetName;

        @ApiModelProperty(value = "资产类型")
        private String assetTypeName;

        @ApiModelProperty(value = "IP地址")
        private String ipAddress;

        @ApiModelProperty(value = "端口")
        private String servicePort;

        @ApiModelProperty(value = "新增时间")
        private String createTime;
    }

    @Data
    @AllArgsConstructor
    static class NewAssetScanList {
        @ApiModelProperty(value = "发现IP")
        private String ipAddress;

        @ApiModelProperty(value = "发现时间")
        private String scanTime;
    }


    @Data
    @AllArgsConstructor
    static class AssetTrendsList {
        @ApiModelProperty(value = "逻辑资产/物理资产")
        private String assetClass;

        @ApiModelProperty(value = "资产趋势列表详情")
        private List<AssetOverviewVO.AssetTrendsNum> list;
    }

    @Data
    @AllArgsConstructor
    public static class AssetTrendsNum {
        @ApiModelProperty(value = "日期")
        private String date;

        @ApiModelProperty(value = "数量")
        private Integer num;
    }

    @AllArgsConstructor
    @Getter
    public static class TypeNum implements Comparable<AssetOverviewVO.TypeNum> {
        static final Comparator<AssetOverviewVO.TypeNum> COMPARATOR =
                comparingInt(AssetOverviewVO.TypeNum::getNum);

        protected String name;

        protected Integer num;

        @Override
        public int compareTo(@NotNull AssetOverviewVO.TypeNum o) {
            return COMPARATOR.compare(o, this);
        }
    }


}
