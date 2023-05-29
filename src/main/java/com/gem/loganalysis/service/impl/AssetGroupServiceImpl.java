package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.mapper.AssetGroupMapper;
import com.gem.loganalysis.mapper.AssetMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupQueryDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.AssetGroup;
import com.gem.loganalysis.model.vo.asset.AssetGroupRespVO;
import com.gem.loganalysis.service.IAssetGroupService;
import com.gem.loganalysis.service.IM4SsoOrgService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * 安全管理资产分组 Service 实现类
 *
 * @author czw
 */
@Service
@Validated
public class AssetGroupServiceImpl extends ServiceImpl<AssetGroupMapper, AssetGroup> implements IAssetGroupService {

    @Resource
    private AssetGroupMapper assetGroupMapper;
    @Resource
    private IM4SsoOrgService orgService;

    @Override
    public Result<String> editGroup(AssetGroupDTO dto) {
        return this.saveOrUpdate(AssetConvert.INSTANCE.convert(dto))?Result.ok("操作成功!"):Result.failed("操作失败!");
    }

    @Override
    public Page<AssetGroupRespVO> getPageList(PageRequest<AssetGroupQueryDTO> dto) {
        Page<AssetGroup> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        AssetGroupQueryDTO data = dto.getData();
        LambdaQueryWrapperX<AssetGroup> wrapper = new LambdaQueryWrapperX<AssetGroup>()
                .eqIfPresent(AssetGroup::getAssetOrg, data.getAssetOrg())
                .likeIfPresent(AssetGroup::getGroupName, data.getGroupName())
                .orderByAsc(AssetGroup::getCreateTime);//根据创建时间正序排
        Page<AssetGroupRespVO> resp = AssetConvert.INSTANCE.convertPage04(this.page(page, wrapper));
        resp.getRecords().forEach(e->{
            e.setAssetOrgName(orgService.changeOrgName(e.getAssetOrg()));
        });
        return resp;
    }

    @Override
    public List<AssetGroupRespVO> getList(AssetGroupQueryDTO dto) {
        LambdaQueryWrapperX<AssetGroup> wrapper = new LambdaQueryWrapperX<AssetGroup>()
                .eqIfPresent(AssetGroup::getAssetOrg, dto.getAssetOrg())
                .likeIfPresent(AssetGroup::getGroupName, dto.getGroupName())
                .orderByAsc(AssetGroup::getCreateTime);//根据创建时间正序排
        List<AssetGroupRespVO> list = AssetConvert.INSTANCE.convertList(this.list(wrapper));
        list.forEach(e->{
            e.setAssetOrgName(orgService.changeOrgName(e.getAssetOrg()));
        });
        return list;
    }

    @Override
    public AssetGroupRespVO getAssetGroup(String id) {
        AssetGroup assetGroup = this.getById(id);
        AssetGroupRespVO respVO = AssetConvert.INSTANCE.convert(assetGroup);
        respVO.setAssetOrgName(orgService.changeOrgName(respVO.getAssetOrg()));
        return respVO;
    }

}
