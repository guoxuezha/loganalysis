package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.LogMapper;
import com.gem.loganalysis.model.entity.Log;
import com.gem.loganalysis.service.LogService;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Date: 2023/4/26 9:29
 *
 * @author GuoChao
 **/
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

}
