package com.gem.loganalysis.snmp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

/**
 * 内存与硬盘占用情况
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/29 15:59
 */
@Data
@NoArgsConstructor
public class MemoryAndDiskInfo {

    /**
     * 设备编号
     */
    private String deviceId;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 总大小
     */
    private Integer totalSize;

    /**
     * 已用大小
     */
    private Integer usedSize;

    /**
     * 占用率
     */
    private String usedRate;

    public MemoryAndDiskInfo(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
        if (this.usedSize != null && this.usedRate == null) {
            this.usedRate = new DecimalFormat("#0.00").format(this.usedSize * 100 / (double) totalSize) + "%";
        }
    }

    public void setUsedSize(Integer usedSize) {
        this.usedSize = usedSize;
        if (this.totalSize != null && this.totalSize != 0 && this.usedRate == null) {
            this.usedRate = new DecimalFormat("#0.00").format(this.usedSize * 100 / (double) totalSize) + "%";
        }
    }

}
