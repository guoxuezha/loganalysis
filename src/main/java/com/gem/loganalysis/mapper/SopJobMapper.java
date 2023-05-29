package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.SopJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调度任务-工作 Mapper 接口
 *
 * @author GuoChao
 * @since 2023-05-26
 */
@Mapper
public interface SopJobMapper extends BaseMapper<SopJob> {

}
