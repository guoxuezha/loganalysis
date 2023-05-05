package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper 接口
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}
