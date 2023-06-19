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
import com.gem.loganalysis.model.vo.PhysicalScannerVO;
import com.gem.loganalysis.model.vo.asset.AssetOverviewVO;
import com.gem.loganalysis.model.vo.asset.PhysicalAssetScannerRespVO;
import com.gem.loganalysis.service.IPhysicalAssetTempService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * <p>
 * IP设备扫描结果 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Service
public class PhysicalAssetTempServiceImpl extends ServiceImpl<PhysicalAssetTempMapper, PhysicalAssetTemp> implements IPhysicalAssetTempService {
    @Resource
    private PhysicalAssetTempMapper physicalAssetTempMapper;

    @Override
    public PhysicalScannerVO getPhysicalAssetList(PhysicalAssetQueryDTO dto) {
        dto.setAssetStatus(1);//只查存在的
        PhysicalScannerVO result = new PhysicalScannerVO();
        List<PhysicalAssetScannerRespVO> physicalAssetList = physicalAssetTempMapper.getPhysicalAssetList(dto);
        result.setUnmanagedList(physicalAssetList.stream()
                .filter(e->e.getExistsInAssetTable()==1).collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(PhysicalAssetScannerRespVO::getIpAddress))),
                        ArrayList::new
                )));//未纳管
        result.setManagedList(physicalAssetList.stream()
                .filter(e->e.getExistsInAssetTable()==0) .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(PhysicalAssetScannerRespVO::getIpAddress))),
                        ArrayList::new
                )));//已纳管
        return result;
    }

    @Override
    public List<PhysicalAssetTemp> getNewAssetScanList() {
        return physicalAssetTempMapper.getNewAssetScanList();
    }

    @Override
    public Integer getUnmanagedCount() {
        return physicalAssetTempMapper.getUnmanagedCount();
    }
}
