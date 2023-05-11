package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.DictTypeMapper;
import com.gem.loganalysis.model.entity.DictType;
import com.gem.loganalysis.service.IDictTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author czw
 * @since 2023-05-10
 */
@Service
@DS("slave")
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements IDictTypeService {

    @Resource
    private DictTypeMapper dictTypeMapper;








}
