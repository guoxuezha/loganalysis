package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.BlockRecordMapper;
import com.gem.loganalysis.model.entity.BlockRecord;
import com.gem.loganalysis.service.IBlockRecordService;
import org.springframework.stereotype.Service;

/**
 * 封堵历史记录 服务实现类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Service
public class BlockRecordServiceImpl extends ServiceImpl<BlockRecordMapper, BlockRecord> implements IBlockRecordService {

}
