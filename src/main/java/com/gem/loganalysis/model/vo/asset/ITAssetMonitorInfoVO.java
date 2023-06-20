package com.gem.loganalysis.model.vo.asset;

import cn.hutool.core.util.StrUtil;
import com.gem.loganalysis.model.bo.SNMPCollectInfo;
import com.gem.loganalysis.snmpmonitor.CommonOID;
import com.gem.loganalysis.snmpmonitor.SNMPMonitorServer;
import com.gem.loganalysis.snmpmonitor.SNMPMonitorThread;
import com.gem.loganalysis.snmpmonitor.SNMPMonitorThreadPool;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;

import static java.util.Comparator.comparingInt;

/**
 * IT设备通过SNMP监听获取到的信息
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/19 23:52
 */
@Data
@NoArgsConstructor
public class ITAssetMonitorInfoVO implements Comparable<ITAssetMonitorInfoVO> {

    private static final Comparator<ITAssetMonitorInfoVO> COMPARATOR = comparingInt(ITAssetMonitorInfoVO::getLoad);

    @ApiModelProperty("资产名称")
    private String assetName;

    @ApiModelProperty("IP地址")
    private String ipAddress;

    @ApiModelProperty("设备类型")
    private String assetType;

    @ApiModelProperty("所属部门")
    private String deptName;

    @ApiModelProperty("CPU")
    private String cpu;

    @ApiModelProperty("内存")
    private String memory;

    @ApiModelProperty("磁盘")
    private String disk;

    @ApiModelProperty("网络吞吐")
    private String mbps;

    @ApiModelProperty("综合负载")
    private Integer load;

    public ITAssetMonitorInfoVO(HashMap<String, String> map) {
        assetName = map.get("ASSET_NAME");
        ipAddress = map.get("IP_ADDRESS");
        assetType = map.get("TEXT");
        deptName = map.get("GROUP_NAME");
        String assetId = map.get("ASSET_ID");

        SNMPMonitorServer instance = SNMPMonitorServer.getInstance();
        SNMPMonitorThreadPool threadPool = instance.getThreadPool();
        SNMPMonitorThread thread = threadPool.getThread(assetId);
        // CPU使用率
        CommonOID c1 = thread.getMeasureValue("空闲CPU百分比");
        if (c1 != null) {
            String v0 = c1.getValue();
            cpu = 100 - Integer.parseInt(v0) + "%";
        }

        // 内存使用率
        CommonOID c2 = thread.getMeasureValue("物理内存使用量");
        CommonOID c3 = thread.getMeasureValue("物理内存总容量");
        if (c2 != null && c3 != null) {
            String v1 = c2.getValue();
            String v2 = c3.getValue();
            int i = Integer.parseInt(v1) * 100 / Integer.parseInt(v2);
            memory = i + "%";
        }

        // 磁盘使用率
        CommonOID c4 = thread.getMeasureValue("磁盘使用量");
        CommonOID c5 = thread.getMeasureValue("磁盘总容量");
        if (c4 != null && c5 != null) {
            String v3 = c4.getValue();
            String v4 = c5.getValue();
            int i = Integer.parseInt(v3) * 100 / Integer.parseInt(v4);
            disk = i + "%";
        }

        // 网络吞吐(带宽)
        Double inAndOutRate = SNMPCollectInfo.getInstance().getInAndOutRate(assetId);
        if (inAndOutRate < 1024) {
            mbps = String.format("%.1f", inAndOutRate) + "B/s";
        } else if (inAndOutRate < 1024 * 1024) {
            mbps = String.format("%.1f", (inAndOutRate / 1024)) + "KB/s";
        } else {
            mbps = String.format("%.1f", (inAndOutRate / (1024 * 1024))) + "MB/s";
        }
        setLoad();
    }

    /**
     * 设置综合负载,计算方式为
     * CPU、内存、磁盘三者使用百分比之和
     */
    private void setLoad() {
        load = 0;
        int divisor = 0;
        if (StrUtil.isNotEmpty(cpu)) {
            load += Integer.parseInt(cpu.split("%")[0]);
            divisor++;
        }
        if (StrUtil.isNotEmpty(memory)) {
            load += Integer.parseInt(memory.split("%")[0]);
            divisor++;
        }
        if (StrUtil.isNotEmpty(disk)) {
            load += Integer.parseInt(disk.split("%")[0]);
            divisor++;
        }
        if (divisor != 0) {
            load = load / divisor;
        }
    }

    @Override
    public int compareTo(@NotNull ITAssetMonitorInfoVO o) {
        return COMPARATOR.compare(o, this);
    }
}
