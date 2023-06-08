package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.dto.edit.MibConfigBaseDTO;
import com.gem.loganalysis.model.dto.edit.MibConfigDTO;
import com.gem.loganalysis.model.dto.query.MibConfigQueryDTO;
import com.gem.loganalysis.model.entity.MibConfig;
import com.gem.loganalysis.model.vo.asset.MibConfigRespVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * MIB指令集设置 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@Mapper
public interface MibConfigMapper extends BaseMapper<MibConfig> {


    MibConfigRespVO getConfig(MibConfigBaseDTO dto);

    int insertConfig(MibConfigDTO dto);

    int updateConfig(MibConfigBaseDTO dto);

    int deleteConfig(MibConfigBaseDTO dto);

    List<MibConfigRespVO> getList(MibConfigQueryDTO dto);
}
