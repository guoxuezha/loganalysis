package com.gem.loganalysis.enmu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//数据字典枚举类
public enum DictType {
    ASSET_CLASS("资产分类","ASSET_CLASS"),
    ASSET_SECURITY_STATUS("物理资产安全状态","ASSET_SECURITY_STATUS"),
    ASSET_STATUS("资产状态","ASSET_STATUS"),
    PHYSICAL_ASSET_TYPE("物理资产类型","PHYSICAL_ASSET_TYPE"),
    LOGICAL_ASSET_TYPE("逻辑资产类型","LOGICAL_ASSET_TYPE");
    private final String name;
    private final String type;

}
