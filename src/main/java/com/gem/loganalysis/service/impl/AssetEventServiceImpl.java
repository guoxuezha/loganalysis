package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.AssetEventMapper;
import com.gem.loganalysis.model.dto.query.RiskOverviewQueryDTO;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.model.vo.RiskOverviewVO;
import com.gem.loganalysis.service.IAssetEventService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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

    private static final List<String> riskTypeList = Arrays.asList("网站风险", "域名风险", "主机风险", "CMS漏洞");

    @Override
    public RiskOverviewVO geOverviewInfo(RiskOverviewQueryDTO dto) {
        return null;
    }
}
