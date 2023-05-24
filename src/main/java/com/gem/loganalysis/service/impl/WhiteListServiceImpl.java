package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.WhiteListMapper;
import com.gem.loganalysis.model.entity.WhiteList;
import com.gem.loganalysis.service.IWhiteListService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 白名单 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Service
public class WhiteListServiceImpl extends ServiceImpl<WhiteListMapper, WhiteList> implements IWhiteListService {

}
