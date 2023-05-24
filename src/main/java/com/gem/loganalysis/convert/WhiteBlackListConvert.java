package com.gem.loganalysis.convert;

import com.gem.loganalysis.model.dto.edit.BlackWhiteListDTO;
import com.gem.loganalysis.model.entity.BlackList;
import com.gem.loganalysis.model.entity.WhiteList;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 数据字典转换 Convert
 *
 * @author czw
 */
@Mapper
public interface WhiteBlackListConvert {

    WhiteBlackListConvert INSTANCE = Mappers.getMapper(WhiteBlackListConvert.class);

    BlackList convert(BlackWhiteListDTO bean);

    WhiteList convert01(BlackWhiteListDTO bean);
}
