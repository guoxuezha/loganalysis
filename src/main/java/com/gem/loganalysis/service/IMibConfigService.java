package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.dto.edit.MibConfigBaseDTO;
import com.gem.loganalysis.model.dto.edit.MibConfigDTO;
import com.gem.loganalysis.model.dto.query.MibConfigQueryDTO;
import com.gem.loganalysis.model.entity.MibConfig;
import com.gem.loganalysis.model.vo.asset.MibConfigRespVO;

import java.util.List;

/**
 * <p>
 * MIB指令集设置 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
public interface IMibConfigService extends IService<MibConfig> {

    /**
     * 新增MIB指令集
     * @param dto
     * @return
     */
    int insertConfig(MibConfigDTO dto);

    /**
     * 删除MIB指令集设置
     * @param dto
     * @return
     */
    int deleteConfig(MibConfigBaseDTO dto);

    /**
     * 更新MIB指令集设置
     * @param dto
     * @return
     */
    int updateConfig(MibConfigDTO dto);

    /**
     * 获得单MIB指令集设置详情
     * @param dto
     * @return
     */
    MibConfigRespVO getConfig(MibConfigBaseDTO dto);

    /**
     * 获得MIB指令集设置列表
     * @param dto
     * @return
     */
    List<MibConfigRespVO> getList(MibConfigQueryDTO dto);

    /**
     * 获得MIB指令集设置分页
     * @param dto
     * @return
     */
    PageResponse<MibConfigRespVO> getPage(PageRequest<MibConfigQueryDTO> dto);
}
