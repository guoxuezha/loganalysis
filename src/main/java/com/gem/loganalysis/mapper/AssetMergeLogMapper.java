package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.AssetMergeLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 归并日志内容 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-10
 */
@Mapper
public interface AssetMergeLogMapper extends BaseMapper<AssetMergeLog> {

}
