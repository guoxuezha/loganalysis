package com.gem.loganalysis.enmu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//资产类型枚举类
public enum AssetClass {
    LOGICAL("逻辑资产","0"),
    PHYSICAL("物理资产","1");
;
    private final String name;
    private final String id;

}
