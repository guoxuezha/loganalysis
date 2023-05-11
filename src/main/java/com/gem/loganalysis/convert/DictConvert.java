package com.gem.loganalysis.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.dto.edit.DictDataDTO;
import com.gem.loganalysis.model.dto.edit.DictTypeDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.AssetGroup;
import com.gem.loganalysis.model.entity.DictData;
import com.gem.loganalysis.model.entity.DictType;
import com.gem.loganalysis.model.vo.DictDataRespVO;
import com.gem.loganalysis.model.vo.DictTypeRespVO;
import com.gem.loganalysis.model.vo.asset.AssetGroupRespVO;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据字典转换 Convert
 *
 * @author czw
 */
@Mapper
public interface DictConvert {

    DictConvert INSTANCE = Mappers.getMapper(DictConvert.class);

    DictType convert(DictTypeDTO bean);
    DictTypeRespVO convert(DictType bean);
    List<DictTypeRespVO> convertList(List<DictType> list);
    Page<DictTypeRespVO> convertPage(Page<DictType> page);


    DictData convert(DictDataDTO bean);
    DictDataRespVO convert(DictData bean);
    List<DictDataRespVO> convertList01(List<DictData> list);
    Page<DictDataRespVO> convertPage01(Page<DictData> page);
}
