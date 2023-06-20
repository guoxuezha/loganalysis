package com.gem.loganalysis.enmu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//资产类型枚举类
public enum ScannerType {

    AUTOMATIC("自动扫描",0),
    MANUAL("手动扫描",1);

    private final String name;
    private final Integer id;

}
