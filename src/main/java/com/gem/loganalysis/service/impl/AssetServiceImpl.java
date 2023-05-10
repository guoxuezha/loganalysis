package com.gem.loganalysis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.AssetMapper;
import com.gem.loganalysis.mapper.BlackWhiteListMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.BlackWhiteList;
import com.gem.loganalysis.service.IAssetService;
import com.gem.loganalysis.service.IBlackWhiteListService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

/**
 * 安全管理资产 Service 实现类
 *
 * @author czw
 */
@Service
@Validated
@DS("slave")
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements IAssetService{

    @Resource
    private AssetMapper assetMapper;


}
