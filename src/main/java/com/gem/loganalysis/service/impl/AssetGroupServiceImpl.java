package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.AssetGroupMapper;
import com.gem.loganalysis.mapper.AssetMapper;
import com.gem.loganalysis.model.entity.AssetGroup;
import com.gem.loganalysis.service.IAssetGroupService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 安全管理资产分组 Service 实现类
 *
 * @author czw
 */
@Service
@Validated
@DS("slave")
public class AssetGroupServiceImpl extends ServiceImpl<AssetGroupMapper, AssetGroup> implements IAssetGroupService {

    @Resource
    private AssetGroupMapper assetGroupMapper;



}
