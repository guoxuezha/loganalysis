package com.gem.loganalysis.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.entity.*;
import com.gem.loganalysis.model.vo.asset.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 资产相关转换 Convert
 *
 * @author czw
 */
@Mapper
public interface AssetConvert {

    AssetConvert INSTANCE = Mappers.getMapper(AssetConvert.class);

    Asset convert(AssetDTO bean);

    AssetGroup convert(AssetGroupDTO bean);

    Page<AssetRespVO> convertPage(Page<Asset> page);



    //逻辑资产扫描
    Page<LogicalAssetScannerRespVO> convertPage02(Page<LogicalAssetTemp> page);
    //IP资产扫描
    Page<PhysicalAssetScannerRespVO> convertPage03(Page<PhysicalAssetTemp> page);
    //资产分组分页
    List<AssetGroupRespVO> convertList(List<AssetGroup> list);
    Page<AssetGroupRespVO> convertPage04(Page<AssetGroup> page);
    //Vlan配置
    Page<OrgVlanRespVO> convertPage05(Page<OrgVlan> page);


}
