package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.LogIndexMapper;
import com.gem.loganalysis.model.entity.LogIndex;
import com.gem.loganalysis.service.LogIndexService;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Date: 2023/4/26 9:06
 *
 * @author GuoChao
 **/
@Service
public class LogIndexServiceImpl extends ServiceImpl<LogIndexMapper, LogIndex> implements LogIndexService {
}
