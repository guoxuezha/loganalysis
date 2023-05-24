package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 业务包信息
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m4_sso_package")
@NoArgsConstructor
public class M4SsoPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务包编码
     */
    @TableId("PACKAGE_ID")
    private String packageId;

    /**
     * 业务包名称
     */
    @TableField("PACKAGE_NAME")
    private String packageName;

    /**
     * 业务包LOGO
     */
    @TableField("PACKAGE_LOGO_URL")
    private String packageLogoUrl;

    /**
     * 业务包标题
     */
    @TableField("PACKAGE_CAPTION")
    private String packageCaption;

    /**
     * 业务包图例
     */
    @TableField("PACKAGE_ICON_NAME")
    private String packageIconName;

    /**
     * 业务包描述信息(Base64编码的富文本)
     */
    @TableField("PACKAGE_DESC")
    private String packageDesc;

    /**
     * 业务包可用状态
     */
    @TableField("ENABLE_STATUS")
    private Integer enableStatus;


}
