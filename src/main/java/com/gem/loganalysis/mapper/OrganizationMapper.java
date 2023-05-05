package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.Organization;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织机构 Mapper 接口
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {

}
