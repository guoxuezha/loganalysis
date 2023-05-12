package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.asset.LogicalAssetQueryDTO;
import com.gem.loganalysis.model.dto.asset.PhysicalAssetQueryDTO;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;

/**
 * <p>
 * IP设备扫描结果 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
public interface IPhysicalAssetTempService extends IService<PhysicalAssetTemp> {


    Page<PhysicalAssetTemp> getPhysicalAssetPage(PageRequest<PhysicalAssetQueryDTO> dto);
}
