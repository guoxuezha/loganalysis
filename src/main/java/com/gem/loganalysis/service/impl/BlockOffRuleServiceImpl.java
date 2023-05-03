package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.BlockOffRuleMapper;
import com.gem.loganalysis.model.entity.BlockOffRule;
import com.gem.loganalysis.service.IBlockOffRuleService;
import org.springframework.stereotype.Service;

/**
 * 封堵规则 服务实现类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Service
public class BlockOffRuleServiceImpl extends ServiceImpl<BlockOffRuleMapper, BlockOffRule> implements IBlockOffRuleService {

}
