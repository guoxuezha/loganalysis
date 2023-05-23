package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组织机构
 * </p>
 *
 * @author czw
 * @since 2023-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class M4SsoOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织机构唯一编码
     */
    @TableId(value = "ORG_ID", type = IdType.ASSIGN_ID)
    private String orgId;

    /**
     * 组织机构名称（惯用名）
     */
    @TableField("ORG_NAME")
    private String orgName;

    /**
     * 组织机构全称（法定名）
     */
    @TableField("ORG_FULLNAME")
    private String orgFullname;

    /**
     * 组织机构简称
     */
    @TableField("ORG_SHORTNAME")
    private String orgShortname;

    /**
     * 统一社会信用编码
     */
    @TableField("USCC")
    private String uscc;

    /**
     * 组织机构类型
     */
    @TableField("ORG_TYPE")
    private String orgType;

    /**
     * 组织机构注册地址
     */
    @TableField("ORG_REG_ADDRESS")
    private String orgRegAddress;

    /**
     * 组织机构所属行政区划
     */
    @TableField("ORG_RGN")
    private String orgRgn;

    /**
     * 组织机构实际地址
     */
    @TableField("ORG_REAL_ADDRESS")
    private String orgRealAddress;

    /**
     * 和所属组织之间的关系
     */
    @TableField("PARENT_ORG_RELA")
    private String parentOrgRela;

    /**
     * 所属组织
     */
    @TableField("PARENT_ORG")
    private String parentOrg;

    /**
     * 在源端系统中的组织机构编码
     */
    @TableField("SOURCE_ORG_ID")
    private String sourceOrgId;

    /**
     * 地理坐标
     */
    @TableField("COORDINATE")
    private String coordinate;

    /**
     * 拓展信息
     */
    @TableField("ORG_EXTEND")
    private String orgExtend;

    /**
     * 组织负责人
     */
    @TableField("DIRECTOR")
    private String director;

    /**
     * 组织负责人电话
     */
    @TableField("DIRECTOR_TEL")
    private String directorTel;

    /**
     * 组织联络人
     */
    @TableField("LIAISON")
    private String liaison;

    /**
     * 组织联络人信息
     */
    @TableField("CONTACT_INFO")
    private String contactInfo;

    /**
     * 数据责任人
     */
    @TableField("DATA_DIRECTOR")
    private String dataDirector;

    /**
     * 数据责任人联系方式
     */
    @TableField("DATA_DIRECTOR_TEL")
    private String dataDirectorTel;

    /**
     * 上报责任人
     */
    @TableField("REPORT_DIRECTOR")
    private String reportDirector;

    /**
     * 上报责任人联系方式
     */
    @TableField("REPORT_DIRECTOR_TEL")
    private String reportDirectorTel;

    /**
     * 数据来源编码
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 数据更新时间
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 数据更新任务编号
     */
    @TableField("TASK_ID")
    private String taskId;

    /**
     * 组织状态，0无效，1有效
     */
    @TableField("ORG_STATUS")
    @TableLogic
    private String orgStatus;


}
