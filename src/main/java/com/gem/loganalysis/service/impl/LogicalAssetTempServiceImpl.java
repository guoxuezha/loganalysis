package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.LogicalAssetTempMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.IpDTO;
import com.gem.loganalysis.model.dto.asset.LogicalAssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;
import com.gem.loganalysis.model.vo.LogicalScannerVO;
import com.gem.loganalysis.model.vo.PhysicalScannerVO;
import com.gem.loganalysis.model.vo.asset.LogicalAssetScannerRespVO;
import com.gem.loganalysis.model.vo.asset.PhysicalAssetScannerRespVO;
import com.gem.loganalysis.service.ILogicalAssetTempService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 逻辑资产扫描结果 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Service
public class LogicalAssetTempServiceImpl extends ServiceImpl<LogicalAssetTempMapper, LogicalAssetTemp> implements ILogicalAssetTempService {

    @Resource
    private LogicalAssetTempMapper logicalAssetTempMapper;


    @Override
    public LogicalScannerVO getLogicalAssetList(LogicalAssetQueryDTO dto) {
        LogicalScannerVO result = new LogicalScannerVO();
        List<LogicalAssetScannerRespVO> logicalAssetList = logicalAssetTempMapper.getLogicalAssetList(dto);
        result.setUnmanagedList(logicalAssetList.stream()
                .filter(e->e.getExistsInAssetTable()==1) .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(asset ->
                                asset.getIpAddress() + asset.getEnablePort()))),
                        ArrayList::new
                )));//未纳管
        result.setManagedList(logicalAssetList.stream()
                .filter(e->e.getExistsInAssetTable()==0)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(asset ->
                                asset.getIpAddress() + asset.getEnablePort()))),
                        ArrayList::new
                )));//已纳管
        return result;
    }

    @Override
    public Integer getUnmanagedCount() {
        return logicalAssetTempMapper.getUnmanagedCount();
    }

    @Override
    public List<LogicalAssetScannerRespVO> getUnmanagedList(IpDTO data) {
        return logicalAssetTempMapper.getUnmanagedList(data);
    }
}
