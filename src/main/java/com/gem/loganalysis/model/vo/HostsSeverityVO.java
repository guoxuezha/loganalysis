package com.gem.loganalysis.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostsSeverityVO {
    private double severity;
    private String gvmAssetId;
    private String modificationTime;
    private String ip;
    private String comment;
    private String os;
}
