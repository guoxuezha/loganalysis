package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.dto.query.OverviewQueryDTO;
import com.gem.loganalysis.model.entity.AssetRisk;
import com.gem.loganalysis.model.vo.RiskOverviewVO;

/**
 * <p>
 * 资产风险记录 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-23
 */
public interface IAssetRiskService extends IService<AssetRisk> {

    /**
     * 查询风险概览信息
     *
     * @param dto 查询入参
     * @return 响应
     */
    RiskOverviewVO geOverviewInfo(OverviewQueryDTO dto);

}
