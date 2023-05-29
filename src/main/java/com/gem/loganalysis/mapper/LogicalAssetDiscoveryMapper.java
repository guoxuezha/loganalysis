package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.LogicalAssetDiscoveryRule;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 逻辑资产扫描结果 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Mapper
public interface LogicalAssetDiscoveryMapper extends BaseMapper<LogicalAssetDiscoveryRule> {

}
