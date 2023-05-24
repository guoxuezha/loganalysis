package com.gem.loganalysis.model.dto.edit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("系统模块 - 组织部门创建/更新 DTO ")
@Data
public class OrgDTO {


    @ApiModelProperty(value = "组织机构唯一编码(新增不传,修改传)")
    private String orgId;

    @ApiModelProperty(value = "组织机构名称（惯用名）,必传",required = true)
    @NotBlank(message = "组织机构名称(惯用名)不能为空")
    private String orgName;

    @ApiModelProperty(value = "组织机构全称（法定名）,必传",required = true)
    @NotBlank(message = "组织机构全称（法定名）不能为空")
    private String orgFullname;

    @ApiModelProperty(value = "组织机构简称,必传",required = true)
    @NotBlank(message = "组织机构简称不能为空")
    private String orgShortname;

    @ApiModelProperty(value = "统一社会信用编码")
    private String uscc;

    @ApiModelProperty(value = "组织机构类型")
    private String orgType;

    @ApiModelProperty(value = "组织机构注册地址")
    private String orgRegAddress;

    @ApiModelProperty(value = "组织机构所属行政区划")
    private String orgRgn;

    @ApiModelProperty(value = "组织机构实际地址")
    private String orgRealAddress;

    @ApiModelProperty(value = "和所属组织之间的关系")
    private String parentOrgRela;

    @ApiModelProperty(value = "所属组织，层级结构")
    private String parentOrg;

    @ApiModelProperty(value = "在源端系统中的组织机构编码")
    private String sourceOrgId;

    @ApiModelProperty(value = "地理坐标")
    private String coordinate;

    @ApiModelProperty(value = "拓展信息")
    private String orgExtend;

    @ApiModelProperty(value = "组织负责人")
    private String director;

    @ApiModelProperty(value = "组织负责人电话")
    private String directorTel;

    @ApiModelProperty(value = "组织联络人")
    private String liaison;

    @ApiModelProperty(value = "组织联络人信息")
    private String contactInfo;

    @ApiModelProperty(value = "数据责任人")
    private String dataDirector;

    @ApiModelProperty(value = "数据责任人联系方式")
    private String dataDirectorTel;

    @ApiModelProperty(value = "上报责任人")
    private String reportDirector;

    @ApiModelProperty(value = "上报责任人联系方式")
    private String reportDirectorTel;

    @ApiModelProperty(value = "数据来源编码")
    private String dataSource;

    @ApiModelProperty(value = "数据更新时间(后端更新)")
    private String updateTime;

    @ApiModelProperty(value = "数据更新任务编号")
    private String taskId;

}
