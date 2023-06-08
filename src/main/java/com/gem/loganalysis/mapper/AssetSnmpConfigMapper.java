package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigDTO;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigDeleteDTO;
import com.gem.loganalysis.model.dto.query.AssetSnmpConfigQueryDTO;
import com.gem.loganalysis.model.entity.AssetSnmpConfig;
import com.gem.loganalysis.model.vo.asset.AssetSnmpConfigRespVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * SNMP参数设置 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@Mapper
public interface AssetSnmpConfigMapper extends BaseMapper<AssetSnmpConfig> {

    /**
     * 新增SNMP参数设置
     * @param dto
     * @return
     */
    int insertSnmpConfig(AssetSnmpConfig dto);

    /**
     * 删除SNMP参数设置
     * @param dto
     * @return
     */
    int deleteSnmpConfig(AssetSnmpConfigDeleteDTO dto);

    /**
     * 更新SNMP参数设置
     * @param convert
     * @return
     */
    int updateSnmpConfig(AssetSnmpConfig convert);

    /**
     *  获得单个SNMP参数设置详情
     * @param dto
     * @return
     */
    AssetSnmpConfigRespVO getSnmpConfig(AssetSnmpConfigDeleteDTO dto);

    /**
     * SNMP列表
     * @param dto
     * @return
     */
    List<AssetSnmpConfigRespVO> getList(AssetSnmpConfigQueryDTO dto);
}
