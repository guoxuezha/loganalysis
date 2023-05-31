package com.gem.loganalysis.mapper;

import java.util.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 安全管理资产 Mapper
 *
 * @author czw
 */
@Mapper
public interface AssetMapper extends BaseMapper<Asset> {


    List<AssetRespVO> getAssetList(AssetQueryDTO dto);

    AssetRespVO getAssetById(String id);
}
