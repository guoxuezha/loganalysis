package com.gem.loganalysis.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.AssetEventMapper;
import com.gem.loganalysis.mapper.AssetRiskMapper;
import com.gem.loganalysis.model.dto.query.OverviewQueryDTO;
import com.gem.loganalysis.model.entity.AssetRisk;
import com.gem.loganalysis.model.vo.RiskOverviewRecordVO;
import com.gem.loganalysis.model.vo.RiskOverviewVO;
import com.gem.loganalysis.service.IAssetRiskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 资产风险记录 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-23
 */
@Service
public class AssetRiskServiceImpl extends ServiceImpl<AssetRiskMapper, AssetRisk> implements IAssetRiskService {

    @Resource
    private AssetEventMapper assetEventMapper;

    /**
     * 根据起止日期区间生成日期列表
     *
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @return 日期列表
     */
    public static List<String> getBetweenDateList(DateTime startDate, DateTime endDate) {
        List<String> dateList = new ArrayList<>();
        long l = DateUtil.betweenDay(startDate, endDate, false);
        for (int i = 1; i <= l; i++) {
            dateList.add(DateUtil.offset(startDate, DateField.DAY_OF_YEAR, i).toString("yyyy-MM-dd"));
        }
        return dateList;
    }

    @Override
    public RiskOverviewVO geOverviewInfo(OverviewQueryDTO dto) {
        RiskOverviewVO result = new RiskOverviewVO();
        DateTime startTime;
        String endTime = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        switch (dto.getCycle()) {
            case 1:
                startTime = DateUtil.offset(new Date(), DateField.WEEK_OF_YEAR, -1);
                break;
            case 2:
                startTime = DateUtil.offset(new Date(), DateField.MONTH, -1);
                break;
            case 3:
                startTime = DateUtil.offset(new Date(), DateField.MONTH, -3);
                break;
            default:
                startTime = new DateTime();
                break;
        }
        List<RiskOverviewRecordVO> list = assetEventMapper.getOverviewInfo(DateUtil.format(startTime, DatePattern.PURE_DATETIME_PATTERN), endTime);

        // 在内存中执行数据统计
        Map<Integer, List<RiskOverviewRecordVO>> handleStatusMap = list.stream().collect(Collectors.groupingBy(RiskOverviewRecordVO::getHandleStatus));
        result.setToBeRepairedNum(CollUtil.isNotEmpty(handleStatusMap.get(0)) ? handleStatusMap.get(0).size() : 0);
        result.setRepairedNum(CollUtil.isNotEmpty(handleStatusMap.get(1)) ? handleStatusMap.get(1).size() : 0);
        result.setRepairedNum(CollUtil.isNotEmpty(handleStatusMap.get(2)) ? handleStatusMap.get(2).size() : 0);
        result.setIgnoreNum(CollUtil.isNotEmpty(handleStatusMap.get(3)) ? handleStatusMap.get(3).size() : 0);

        // 构造每日风险数量
        Map<String, List<RiskOverviewRecordVO>> typeRiskListMap = list.stream().sorted().collect(Collectors.groupingBy(RiskOverviewRecordVO::getEventType));
        result.setRiskNumDaily(dailyDataPadding(startTime, typeRiskListMap));

        // 风险类型分布
        HashMap<String, Integer> riskTypeDistribution = new HashMap<>();
        for (Map.Entry<String, List<RiskOverviewRecordVO>> entry : typeRiskListMap.entrySet()) {
            riskTypeDistribution.put(entry.getKey(), entry.getValue().size());
        }
        result.setRiskTypeDistribution(riskTypeDistribution);
        return result;
    }

    /**
     * 填充每日风险数量
     *
     * @param startDate       统计开始时间
     * @param typeRiskListMap 查询到的风险数据
     * @return 结果
     */
    private Map<String, List<RiskOverviewVO.RiskNumDaily>> dailyDataPadding(DateTime startDate,
                                                                            Map<String, List<RiskOverviewRecordVO>> typeRiskListMap) {
        Map<String, List<RiskOverviewVO.RiskNumDaily>> result = new HashMap<>();
        List<String> betweenDateList = getBetweenDateList(startDate, new DateTime());
        for (Map.Entry<String, List<RiskOverviewRecordVO>> entry : typeRiskListMap.entrySet()) {
            List<RiskOverviewVO.RiskNumDaily> dailyNum = generateDailyList(betweenDateList);
            List<RiskOverviewRecordVO> vos = entry.getValue();
            Map<String, List<RiskOverviewRecordVO>> dateRiskNumMap = vos.stream().collect(Collectors.groupingBy(vo -> vo.getBeginTime().substring(0, 8)));
            for (RiskOverviewVO.RiskNumDaily daily : dailyNum) {
                List<RiskOverviewRecordVO> list = dateRiskNumMap.get(daily.getDate().replaceAll("-", ""));
                daily.setNum(CollUtil.isNotEmpty(list) ? list.size() : 0);
            }
            result.put(entry.getKey(), dailyNum);
        }
        return result;
    }

    private List<RiskOverviewVO.RiskNumDaily> generateDailyList(List<String> dateList) {
        List<RiskOverviewVO.RiskNumDaily> list = new ArrayList<>(dateList.size());
        for (String date : dateList) {
            list.add(new RiskOverviewVO.RiskNumDaily(date, 0));
        }
        return list;
    }


}
