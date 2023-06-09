package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.BlockRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 封堵规则 Mapper 接口
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Mapper
public interface BlockRuleMapper extends BaseMapper<BlockRule> {

}
