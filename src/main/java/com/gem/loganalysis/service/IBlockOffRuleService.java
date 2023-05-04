package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.query.BlockRecordQueryDTO;
import com.gem.loganalysis.model.entity.BlockOffRule;

/**
 * 封堵规则 服务类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
public interface IBlockOffRuleService extends IService<BlockOffRule> {

    IPage<Object> blockOffRecords(PageRequest<BlockRecordQueryDTO> dto);
}
