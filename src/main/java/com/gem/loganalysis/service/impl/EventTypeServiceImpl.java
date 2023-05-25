package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.EventTypeMapper;
import com.gem.loganalysis.model.entity.EventType;
import com.gem.loganalysis.service.IEventTypeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 事件类型 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-25
 */
@Service
public class EventTypeServiceImpl extends ServiceImpl<EventTypeMapper, EventType> implements IEventTypeService {

}
