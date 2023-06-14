package com.gem.loganalysis.model.vo;

import com.gem.loganalysis.model.vo.asset.LogicalAssetScannerRespVO;
import com.gem.loganalysis.model.vo.asset.PhysicalAssetScannerRespVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("逻辑扫描结果 VO ")
public class LogicalScannerVO {
    @ApiModelProperty("纳管(已存在)集合")
    private List<LogicalAssetScannerRespVO> managedList;

    @ApiModelProperty("未纳管(不存在)集合")
    private List<LogicalAssetScannerRespVO> unmanagedList;
}