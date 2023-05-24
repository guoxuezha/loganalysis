package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/24 15:59
 */
@Data
public class UserQueryDTO {

    @ApiModelProperty("昵称")
    private String account;

    @ApiModelProperty("组织机构ID")
    private String orgId;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("邮箱")
    private String email;

}
