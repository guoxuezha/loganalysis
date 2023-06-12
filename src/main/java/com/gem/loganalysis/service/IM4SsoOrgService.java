package com.gem.loganalysis.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.dto.edit.OrgDTO;
import com.gem.loganalysis.model.dto.query.OrgQueryDTO;
import com.gem.loganalysis.model.entity.M4SsoOrg;
import com.gem.loganalysis.model.vo.OrgRespVO;
import com.gem.loganalysis.model.vo.TreeRespVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组织机构 服务类
 * </p>
 *
 * @author czw
 * @since 2023-05-23
 */
public interface IM4SsoOrgService extends IService<M4SsoOrg> {



    /**
     * 初始化组织机构缓存
     */
    void initLocalCache();

    /**
     * 组织机构ID转NAMe
     */
    String changeOrgName(String orgId);

    /**
     * 缓存中获取组织机构MAP(非树形)
     */
    Map<String, M4SsoOrg> getListByCache();

    /**
     * 新增/编辑组织机构
     * @param dto
     * @return
     */
    boolean editOrg(OrgDTO dto);

    /**
     * 新增/编辑组织机构
     * @param dto
     * @return
     */
    List<OrgRespVO> OrgList(OrgQueryDTO dto);

    List<TreeRespVO>  OrgSimpleList();
}
