package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.AssetEvent;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 资产事件记录 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-10
 */
@Mapper
public interface AssetEventMapper extends BaseMapper<AssetEvent> {

}
