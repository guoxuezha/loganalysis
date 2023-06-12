package com.gem.loganalysis.model.vo.asset;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@ApiModel("物理资产EXCEL导出 Response VO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false)
public class PhysicalAssetExcelVO {

    @ExcelProperty(value = "*资产名称")
    @ColumnWidth(value = 20)
    private String assetName;

    @ExcelProperty(value = "*资产型别")
    @ColumnWidth(value = 18)
    private String assetCategory;

    @ExcelProperty(value = "*资产类型")
    @ColumnWidth(value = 18)
    private String typeName;

    @ExcelProperty(value = "*IP地址")
    @ColumnWidth(value = 20)
    private String ipAddress;

    @ExcelProperty(value = "品牌")
    @ColumnWidth(value = 20)
    private String assetBrand;

    @ExcelProperty(value = "型号")
    @ColumnWidth(value = 20)
    private String assetModel;

    @ExcelProperty(value = "规格")
    @ColumnWidth(value = 20)
    private String assetSpec;

    @ExcelProperty(value = "操作系统版本")
    @ColumnWidth(value = 20)
    private String osVersion;

    @ExcelProperty(value = "固件版本")
    @ColumnWidth(value = 20)
    private String firmwareVersion;

    @ExcelProperty(value = "软件版本")
    @ColumnWidth(value = 20)
    private String softwareVersion;

    @ExcelProperty(value = "MIB版本")
    @ColumnWidth(value = 20)
    private String mibVersion;

    @ExcelProperty(value = "服务端口")
    @ColumnWidth(value = 10)
    private String servicePort;

    @ExcelProperty(value = "资产管理人")
    @ColumnWidth(value = 18)
    private String assetManagerName;

    @ExcelProperty(value = "*资产部门")
    @ColumnWidth(value = 20)
    private String assetOrgName;

    @ExcelProperty(value = "资产分组")
    @ColumnWidth(value = 20)
    private String assetGroupName;

    @ExcelProperty(value = "资产标签")
    @ColumnWidth(value = 20)
    private String assetTag;

    @ExcelProperty(value = "资产描述")
    @ColumnWidth(value = 35)
    private String assetDesc;

}
