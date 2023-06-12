package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/8 22:46
 */
@Data
public class LogFieldMappingDTO {

    @ApiModelProperty(value = "日志解析规则关联关系编码", required = true)
    private String ruleRelaId;

    @ApiModelProperty(value = "属性映射列表")
    private List<FieldMapping> fieldMappingList;

    @Data
    public static class FieldMapping {

        @ApiModelProperty(value = "源端日志属性名称", required = true)
        private String sourceFieldName;

        @ApiModelProperty("源端日志属性描述")
        private String sourceFieldDesc;

        @ApiModelProperty(value = "目标日志属性ID", required = true)
        private String targetFieldId;
    }

}
