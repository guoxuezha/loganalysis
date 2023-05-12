package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.PhysicalAssetTempMapper;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;
import com.gem.loganalysis.service.IPhysicalAssetTempService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * IP设备扫描结果 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Service
@DS("slave")
public class PhysicalAssetTempServiceImpl extends ServiceImpl<PhysicalAssetTempMapper, PhysicalAssetTemp> implements IPhysicalAssetTempService {

}
