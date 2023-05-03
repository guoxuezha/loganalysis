package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.LogMapper;
import com.gem.loganalysis.model.entity.Log;
import com.gem.loganalysis.service.ILogService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

}
