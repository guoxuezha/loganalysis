package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.asset.OrgVlanDTO;
import com.gem.loganalysis.model.dto.asset.OrgVlanQueryDTO;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.model.entity.OrgVlan;
import com.gem.loganalysis.model.vo.asset.OrgVlanRespVO;

import java.util.List;

/**
 * <p>
 * Vlan配置 服务类
 * </p>
 *
 * @author czw
 * @since 2023-05-10
 */
public interface IOrgVlanService extends IService<OrgVlan> {

    Page<OrgVlanRespVO> getVlanPage(PageRequest<OrgVlanQueryDTO> dto);

    boolean editVlan(OrgVlanDTO dto);

    List<OrgVlanRespVO> getVlanList(OrgVlanQueryDTO dto);
}
