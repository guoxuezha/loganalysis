package com.gem.loganalysis.model.vo.asset;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("逻辑资产EXCEL导出 Response VO")
@Data
public class LogicalAssetExcelVO {


    @ExcelProperty(value = "*资产名称")
    private String assetName;

    @ExcelProperty(value = "*资产类型")
    private String assetType;

    @ExcelProperty(value = "*IP地址")
    private String ipAddress;

    @ExcelProperty(value = "服务端口")
    private Integer servicePort;

    @ExcelProperty(value = "品牌")
    private String assetBrand;

    @ExcelProperty(value = "资产管理人")
    private String assetManager;

    @ExcelProperty(value = "资产标签")
    private String assetTag;

    @ExcelProperty(value = "资产描述")
    private String assetDesc;

}
