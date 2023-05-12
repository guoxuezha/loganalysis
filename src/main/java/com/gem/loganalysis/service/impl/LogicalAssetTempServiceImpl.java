package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.LogicalAssetTempMapper;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.service.ILogicalAssetTempService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 逻辑资产扫描结果 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Service
@DS("slave")
public class LogicalAssetTempServiceImpl extends ServiceImpl<LogicalAssetTempMapper, LogicalAssetTemp> implements ILogicalAssetTempService {

    public void abc(){
        System.out.println(11);
    }
}
