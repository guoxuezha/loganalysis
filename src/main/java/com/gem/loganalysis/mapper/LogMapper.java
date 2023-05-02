package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description:
 * Date 2023/4/26 9:27
 *
 * @author GuoChao
 **/
@Mapper
public interface LogMapper extends BaseMapper<Log> {
}
