package com.gem.loganalysis.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@ApiModel(" 导入 Response VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportRespVO {

    @ApiModelProperty(value = "创建成功的数组", required = true)
    private List<String> successNames;

    @ApiModelProperty(value = "创建失败的数组", required = true, notes = "key 为用户名，value 为失败原因")
    private Map<String, String> failNames;
}
