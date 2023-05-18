package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.enmu.DictType;
import com.gem.loganalysis.mapper.AssetMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.AssetGroup;
import com.gem.loganalysis.model.vo.asset.AssetAccountRespVO;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import com.gem.loganalysis.service.IAssetGroupService;
import com.gem.loganalysis.service.IAssetService;
import com.gem.loganalysis.service.IDictDataService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.regex.Pattern;

/**
 * 安全管理资产 Service 实现类
 *
 * @author czw
 */
@Service
@Validated
@DS("slave")
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements IAssetService {

    @Resource
    private AssetMapper assetMapper;
    @Resource
    private IDictDataService dictDataService;
    @Resource
    private IAssetGroupService assetGroupService;

    @Override
    public Result<String> editAsset(AssetDTO dto) {
        if (dto.getIpAddress() != null && !dto.getIpAddress().trim().equals("")) {
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            if (!Pattern.matches(regex, dto.getIpAddress())) {
                //效验IP地址格式是否正确
                return Result.failed("请输入正确的IP地址格式");
            }
        }
        //TODO AES加密
        return this.saveOrUpdate(AssetConvert.INSTANCE.convert(dto)) ? Result.ok("操作成功!") : Result.failed("操作失败!");
    }

    @Override
    public Page<AssetRespVO> getPageList(PageRequest<AssetQueryDTO> dto) {
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
        Page<Asset> resp = this.page(page, wrapper);
        Page<AssetRespVO> assetRespVOPage = AssetConvert.INSTANCE.convertPage(resp);
        //转义
        assetRespVOPage.getRecords().forEach(e->{
            //TODO 资产的安全状态目前先全部放安全，等待之后风险部分完成再放入
            e.setAssetSecurityStatus("2");
            changeAssetName(e);
        });
        return assetRespVOPage;
    }

    @Override
    public AssetRespVO getAsset(String id) {
        Asset asset = this.getById(id);
        AssetRespVO convert = AssetConvert.INSTANCE.convert(asset);
        return changeAssetName(convert);
    }

    @Override
    public AssetAccountRespVO getAssetAccount(String id) {
        Asset asset = this.getById(id);
        AssetAccountRespVO account = new AssetAccountRespVO();
        account.setNmAccount(asset.getNmAccount());
        account.setNmPassword(asset.getNmPassword());
        return account;
    }

    private AssetRespVO changeAssetName(AssetRespVO respVO){
        respVO.setAssetClassName(dictDataService.getDictData(DictType.ASSET_CLASS.getType(),respVO.getAssetClass()));
        respVO.setAssetStatusName(dictDataService.getDictData(DictType.ASSET_STATUS.getType(),respVO.getAssetStatus()));
        if(respVO.getAssetType().equals("0")){//逻辑资产
            respVO.setAssetTypeName(dictDataService.getDictData(DictType.LOGICAL_ASSET_TYPE.getType(),respVO.getAssetType()));
        }
        if(respVO.getAssetType().equals("1")){//物理资产
            respVO.setAssetTypeName(dictDataService.getDictData(DictType.PHYSICAL_ASSET_TYPE.getType(),respVO.getAssetType()));
        }
        respVO.setAssetSecurityStatusName(dictDataService.getDictData(DictType.PHYSICAL_ASSET_STATUS.getType(),respVO.getAssetSecurityStatus()));
        AssetGroup assetGroup = assetGroupService.getById(respVO.getAssetGroupId());
        respVO.setAssetGroupName(assetGroup==null?"":assetGroup.getGroupName());
        respVO.setAssetOrgName("资产管理部");//TODO 融入组织框架后放组织
        return respVO;
    }
}
