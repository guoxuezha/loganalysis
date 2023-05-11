package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.mapper.AssetGroupMapper;
import com.gem.loganalysis.mapper.AssetMapper;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.AssetGroup;
import com.gem.loganalysis.service.IAssetGroupService;
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
@DS("slave")
public class AssetGroupServiceImpl extends ServiceImpl<AssetGroupMapper, AssetGroup> implements IAssetGroupService {

    @Resource
    private AssetGroupMapper assetGroupMapper;


    @Override
    public List<AssetGroup> getList(AssetGroupQueryDTO dto) {
        LambdaQueryWrapperX<AssetGroup> wrapper = new LambdaQueryWrapperX<AssetGroup>()
                .eqIfPresent(AssetGroup::getAssetOrg, dto.getAssetOrg())
                .likeIfPresent(AssetGroup::getGroupName, dto.getGroupName())
                .orderByAsc(AssetGroup::getCreateTime);//根据创建时间正序排
        return this.list(wrapper);
    }

    @Override
    public Result<String> editGroup(AssetGroupDTO dto) {
        return this.saveOrUpdate(AssetConvert.INSTANCE.convert(dto))?Result.ok("操作成功!"):Result.failed("操作失败!");
    }
}
