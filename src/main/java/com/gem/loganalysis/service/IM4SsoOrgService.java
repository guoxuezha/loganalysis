package com.gem.loganalysis.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.dto.edit.OrgDTO;
import com.gem.loganalysis.model.dto.query.OrgQueryDTO;
import com.gem.loganalysis.model.entity.M4SsoOrg;
import com.gem.loganalysis.model.vo.OrgRespVO;
import com.gem.loganalysis.model.vo.TreeRespVO;

import java.util.List;

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
