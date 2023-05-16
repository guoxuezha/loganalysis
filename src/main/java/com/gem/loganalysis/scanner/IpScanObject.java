package com.gem.loganalysis.scanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * IP扫描信息实体
 */
@Data
@Accessors(chain = true)
public class IpScanObject {
     private String ip;
     private Boolean isOpen;
}
