package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.DictDataMapper;
import com.gem.loganalysis.model.entity.DictData;
import com.gem.loganalysis.service.IDictDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author czw
 * @since 2023-05-10
 */
@Service
@DS("slave")
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements IDictDataService {

    @Resource
    private DictDataMapper dictDataMapper;




}
