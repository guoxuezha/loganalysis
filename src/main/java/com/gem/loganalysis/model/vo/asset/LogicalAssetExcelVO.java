package com.gem.loganalysis.model.vo.asset;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("逻辑资产EXCEL导出 Response VO")
@Data
public class LogicalAssetExcelVO {

    @ExcelProperty(value = "*资产名称")
    @ColumnWidth(value = 20)
    private String assetName;

    @ExcelProperty(value = "*资产类型")
    @ColumnWidth(value = 18)
    private String assetType;

    @ExcelProperty(value = "*IP地址")
    @ColumnWidth(value = 20)
    private String ipAddress;

    @ExcelProperty(value = "服务端口")
    @ColumnWidth(value = 12)
    private String servicePort;

    @ExcelProperty(value = "品牌")
    @ColumnWidth(value = 20)
    private String assetBrand;

    @ExcelProperty(value = "资产管理人")
    @ColumnWidth(value = 20)
    private String assetManager;

    @ExcelProperty(value = "资产标签")
    @ColumnWidth(value = 20)
    private String assetTag;

    @ExcelProperty(value = "资产描述")
    @ColumnWidth(value = 40)
    private String assetDesc;

}
