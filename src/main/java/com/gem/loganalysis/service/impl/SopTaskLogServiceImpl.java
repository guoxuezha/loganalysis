package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.SopTaskLogMapper;
import com.gem.loganalysis.model.entity.SopTaskLog;
import com.gem.loganalysis.service.ISopTaskLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 调度任务-任务执行记录 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-26
 */
@Service
public class SopTaskLogServiceImpl extends ServiceImpl<SopTaskLogMapper, SopTaskLog> implements ISopTaskLogService {

}
