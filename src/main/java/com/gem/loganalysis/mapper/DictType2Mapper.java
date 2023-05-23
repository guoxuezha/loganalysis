package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.DictType2;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 字典类型表持久层
 *
 * @author czw
 * @since 2023-02-27
 */
@Mapper
@Repository
public interface DictType2Mapper extends BaseMapper<DictType2> {


}
