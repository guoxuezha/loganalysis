package com.gem.loganalysis.convert;

import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigDTO;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigUpdateDTO;
import com.gem.loganalysis.model.entity.AssetGroup;
import com.gem.loganalysis.model.entity.AssetSnmpConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SNMP相关转换 Convert
 *
 * @author czw
 */
@Mapper
public interface SnmpConvert {

    SnmpConvert INSTANCE = Mappers.getMapper(SnmpConvert.class);

    AssetSnmpConfig convert(AssetSnmpConfigDTO bean);

    AssetSnmpConfig convert(AssetSnmpConfigUpdateDTO bean);


}
