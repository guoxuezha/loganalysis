package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.PhysicalAssetTempMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.asset.LogicalAssetQueryDTO;
import com.gem.loganalysis.model.dto.asset.PhysicalAssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
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


    @Override
    public Page<PhysicalAssetTemp> getPhysicalAssetPage(PageRequest<PhysicalAssetQueryDTO> dto) {
        Page<PhysicalAssetTemp> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        PhysicalAssetQueryDTO data = dto.getData();
        LambdaQueryWrapperX<PhysicalAssetTemp> wrapper = new LambdaQueryWrapperX<PhysicalAssetTemp>()
                .eqIfPresent(PhysicalAssetTemp::getIpAddress, data.getIpAddress())
                .eqIfPresent(PhysicalAssetTemp::getAssetStatus, data.getAssetStatus())
                .eqIfPresent(PhysicalAssetTemp::getAssetOrg, data.getAssetOrg())
                .betweenIfPresent(PhysicalAssetTemp::getScanTime,data.getBeginScanTime(), data.getEndScanTime())
                .orderByDesc(PhysicalAssetTemp::getScanTime);
        return this.page(page, wrapper);
    }
}
