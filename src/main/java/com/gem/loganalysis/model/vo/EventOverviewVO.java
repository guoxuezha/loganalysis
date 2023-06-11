package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

/**
 * 事件总览视图类
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/24 21:49
 */
@Getter
public class EventOverviewVO {

    @ApiModelProperty("根据处置状态统计")
    private List<TypeNum> handleStatusNum;

    @ApiModelProperty("根据事件类型统计")
    private List<TypeNum> eventTypeNum;

    @ApiModelProperty("根据事件级别统计")
    private List<TypeNum> eventClassNum;

    @ApiModelProperty("根据源端IP统计事件数Top5")
    private List<TypeNum> sourceIpTop5;

    @ApiModelProperty("根据设备ID统计事件数Top5")
    private List<TypeNum> assetEventTop5;

    @ApiModelProperty("根据目标IP统计事件数Top5")
    private List<TypeNum> targetIpTop5;

    @ApiModelProperty("时段内每日事件数")
    private HashMap<Integer, List<TypeNum>> dailyEventNumMap;

    @ApiModelProperty("未处置事件列表")
    private List<AssetEventVO> undisposedEventList;

    public void setHandleStatusNum(Map<Integer, List<AssetEventVO>> map) {
        this.handleStatusNum = new ArrayList<>();
        for (Map.Entry<Integer, List<AssetEventVO>> entry : map.entrySet()) {
            this.handleStatusNum.add(new TypeNum(entry.getKey().toString(), entry.getValue().size()));
        }
    }

    public void setEventTypeNum(Map<String, List<AssetEventVO>> map) {
        this.eventTypeNum = new ArrayList<>();
        for (Map.Entry<String, List<AssetEventVO>> entry : map.entrySet()) {
            this.eventTypeNum.add(new TypeNum(entry.getKey(), entry.getValue().size()));
        }
    }

    public void setEventClassNum(Map<String, List<AssetEventVO>> map) {
        this.eventClassNum = new ArrayList<>();
        for (Map.Entry<String, List<AssetEventVO>> entry : map.entrySet()) {
            this.eventClassNum.add(new TypeNum(entry.getKey(), entry.getValue().size()));
        }
    }

    public void setSourceIpTop5(Map<String, List<AssetEventVO>> map) {
        ArrayList<TypeNum> list = new ArrayList<>();
        for (Map.Entry<String, List<AssetEventVO>> entry : map.entrySet()) {
            list.add(new TypeNum(entry.getKey(), entry.getValue().size()));
        }
        List<TypeNum> collect = list.stream().sorted().collect(Collectors.toList());
        if (collect.size() > 5) {
            this.sourceIpTop5 = collect.subList(0, 5);
        } else {
            this.sourceIpTop5 = collect;
        }
    }

    public void setAssetEventTop5(Map<String, List<AssetEventVO>> map) {
        ArrayList<TypeNum> list = new ArrayList<>();
        for (Map.Entry<String, List<AssetEventVO>> entry : map.entrySet()) {
            list.add(new TypeNum(entry.getKey(), entry.getValue().size()));
        }
        List<TypeNum> collect = list.stream().sorted().collect(Collectors.toList());
        if (collect.size() > 5) {
            this.assetEventTop5 = collect.subList(0, 5);
        } else {
            this.assetEventTop5 = collect;
        }
    }

    public void setTargetIpTop5(Map<String, List<AssetEventVO>> map) {
        ArrayList<TypeNum> list = new ArrayList<>();
        for (Map.Entry<String, List<AssetEventVO>> entry : map.entrySet()) {
            list.add(new TypeNum(entry.getKey(), entry.getValue().size()));
        }
        List<TypeNum> collect = list.stream().sorted().collect(Collectors.toList());
        if (collect.size() > 5) {
            this.targetIpTop5 = collect.subList(0, 5);
        } else {
            this.targetIpTop5 = collect;
        }
    }

    public void setDailyEventNumMap(HashMap<Integer, List<TypeNum>> dailyEventNumMap) {
        this.dailyEventNumMap = dailyEventNumMap;
    }

    public void setUndisposedEventList(List<AssetEventVO> undisposedEventList) {
        this.undisposedEventList = undisposedEventList;
    }

    @AllArgsConstructor
    @Getter
    public static class TypeNum implements Comparable<TypeNum> {
        static final Comparator<TypeNum> COMPARATOR =
                comparingInt(TypeNum::getNum);

        protected String name;

        protected Integer num;

        @Override
        public int compareTo(@NotNull TypeNum o) {
            return COMPARATOR.compare(o, this);
        }
    }

}
