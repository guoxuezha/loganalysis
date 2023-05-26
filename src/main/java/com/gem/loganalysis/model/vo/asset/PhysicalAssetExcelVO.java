package com.gem.loganalysis.model.vo.asset;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("物理资产EXCEL导出 Response VO")
@Data
public class PhysicalAssetExcelVO {

    @ExcelProperty(value = "*资产名称")
    private String assetName;

    @ExcelProperty(value = "*资产类型")
    private String assetType;

    @ExcelProperty(value = "*IP地址")
    private String ipAddress;

    @ExcelProperty(value = "品牌")
    private String assetBrand;

    @ExcelProperty(value = "型号")
    private String assetModel;

    @ExcelProperty(value = "规格")
    private String assetSpec;

    @ExcelProperty(value = "操作系统版本")
    private String osVersion;

    @ExcelProperty(value = "固件版本")
    private String firmwareVersion;

    @ExcelProperty(value = "软件版本")
    private String softwareVersion;

    @ExcelProperty(value = "MIB版本")
    private String mibVersion;

    @ExcelProperty(value = "服务端口")
    private Integer servicePort;

    @ExcelProperty(value = "*网管端口")
    private Integer nmPort;

    @ExcelProperty(value = "*网管协议")
    private String nmProcotol;

    @ExcelProperty(value = "*网管账号")
    private String nmAccount;

    @ExcelProperty(value = "*网管密码")
    private String nmPassword;

    @ExcelProperty(value = "资产管理人")
    private String assetManager;

    @ExcelProperty(value = "资产标签")
    private String assetTag;

    @ExcelProperty(value = "资产描述")
    private String assetDesc;

}
