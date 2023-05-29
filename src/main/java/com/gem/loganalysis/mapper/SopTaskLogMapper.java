package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.SopTaskLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调度任务-任务执行记录 Mapper 接口
 *
 * @author GuoChao
 * @since 2023-05-26
 */
@Mapper
public interface SopTaskLogMapper extends BaseMapper<SopTaskLog> {

}
