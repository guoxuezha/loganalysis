package com.gem.loganalysis.model.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gem.loganalysis.model.dto.edit.UserDTO;
import com.gem.utils.crypto.MD5;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m4_sso_user")
@NoArgsConstructor
public class M4SsoUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一编码
     */
    @TableId("USER_ID")
    @ApiModelProperty("用户唯一编码")
    private String userId;

    /**
     * 用户账号(昵称)
     */
    @TableField("ACCOUNT")
    private String account;

    /**
     * 用户名称
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 用户密码(不可逆加密后的Base64)
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 所属组织编码
     */
    @TableField("ORG_ID")
    private String orgId;

    /**
     * 性别
     */
    @TableField("USER_SEX")
    private Integer userSex;

    /**
     * 注册手机号码
     */
    @TableField("MOBILE")
    private String mobile;

    /**
     * 注册电子邮箱
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 用户账号临时锁定时间
     */
    @TableField("LOCK_TIME")
    private String lockTime;

    /**
     * 可用状态
     */
    @TableField("ENABLE_STATUS")
    private Integer enableStatus;

    public M4SsoUser(UserDTO dto) {
        BeanUtil.copyProperties(dto, this);
        this.password = MD5.encryptPwd(this.userName, this.password, "GEM#SHA512");
        this.enableStatus = 1;
    }

    public ArrayList<Object[]> generateLoginInfo() {
        ArrayList<Object[]> arr = new ArrayList<>();
        if (StrUtil.isNotEmpty(this.userName)) {
            arr.add(new Object[]{this.userName, this.userId});
        }
        // 因登录验证账号密码处对密码的加密匹配结合了loginId,所以实际上不支持多账号登录,因为密码只存储了一份
        /*if (StrUtil.isNotEmpty(this.mobile)) {
            arr.add(new Object[]{this.mobile, this.userId});
        }
        if (StrUtil.isNotEmpty(this.email)) {
            arr.add(new Object[]{this.email, this.userId});
        }*/
        return arr;
    }

}
