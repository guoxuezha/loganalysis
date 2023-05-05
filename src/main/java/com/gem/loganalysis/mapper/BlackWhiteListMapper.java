package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.BlackWhiteList;
import org.apache.ibatis.annotations.Mapper;

/**
 * 黑白名单 Mapper 接口
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Mapper
public interface BlackWhiteListMapper extends BaseMapper<BlackWhiteList> {

}
