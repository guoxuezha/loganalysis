package com.gem.loganalysis.model.vo;

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
@ApiModel("物理扫描结果 VO ")
public class PhysicalScannerVO {
    @ApiModelProperty("纳管(已存在)集合")
    private List<PhysicalAssetScannerRespVO> managedList;

    @ApiModelProperty("未纳管(不存在)集合")
    private List<PhysicalAssetScannerRespVO> unmanagedList;
}