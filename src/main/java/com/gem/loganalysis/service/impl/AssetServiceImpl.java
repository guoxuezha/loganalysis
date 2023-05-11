package com.gem.loganalysis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.mapper.AssetMapper;
import com.gem.loganalysis.mapper.BlackWhiteListMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.BlackWhiteList;
import com.gem.loganalysis.service.IAssetService;
import com.gem.loganalysis.service.IBlackWhiteListService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.regex.Pattern;

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


    @Override
    public Result<String> editAsset(AssetDTO dto) {
        if(dto.getIpAddress()!=null&& !dto.getIpAddress().trim().equals("")){
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            if(!Pattern.matches(regex,dto.getIpAddress())){
                //效验IP地址格式是否正确
                return Result.failed("请输入正确的IP地址格式");
            }
        }
        return this.saveOrUpdate(AssetConvert.INSTANCE.convert(dto))?Result.ok("操作成功!"):Result.failed("操作失败!");
    }

    @Override
    public Page<Asset> getPageList(PageRequest<AssetQueryDTO> dto) {
        Page<Asset> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        AssetQueryDTO data = dto.getData();
        LambdaQueryWrapperX<Asset> wrapper = new LambdaQueryWrapperX<Asset>()
                .likeIfPresent(Asset::getAssetName, data.getAssetName())
                .eqIfPresent(Asset::getAssetClass, data.getAssetClass())
                .eqIfPresent(Asset::getAssetType, data.getAssetType())
                .eqIfPresent(Asset::getIpAddress, data.getIpAddress())
                .eqIfPresent(Asset::getAssetManager, data.getAssetManager())
                .eqIfPresent(Asset::getAssetGroupId, data.getAssetGroupId())
                .eqIfPresent(Asset::getAssetOrg, data.getAssetOrg())
                .eqIfPresent(Asset::getAssetStatus, data.getAssetStatus())
                .orderByDesc(Asset::getUpdateTime);
        return this.page(page, wrapper);
    }
}
