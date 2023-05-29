package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.dto.asset.AssetTypeDTO;
import com.gem.loganalysis.model.dto.asset.AssetTypeQueryDTO;
import com.gem.loganalysis.model.entity.AssetType;
import com.gem.loganalysis.model.vo.asset.AssetTypeRespVO;
import com.gem.loganalysis.model.vo.asset.AssetTypeVO;

import java.util.List;
import java.util.Map;

/**
 * 资产类别 Service 接口
 *
 * @author czw
 */
public interface IAssetTypeService extends IService<AssetType> {

    /**
     * 初始化资产列表缓存
     */
    void initLocalCache();

    /**
     * 资产类型列表
     * @return
     */
    List<AssetTypeRespVO> getList(AssetTypeQueryDTO dto);

    Map<String,List<AssetTypeRespVO>> getTypeMap(AssetTypeQueryDTO dto);

    List<AssetTypeVO> getAssetList(AssetTypeQueryDTO dto);

    /**
     * 新增/编辑资产类型
     * @return
     */
    boolean editType(AssetTypeDTO dto);

    /**
     * 根据ID获得大类型-小类型格式的类别名称
     * @param dto
     * @return
     */
    String getAssetTypeName(Integer typeId);
    /**
     * 根据类别名称获取ID
     * @param dto
     * @return
     */
    String getAssetTypeId(String typeName);
}
