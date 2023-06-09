package com.gem.loganalysis.model.dto;

import com.gem.loganalysis.model.dto.asset.VlanDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 11:53
 */
@Data
public class IpSectionDTO {

    @ApiModelProperty(value = "IP区段",required = true)
    @NotNull(message = "IP区段不能为空")
    private List<VlanDTO> vlanList;

    @ApiModelProperty(value = "扫描类型，SIMPLE,ALL",required = true)
    @NotNull(message = "扫描类型不能为空,SIMPLE,ALL")
    private String scannerType;

}
