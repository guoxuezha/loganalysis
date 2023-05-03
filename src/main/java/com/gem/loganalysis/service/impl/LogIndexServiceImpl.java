package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.LogIndexMapper;
import com.gem.loganalysis.model.entity.LogIndex;
import com.gem.loganalysis.service.ILogIndexService;
import org.springframework.stereotype.Service;

/**
 * 日志索引 服务实现类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Service
public class LogIndexServiceImpl extends ServiceImpl<LogIndexMapper, LogIndex> implements ILogIndexService {

}
