package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.dto.query.OverviewQueryDTO;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.model.vo.EventOverviewVO;

/**
 * <p>
 * 资产事件记录 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-10
 */
public interface IAssetEventService extends IService<AssetEvent> {

    /**
     * 查询风险概览信息
     *
     * @param dto 查询入参
     * @return 响应
     */
    EventOverviewVO geOverviewInfo(OverviewQueryDTO dto);
}
