package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.BlockOffRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 封堵历史记录 Mapper 接口
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Mapper
public interface BlockOffRecordMapper extends BaseMapper<BlockOffRecord> {

}
