package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.LogicalAssetTempMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.asset.LogicalAssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;
import com.gem.loganalysis.service.ILogicalAssetTempService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * <p>
 * 逻辑资产扫描结果 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Service
@DS("slave")
public class LogicalAssetTempServiceImpl extends ServiceImpl<LogicalAssetTempMapper, LogicalAssetTemp> implements ILogicalAssetTempService {

    @Override
    public Page<LogicalAssetTemp> getLogicalAssetPage(PageRequest<LogicalAssetQueryDTO> dto) {
        Page<LogicalAssetTemp> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LogicalAssetQueryDTO data = dto.getData();
        LambdaQueryWrapperX<LogicalAssetTemp> wrapper = new LambdaQueryWrapperX<LogicalAssetTemp>()
                .eqIfPresent(LogicalAssetTemp::getIpAddress, data.getIpAddress())
                .eqIfPresent(LogicalAssetTemp::getEnablePort, data.getEnablePort())
                .eqIfPresent(LogicalAssetTemp::getAssetOrg, data.getAssetOrg())
                .eqIfPresent(LogicalAssetTemp::getAssetType, data.getAssetType())
                .betweenIfPresent(LogicalAssetTemp::getScanTime,data.getBeginScanTime(), data.getEndScanTime())
                .orderByDesc(LogicalAssetTemp::getScanTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<LogicalAssetTemp> getLogicalAssetList(LogicalAssetQueryDTO dto) {
        LambdaQueryWrapperX<LogicalAssetTemp> wrapper = new LambdaQueryWrapperX<LogicalAssetTemp>()
                .eqIfPresent(LogicalAssetTemp::getIpAddress, dto.getIpAddress())
                .eqIfPresent(LogicalAssetTemp::getEnablePort, dto.getEnablePort())
                .eqIfPresent(LogicalAssetTemp::getAssetOrg, dto.getAssetOrg())
                .eqIfPresent(LogicalAssetTemp::getAssetType, dto.getAssetType())
                .betweenIfPresent(LogicalAssetTemp::getScanTime,dto.getBeginScanTime(), dto.getEndScanTime())
                .orderByDesc(LogicalAssetTemp::getScanTime);
        return this.list(wrapper);
    }
}
