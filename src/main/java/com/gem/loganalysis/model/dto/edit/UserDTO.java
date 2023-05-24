package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/24 16:01
 */
@Data
public class UserDTO {

    /**
     * 用户唯一编码
     */
    @ApiModelProperty("用户唯一编码(为空时修改,否则新增)")
    private String userId;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String account;

    /**
     * 账号
     */
    @ApiModelProperty(value = "用户账号", required = true)
    private String userName;

    /**
     * 用户密码(不可逆加密后的Base64)
     */
    @ApiModelProperty(value = "用户密码", required = true)
    private String password;

    /**
     * 所属组织编码
     */
    @ApiModelProperty("所属组织编码")
    private String orgId;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private Integer userSex;

    /**
     * 注册手机号码
     */
    @ApiModelProperty("注册手机号码")
    private String mobile;

    /**
     * 注册电子邮箱
     */
    @ApiModelProperty("注册电子邮箱")
    private String email;

}
