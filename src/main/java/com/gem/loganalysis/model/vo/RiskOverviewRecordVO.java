package com.gem.loganalysis.model.vo;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

import static java.util.Comparator.comparing;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/23 10:47
 */
@Data
public class RiskOverviewRecordVO implements Comparable<RiskOverviewRecordVO> {

    private static final Comparator<RiskOverviewRecordVO> COMPARATOR =
            comparing(RiskOverviewRecordVO::getBeginTime);

    private String assetId;

    private String assetName;

    private String ipAddress;

    private String scanTime;

    /**
     * 处理状态
     * （0待处理 / 1处理中 / 2处理完成 / 3忽略）
     */
    private Integer handleStatus;

    private String eventOrigin;

    private String originId;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件级别
     */
    private String eventClass;

    private String sourceIp;

    private String beginTime;

    private String endTime;

    private String eventMessage;

    @Override
    public int compareTo(@NotNull RiskOverviewRecordVO o) {
        return COMPARATOR.compare(this, o);
    }
}
