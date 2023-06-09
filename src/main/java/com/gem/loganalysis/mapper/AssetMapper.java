package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.vo.AssetEventHomeOverviewVO;
import com.gem.loganalysis.model.vo.HomeOverviewVO;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 安全管理资产 Mapper
 *
 * @author czw
 */
@Mapper
public interface AssetMapper extends BaseMapper<Asset> {

    List<AssetRespVO> getAssetList(AssetQueryDTO dto);

    List<AssetRespVO> getLogAsset(AssetQueryDTO dto);

    AssetRespVO getAssetById(String id);

    HomeOverviewVO getAssetHomeOverview();

    //今日各类型事件发生次数
    List<AssetEventHomeOverviewVO> getEventHomeOverview();
}
