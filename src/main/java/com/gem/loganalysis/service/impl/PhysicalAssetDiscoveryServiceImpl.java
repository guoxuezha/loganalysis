package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.PhysicalAssetDiscoveryMapper;
import com.gem.loganalysis.mapper.PhysicalAssetTempMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.asset.PhysicalAssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.PhysicalAssetDiscoveryRule;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;
import com.gem.loganalysis.service.IPhysicalAssetDiscoveryService;
import com.gem.loganalysis.service.IPhysicalAssetTempService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 物理资产扫描规则 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Service
public class PhysicalAssetDiscoveryServiceImpl extends ServiceImpl<PhysicalAssetDiscoveryMapper, PhysicalAssetDiscoveryRule> implements IPhysicalAssetDiscoveryService {


}
