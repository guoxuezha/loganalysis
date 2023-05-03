package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.BlockOffRecordMapper;
import com.gem.loganalysis.model.entity.BlockOffRecord;
import com.gem.loganalysis.service.IBlockOffRecordService;
import org.springframework.stereotype.Service;

/**
 * 封堵历史记录 服务实现类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Service
public class BlockOffRecordServiceImpl extends ServiceImpl<BlockOffRecordMapper, BlockOffRecord> implements IBlockOffRecordService {

}
