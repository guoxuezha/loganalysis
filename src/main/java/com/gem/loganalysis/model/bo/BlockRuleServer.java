package com.gem.loganalysis.model.bo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gem.loganalysis.mapper.BlockRuleMapper;
import com.gem.loganalysis.model.entity.BlockRule;
import com.gem.loganalysis.util.SpringContextUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 封堵规则内存服务
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/9 17:35
 */
public class BlockRuleServer {

    private static BlockRuleServer INSTANCE;

    @Getter
    private final HashMap<String, AssetBlockRuleBo> logAnalysisRuleBoMap;

    private BlockRuleServer() {
        logAnalysisRuleBoMap = new HashMap<>();
        BlockRuleMapper blockRuleMapper = SpringContextUtil.getBean(BlockRuleMapper.class);
        LambdaQueryWrapper<BlockRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlockRule::getDeleteState, 0);
        List<BlockRule> list = blockRuleMapper.selectList(wrapper);
        Map<String, List<BlockRule>> collect = list.stream().collect(Collectors.groupingBy(BlockRule::getAssetId));
        for (Map.Entry<String, List<BlockRule>> entry : collect.entrySet()) {
            logAnalysisRuleBoMap.put(entry.getKey(), new AssetBlockRuleBo(entry.getKey(), entry.getValue()));
        }
    }

    public static BlockRuleServer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlockRuleServer();
        }
        return INSTANCE;
    }

    /**
     * 判断是否符合根据IP属地执行封堵规则
     *
     * @param assetId  产生日志的资产ID
     * @param sourceIp 日志内容的源IP
     * @return 是否需要执行封堵
     */
    public boolean judgeNeedRegionBlock(String assetId, String sourceIp) {
        AssetBlockRuleBo assetBlockRuleBo = logAnalysisRuleBoMap.get(assetId);
        return assetBlockRuleBo != null && assetBlockRuleBo.needRegionBlock(sourceIp);
    }

}
