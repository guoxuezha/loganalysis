package com.gem.loganalysis.model.dto.edit;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 黑名单
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Data
@ApiModel("安全管理 - 黑白名单创建/更新 DTO ")
public class BlackWhiteListDTO {

    @ApiModelProperty(value = "组织机构唯一编码")
    private String orgId;

    @ApiModelProperty(value = "关联资产唯一编码（*表示当前组织机构【含下属】的所有资产）",required = true)
    @NotBlank(message = "请传入关联资产唯一编码,*表示当前组织机构【含下属】的所有资产")
    private String assetId;

    @ApiModelProperty(value = "列入黑名单的IP",required = true)
    @NotBlank(message = "请传入列入黑名单的IP")
    private String ipAddress;

    @ApiModelProperty(value = "有效时间，为空时表示永久有效,14位格式")
    private String validTime;

}
