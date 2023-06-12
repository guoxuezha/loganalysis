package com.gem.loganalysis.model.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.model.entity.M4SsoUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/25 9:56
 */
@Data
@NoArgsConstructor
public class UserInfoVO {

    @ApiModelProperty("用户唯一编码")
    private String userId;

    @ApiModelProperty("昵称")
    private String account;

    @ApiModelProperty("账号")
    private String userName;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("所属组织编码")
    private String orgId;

    @ApiModelProperty("组织(部门名称)")
    private String orgName;

    @ApiModelProperty("性别")
    private Integer userSex;

    @ApiModelProperty("注册手机号码")
    private String mobile;

    @ApiModelProperty("注册电子邮箱")
    private String email;

    @ApiModelProperty("用户账号临时锁定时间")
    private String lockTime;

    @ApiModelProperty("可用状态")
    private Integer enableStatus;

    @ApiModelProperty("用户具备的角色列表")
    private List<String> roleIdList;

    public UserInfoVO(M4SsoUser record) {
        BeanUtil.copyProperties(record, this);
        this.roleIdList = new ArrayList<>();
        ArrayList<HashMap<String, String>> dataSet = new DAO().getDataSet(BaseConstant.DEFAULT_POOL_NAME, "SELECT ROLE_ID FROM m4_sso_user_role WHERE USER_ID = '" + this.userId + "' AND PACKAGE_ID = '01'", 0, 0);
        if (CollUtil.isNotEmpty(dataSet)) {
            for (HashMap<String, String> map : dataSet) {
                this.roleIdList.add(map.get("ROLE_ID"));
            }
        }
    }

}
