package com.gem.loganalysis.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.AssetGroup;
import org.mapstruct.Mapper;
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

    Page<AssetDTO> convertPage(Page<Asset> page);





}
