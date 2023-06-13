package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.model.dto.block.BlockExecuteHelper;
import com.gem.loganalysis.model.entity.BlockRule;
import com.gem.loganalysis.util.IPUtil;
import com.gem.loganalysis.util.MapToBeanUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资产封堵规则独立对象
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/9 17:41
 */
@Slf4j
public class AssetBlockRuleBo {

    private final String assetId;

    /**
     * 该资产规则自身的属地信息
     */
    private RegionInfo localRegin;

    /**
     * 根据风险级别封堵规则
     */
    private List<BlockRule> riskBlockRuleList;

    /**
     * 根据属地封堵规则
     */
    private List<BlockRule> regionBlockRuleList;

    /**
     * 是否存在IP属地封堵规则
     */
    private boolean haveIp2RegionRule;

    protected AssetBlockRuleBo(String assetId, List<BlockRule> ruleList) {
        this.assetId = assetId;
        init(ruleList);
    }

    /**
     * 初始化业务对象
     *
     * @param ruleList 封堵规则列表
     */
    private void init(List<BlockRule> ruleList) {
        ArrayList<HashMap<String, String>> dataSet = new DAO().getDataSet(BaseConstant.DEFAULT_POOL_NAME, "SELECT IP_ADDRESS FROM SOP_ASSET WHERE ASSET_ID = '" + this.assetId + "' LIMIT 1", 1, 1);
        if (CollUtil.isNotEmpty(dataSet)) {
            String assetIp = dataSet.get(0).get("IP_ADDRESS");
            this.localRegin = new RegionInfo(assetIp);

            Map<Integer, List<BlockRule>> collect = ruleList.stream().collect(Collectors.groupingBy(BlockRule::getRuleType));
            this.riskBlockRuleList = collect.get(0);
            this.regionBlockRuleList = collect.get(1);
            this.haveIp2RegionRule = CollUtil.isNotEmpty(this.regionBlockRuleList);
        }
    }

    public void refresh() {
        String querySql = "SELECT BLOCK_RULE_ID, ASSET_D, BLOCK_RULE_DESC, RULE_TYPE, RISK_LEVEL, BLOCK_RANGE, BLOCK_TYPE, BLOCK_DURATION, WHITE_LIST_ENABLE, BLACK_LIST_ENABLE, OPERATION_ASSET_ID " +
                "FROM SOP_BLOCK_RULE WHERE DELETE_STATE = 0 AND ASSET_D = '" + this.assetId + "'";
        ArrayList<HashMap<String, String>> dataSet = new DAO().getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql, 0, 0);
        if (CollUtil.isNotEmpty(dataSet)) {
            List<BlockRule> ruleList = MapToBeanUtil.execute(dataSet, BlockRule.class);
            init(ruleList);
        }
    }

    /**
     * 根据源IP匹配属地规则封堵
     *
     * @param sourceIp 源IP
     * @return 是否需要执行封堵
     */
    public boolean needRegionBlock(String sourceIp) {
        // 内网IP解析到的国家及省份为"0",而非"中国",添加特判以跳过内网IP的属地判断
        if (haveIp2RegionRule && !"0".equals(this.localRegin.country)) {
            RegionInfo sourceIpRegionInfo = new RegionInfo(sourceIp);
            boolean needRegionBlock = false;
            for (BlockRule blockRule : this.regionBlockRuleList) {
                // 逐条判断是否符合属地封堵规则
                switch (blockRule.getBlockRange()) {
                    case 1:
                        needRegionBlock |= !this.localRegin.sameCountry(sourceIpRegionInfo);
                        break;
                    case 2:
                        needRegionBlock |= !this.localRegin.sameProvince(sourceIpRegionInfo);
                        break;
                    case 3:
                        needRegionBlock |= !this.localRegin.sameCity(sourceIpRegionInfo);
                        break;
                }
            }
            return needRegionBlock;
        }
        return false;
    }

    /**
     * 执行封堵操作
     *
     * @param eventId   风险关联的事件ID
     * @param sourceIp  要封堵的IP
     * @param riskLevel 风险级别
     * @param eventType 事件类型(供属地封堵规则特判)
     * @return 封堵结果
     */
    protected boolean executeBlock(String eventId, String sourceIp, Integer riskLevel, String eventType) {
        // 判断应采取哪条封堵规则
        BlockRule rule = null;
        if (BaseConstant.EXTRA_TERRITORIAL_ACCESS.equals(eventType)) {
            rule = CollUtil.isNotEmpty(regionBlockRuleList) ? regionBlockRuleList.get(0) : null;
        } else {
            for (BlockRule blockRule : riskBlockRuleList) {
                if (blockRule.getRiskLevel().equals(riskLevel)) {
                    rule = blockRule;
                    break;
                }
            }
        }
        if (rule != null && StrUtil.isNotEmpty(rule.getOperationAssetId())) {
            BlockExecuteHelper helper = new BlockExecuteHelper(rule.getOperationAssetId(), eventId, sourceIp, rule.getBlockType(), rule.getBlockDuration());
            return BlockExecutor.block(helper);
        } else {
            log.warn("未找到与该风险匹配的封堵规则,或封堵规则未指定操作设备! assetId: {}, riskLevel: {}, eventType: {}", assetId, riskLevel, eventType);
        }
        return false;
    }

    @Data
    static class RegionInfo {

        private String country;

        private String province;

        private String city;

        public RegionInfo(String ip) {
            String regionInfoStr = IPUtil.getInstance().getCityInfoByMemorySearch(ip);
            if (StrUtil.isNotEmpty(regionInfoStr)) {
                String[] split = regionInfoStr.split("\\|");
                country = split[0];
                province = split[2];
                city = split[3];
            }
        }

        protected boolean sameCountry(RegionInfo regionInfo) {
            return this.country.equals(regionInfo.country);
        }

        protected boolean sameProvince(RegionInfo regionInfo) {
            return sameCountry(regionInfo) && this.province.equals(regionInfo.province);
        }

        protected boolean sameCity(RegionInfo regionInfo) {
            return sameProvince(regionInfo) && this.city.equals(regionInfo.city);
        }
    }

}
