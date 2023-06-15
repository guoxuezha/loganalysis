package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.BlockRecordMapper;
import com.gem.loganalysis.mapper.BlockRuleMapper;
import com.gem.loganalysis.mapper.DailyDataMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.query.BlockRecordQueryDTO;
import com.gem.loganalysis.model.entity.BlockRule;
import com.gem.loganalysis.model.entity.DailyData;
import com.gem.loganalysis.service.IBlockRuleService;
import com.gem.loganalysis.service.IDailyDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 每日数据 服务实现类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Service
public class DailyDataImpl extends ServiceImpl<DailyDataMapper, DailyData> implements IDailyDataService {




}
