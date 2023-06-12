package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.mapper.AssetTypeMapper;
import com.gem.loganalysis.model.dto.asset.AssetTypeDTO;
import com.gem.loganalysis.model.dto.asset.AssetTypeQueryDTO;
import com.gem.loganalysis.model.entity.*;
import com.gem.loganalysis.model.vo.asset.*;
import com.gem.loganalysis.service.*;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产类别 Service 实现类
 *
 * @author czw
 */
@Service
@Validated
public class AssetTypeServiceImpl extends ServiceImpl<AssetTypeMapper, AssetType> implements IAssetTypeService {

    @Getter
    //资产类型缓存
    private volatile List<AssetTypeRespVO> typeCache;

    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<AssetType> list = this.list();
        // 第二步：构建缓存
        typeCache = AssetConvert.INSTANCE.convertList13(list);
    }


    @Override
    public List<AssetTypeRespVO> getList(AssetTypeQueryDTO dto) {
        if(dto==null||StringUtils.isBlank(dto.getAssetType())){
            return typeCache;
        }else{
            List<AssetTypeRespVO> list = typeCache;
            return list.stream().filter(e->e.getAssetType().equals(dto.getAssetType())).collect(Collectors.toList());
        }
    }

    @Override
    public Map<String,List<AssetTypeRespVO>> getTypeMap(AssetTypeQueryDTO dto) {
        List<AssetTypeRespVO> list = getList(dto);
        Map<String,List<AssetTypeRespVO>> map = list.stream().collect(Collectors.groupingBy(AssetTypeRespVO::getAssetType));
        return map;
    }

    @Override
    public List<AssetTypeVO> getAssetList(AssetTypeQueryDTO dto) {
        List<AssetTypeVO> list = new ArrayList<>();
        Map<String, List<AssetTypeRespVO>> typeMap = getTypeMap(dto);
        typeMap.forEach((m,n)->{
            list.add(new AssetTypeVO(m,m,n));
        });
        return list;
    }

    @Override
    public boolean editType(AssetTypeDTO dto) {
        AssetType type = AssetConvert.INSTANCE.convert(dto);
        boolean b = this.saveOrUpdate(type);
        initLocalCache();
        return b;
    }

    @Override
    public String getAssetTypeName(Integer typeId) {
        if(typeId==null){
            return "";
        }
        List<AssetTypeRespVO> collect = typeCache.stream().filter(e -> e.getTypeId().equals(typeId)).collect(Collectors.toList());
        if(collect.size()>0){
            AssetTypeRespVO assetTypeRespVO = collect.get(0);
            return assetTypeRespVO.getAssetType()+"-"+assetTypeRespVO.getTypeName();
        }
        return "";
    }

    @Override
    public AssetTypeRespVO getAssetTypeById(Integer typeId) {
        if(typeId==null){
            return null;
        }
        List<AssetTypeRespVO> collect = typeCache.stream().filter(e -> e.getTypeId().equals(typeId)).collect(Collectors.toList());
        if(collect.size()>0){
            return collect.get(0);
        }
        return  null;
    }

    @Override
    public String getAssetTypeId(String typeName) {
        if(StringUtils.isBlank(typeName)){
            return null;
        }
        List<AssetTypeRespVO> collect = typeCache.stream().filter(e -> e.getTypeName().equals(typeName)).collect(Collectors.toList());
        if(collect.size()>0){
            AssetTypeRespVO assetTypeRespVO = collect.get(0);
            return assetTypeRespVO.getTypeId().toString();
        }
        return null;
    }
}
