package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigDTO;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigDeleteDTO;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigUpdateDTO;
import com.gem.loganalysis.model.dto.query.AssetSnmpConfigQueryDTO;
import com.gem.loganalysis.model.entity.AssetSnmpConfig;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import com.gem.loganalysis.model.vo.asset.AssetSnmpConfigRespVO;

import java.util.List;

/**
 * <p>
 * SNMP参数设置 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
public interface IAssetSnmpConfigService extends IService<AssetSnmpConfig> {

    /**
     * 新增SNMP参数设置
     * @param dto
     * @return
     */
    int insertConfig(AssetSnmpConfigDTO dto);

    /**
     * 删除SNMP参数设置
     * @param dto
     * @return
     */
    int deleteConfig(AssetSnmpConfigDeleteDTO dto);

    /**
     * 更新SNMP参数配置
     * @param dto
     * @return
     */
    int updateConfig(AssetSnmpConfigUpdateDTO dto);

    /**
     * 获得单个SNMP参数设置详情
     * @param dto
     * @return
     */
    AssetSnmpConfigRespVO getConfig(AssetSnmpConfigDeleteDTO dto);

    /**
     * 获得SNMP参数设置列表
     * @param dto
     * @return
     */
    List<AssetSnmpConfigRespVO> getList(AssetSnmpConfigQueryDTO dto);

    /**
     * SNMP参数设置分页
     * @param dto
     * @return
     */
    PageResponse<AssetSnmpConfigRespVO> getPage(PageRequest<AssetSnmpConfigQueryDTO> dto);
}
