package com.gem.loganalysis.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.dto.asset.*;
import com.gem.loganalysis.model.entity.*;
import com.gem.loganalysis.model.vo.asset.*;
import com.gem.loganalysis.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * 资产相关转换 Convert
 *
 * @author czw
 */
@Mapper
public interface AssetConvert {

    AssetConvert INSTANCE = Mappers.getMapper(AssetConvert.class);

    //转换资产类型
    List<AssetTypeRespVO> convertList13(List<AssetType> list);

    @Mapping(target = "assetBrand", expression = "java(listToJson(bean.getAssetBrandList()))")
    @Mapping(target = "assetModel", expression = "java(listToJson(bean.getAssetModelList()))")
    @Mapping(target = "osVersion", expression = "java(listToJson(bean.getOsVersionList()))")
    AssetType convert(AssetTypeDTO bean);

    @Mapping(target = "assetBrandList", expression = "java(mapJsonToList(bean.getAssetBrand()))")
    @Mapping(target = "assetModelList", expression = "java(mapJsonToList(bean.getAssetModel()))")
    @Mapping(target = "osVersionList", expression = "java(mapJsonToList(bean.getOsVersion()))")
    AssetTypeRespVO convert(AssetType bean);


    default List<String> mapJsonToList(String json) {
        if(StringUtils.isBlank(json)){
            return new ArrayList<>();
        }
        return JsonUtils.parseArray(json,String.class);
    }

    default String listToJson(List<String> list) {
        if(list==null){
            return null;
        }
        return JsonUtils.toJsonString(list);
    }

    Asset convert(AssetDTO bean);

    AssetGroup convert(AssetGroupDTO bean);
    List<Asset> convertList01(List<AssetDTO> list);
    Page<AssetRespVO> convertPage(Page<Asset> page);
    List<AssetRespVO> convertList10(List<Asset> list);
    List<AssetOverviewVO.NewAssetList> convertList11(List<AssetRespVO> list);
    AssetRespVO convert(Asset page);
    AssetGroupRespVO convert(AssetGroup page);

    //资产导出
    List<PhysicalAssetExcelVO> convertList14(List<AssetRespVO> list);
    List<LogicalAssetExcelVO> convertList15(List<AssetRespVO> list);
    //资产导入
    Asset convert(PhysicalAssetExcelVO bean);
    Asset convert(LogicalAssetExcelVO bean);
    List<Asset> convertList16(List<PhysicalAssetExcelVO> list);
    List<Asset> convertList17(List<LogicalAssetExcelVO> list);

    //逻辑资产扫描
    Page<LogicalAssetScannerRespVO> convertPage02(Page<LogicalAssetTemp> page);
    List<LogicalAssetScannerRespVO> convertList02(List<LogicalAssetTemp> list);
    //IP资产扫描
    Page<PhysicalAssetScannerRespVO> convertPage03(Page<PhysicalAssetTemp> page);
    List<PhysicalAssetScannerRespVO> convertList03(List<PhysicalAssetTemp> list);
    List<AssetOverviewVO.NewAssetScanList> convertList06(List<PhysicalAssetTemp> list);
    //资产分组分页
    List<AssetGroupRespVO> convertList(List<AssetGroup> list);
    Page<AssetGroupRespVO> convertPage04(Page<AssetGroup> page);
    //Vlan配置
    Page<OrgVlanRespVO> convertPage05(Page<OrgVlan> page);
    List<OrgVlanRespVO> convertList05(List<OrgVlan> list);

    //物理资产扫描规则
    @Mapping(target = "oid", expression = "java(listToJson(bean.getOidList()))")
    PhysicalAssetDiscoveryRule convert(PhysicalAssetDiscoveryRuleDTO bean);
    @Mapping(target = "oidList", expression = "java(mapJsonToList(bean.getOid()))")
    PhysicalAssetDiscoveryRuleVO convert(PhysicalAssetDiscoveryRule bean);
    List<PhysicalAssetDiscoveryRuleVO> convertList18(List<PhysicalAssetDiscoveryRule> list);
    Page<PhysicalAssetDiscoveryRuleVO> convertPage06(Page<PhysicalAssetDiscoveryRule> list);
    //逻辑资产扫描规则
    LogicalAssetDiscoveryRule convert(LogicalAssetDiscoveryRuleDTO bean);
    List<LogicalAssetDiscoveryRuleVO> convertList19(List<LogicalAssetDiscoveryRule> list);
    Page<LogicalAssetDiscoveryRuleVO> convertPage07(Page<LogicalAssetDiscoveryRule> list);
}
