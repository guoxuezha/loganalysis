package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.BlackWhiteListMapper;
import com.gem.loganalysis.model.entity.BlackWhiteList;
import com.gem.loganalysis.service.IBlackWhiteListService;
import org.springframework.stereotype.Service;

/**
 * 黑白名单 服务实现类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Service
public class BlackWhiteListServiceImpl extends ServiceImpl<BlackWhiteListMapper, BlackWhiteList> implements IBlackWhiteListService {

}
