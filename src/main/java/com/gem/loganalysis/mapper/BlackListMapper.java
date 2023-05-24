package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.BlackList;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 黑名单 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Mapper
public interface BlackListMapper extends BaseMapper<BlackList> {

}
