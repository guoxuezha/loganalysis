package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.PhysicalAssetDiscoveryRule;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 物理资产扫描规则 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Mapper
public interface PhysicalAssetDiscoveryMapper extends BaseMapper<PhysicalAssetDiscoveryRule> {

}
