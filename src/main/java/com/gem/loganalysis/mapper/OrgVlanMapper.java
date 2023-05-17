package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.OrgVlan;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Vlan设置 Mapper 接口
 * </p>
 *
 * @author czw
 * @since 2023-05-12
 */
@Mapper
public interface OrgVlanMapper extends BaseMapper<OrgVlan> {

}
