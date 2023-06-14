package com.gem.loganalysis.model.vo.asset;

import com.gem.loganalysis.model.vo.AssetEventVO;
import com.gem.loganalysis.model.vo.EventOverviewVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

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
    private List<HashMap<String, Object>> logicalAssetTypeDistribution;

    @ApiModelProperty(value = "物理资产类型分布")
    private List<HashMap<String, Object>> physicalAssetTypeDistribution;

    @ApiModelProperty(value = "资产在役情况分布")
    private List<HashMap<String, Object>> assetStatusDistribution;

    @ApiModelProperty(value = "资产在线情况分布")
    private List<HashMap<String, Object>> assetOnlineStatusDistribution;

    @ApiModelProperty(value = "资产类别分布")
    private List<HashMap<String, Object>> assetCategoryDistribution;

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

    @ApiModelProperty(value = "资产趋势日期数组")
    private List<String> dateList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewAssetList {
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewAssetScanList {
        @ApiModelProperty(value = "发现IP")
        private String ipAddress;

        @ApiModelProperty(value = "发现时间")
        private String scanTime;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetTrendsList {
        @ApiModelProperty(value = "逻辑资产/物理资产")
        private String assetClass;

        @ApiModelProperty(value = "资产趋势列表详情")
        private List<Integer> list;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class AssetTrendsNum {
        @ApiModelProperty(value = "日期")
        private String date;

        @ApiModelProperty(value = "数量")
        private Integer num;
    }

    //放入物理资产
    public void setPhysicalAssetTypeDistribution(Map<String, List<AssetRespVO>> map) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<AssetRespVO>> entry : map.entrySet()) {
            HashMap<String, Object> m = new HashMap<>();
            m.put("assetType", entry.getKey());
            m.put("num", entry.getValue().size());
            result.add(m);
        }
        this.physicalAssetTypeDistribution = result;
    }
    //放入逻辑资产统计
    public void setLogicalAssetDistribution(Map<String, List<AssetRespVO>> map) {
      /*  ArrayList<TypeNum> list = new ArrayList<>();
        for (Map.Entry<String, List<AssetRespVO>> entry : map.entrySet()) {
            list.add(new TypeNum(entry.getKey(), entry.getValue().size()));
        }
       this.logicalAssetDistribution = list;*/
        List<HashMap<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<AssetRespVO>> entry : map.entrySet()) {
            HashMap<String, Object> m = new HashMap<>();
            m.put("assetType", entry.getKey());
            m.put("num", entry.getValue().size());
            result.add(m);
        }
        this.logicalAssetTypeDistribution = result;
    }


    //放入资产类别
    public void setAssetCategoryDistribution(Map<String, List<AssetRespVO>> map) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<AssetRespVO>> entry : map.entrySet()) {
            HashMap<String, Object> m = new HashMap<>();
            m.put("name", entry.getKey());
            m.put("num", entry.getValue().size());
            result.add(m);
        }
        this.assetCategoryDistribution = result;
    }

    //放入资产状态
    //在役 =在役 + 在线 + 离线  退役=退役
    public void setAssetStatusDistribution(Map<String, List<AssetRespVO>> map) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        //已纳管_在线   已纳管_离线  未纳管 三种状态
        int managedOnlineCount = 0; // 已纳管_在线 =在役 + 在线 的个数
        int managedOfflineCount = 0; // 已纳管_离线的个数 = 离线
        int unmanagedCount = 0; // 未纳管个数 = 退役+空
        for (Map.Entry<String, List<AssetRespVO>> entry : map.entrySet()) {
            // 统计各个状态的个数
            if (entry.getKey().equals("在役") || entry.getKey().equals("在线")) {
                managedOnlineCount += entry.getValue().size();
            } else if (entry.getKey().equals("离线")) {
                managedOfflineCount += entry.getValue().size();
            } else if (entry.getKey().equals("")|| entry.getKey().equals("退役")) {
                unmanagedCount += entry.getValue().size();
            }
        }
        HashMap<String, Object> active = new HashMap<>();
        active.put("assetStatus", "已纳管_在线");
        active.put("num", managedOnlineCount);
        result.add(active);
        HashMap<String, Object> retired = new HashMap<>();
        retired.put("assetStatus", "已纳管_离线");
        retired.put("num", managedOfflineCount);
        result.add(retired);
        HashMap<String, Object> empty = new HashMap<>();
        empty.put("assetStatus", "未纳管");
        empty.put("num", unmanagedCount);
        result.add(empty);
        this.assetStatusDistribution = result;
    }

    //放入资产在线状态
    public void setAssetOnlineStatusDistribution(Map<String, List<AssetRespVO>> map) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        int onlineCount = 0; // 在线个数
        int offlineCount = 0; // 离线个数
        int emptyCount = 0; // 空的个数
        for (Map.Entry<String, List<AssetRespVO>> entry : map.entrySet()) {
            // 统计各个状态的个数
            if (entry.getKey().equals("在线")) {
                onlineCount += entry.getValue().size();
            } else if (entry.getKey().equals("离线")) {
                offlineCount += entry.getValue().size();
            } else if (entry.getKey().equals("")) {
                emptyCount += entry.getValue().size();
            }
        }

        HashMap<String, Object> online = new HashMap<>();
        online.put("assetStatus", "在线");
        online.put("num", onlineCount);
        result.add(online);
        HashMap<String, Object> offline = new HashMap<>();
        offline.put("assetStatus", "离线");
        offline.put("num", offlineCount);
        result.add(offline);
        HashMap<String, Object> empty = new HashMap<>();
        empty.put("assetStatus", "未知");
        empty.put("num", emptyCount);
        result.add(empty);
        this.assetOnlineStatusDistribution = result;
    }

    //放入主机开放端口Top5
    public void setIpTop5(Map<String, List<AssetRespVO>> map) {
        ArrayList<TypeNum> list = new ArrayList<>();
        for (Map.Entry<String, List<AssetRespVO>> entry : map.entrySet()) {
            list.add(new TypeNum(entry.getKey(), entry.getValue().size()));
        }
        List<TypeNum> collect = list.stream()
                //.sorted(Comparator.comparing(TypeNum::getNum))
                .sorted()
                .collect(Collectors.toList());
        if (collect.size() > 5) {
            this.ipTop5 = collect.subList(0, 5);
        } else {
            this.ipTop5 = collect;
        }
    }


    //放入主机端口TOP5
    public void setIpPortTop5(Map<Integer, List<AssetRespVO>> map) {
        ArrayList<TypeNum> list = new ArrayList<>();
        for (Map.Entry<Integer, List<AssetRespVO>> entry : map.entrySet()) {
            list.add(new TypeNum(entry.getKey().toString(), entry.getValue().size()));
        }
        List<TypeNum> collect = list.stream()
                .sorted()
                .collect(Collectors.toList());
        if (collect.size() > 5) {
            this.ipPortTop5 = collect.subList(0, 5);
        } else {
            this.ipPortTop5 = collect;
        }
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
