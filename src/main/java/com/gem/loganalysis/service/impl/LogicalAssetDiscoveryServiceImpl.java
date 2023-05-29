package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.LogicalAssetDiscoveryMapper;
import com.gem.loganalysis.mapper.LogicalAssetTempMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.asset.LogicalAssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.LogicalAssetDiscoveryRule;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.service.ILogicalAssetDiscoveryService;
import com.gem.loganalysis.service.ILogicalAssetTempService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 逻辑资产发现规则 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Service
public class LogicalAssetDiscoveryServiceImpl extends ServiceImpl<LogicalAssetDiscoveryMapper, LogicalAssetDiscoveryRule> implements ILogicalAssetDiscoveryService {



}
