package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.AssetEventMapper;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.service.IAssetEventService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资产事件记录 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-10
 */
@Service
public class AssetEventServiceImpl extends ServiceImpl<AssetEventMapper, AssetEvent> implements IAssetEventService {

}
