package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.BlockOffRecordMapper;
import com.gem.loganalysis.mapper.BlockOffRuleMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.query.BlockRecordQueryDTO;
import com.gem.loganalysis.model.entity.BlockOffRule;
import com.gem.loganalysis.service.IBlockOffRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 封堵规则 服务实现类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Service
public class BlockOffRuleServiceImpl extends ServiceImpl<BlockOffRuleMapper, BlockOffRule> implements IBlockOffRuleService {

    @Resource
    private BlockOffRecordMapper blockOffRecordMapper;

    @Override
    public IPage<Object> blockOffRecords(PageRequest<BlockRecordQueryDTO> dto) {


        return null;
    }


}
