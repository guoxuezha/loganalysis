package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户权限 VO ")
public class UserAuthorityVO {
    @ApiModelProperty("用户具备的角色列表")
    private List<String> roleIdList;

    @ApiModelProperty("用户具备的菜单列表")
    private List<String> menuList;
}