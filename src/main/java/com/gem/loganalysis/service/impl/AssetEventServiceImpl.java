package com.gem.loganalysis.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.mapper.AssetEventMapper;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.model.dto.query.OverviewQueryDTO;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.model.vo.AssetEventVO;
import com.gem.loganalysis.model.vo.EventMonitorVO;
import com.gem.loganalysis.model.vo.EventOverviewVO;
import com.gem.loganalysis.model.vo.ITEquipmentVO;
import com.gem.loganalysis.service.IAssetEventService;
import com.gem.loganalysis.util.MapToBeanUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.gem.loganalysis.service.impl.AssetRiskServiceImpl.getBetweenDateList;

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

    @Resource
    private AssetEventMapper assetEventMapper;

    @Resource
    private DAO dao;

    @Override
    public EventOverviewVO geOverviewInfo(OverviewQueryDTO dto) {
        EventOverviewVO result = new EventOverviewVO();
        DateTime startTime;
        String endTime = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        switch (dto.getCycle()) {
            case 0:
                startTime = null;
                break;
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
        StringBuilder querySql = new StringBuilder("SELECT A.*, ASSET_NAME FROM SOP_ASSET_EVENT A LEFT JOIN SOP_ASSET B ON A.ASSET_ID = B.ASSET_ID WHERE TRUE ");
        if (startTime == null) {
            querySql.append("AND LEFT(A.BEGIN_TIME,8) = '").append(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)).append("'");
        } else {
            querySql.append("AND A.BEGIN_TIME BETWEEN '").append(DateUtil.format(startTime, DatePattern.PURE_DATETIME_PATTERN)).append("' ")
                    .append("AND '").append(endTime).append("'");
        }
        ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql.toString(), 0, 0);
        if (CollUtil.isNotEmpty(dataSet)) {
            List<AssetEventVO> list = MapToBeanUtil.execute(dataSet, AssetEventVO.class);
            result.setHandleStatusNum(list.stream().collect(Collectors.groupingBy(AssetEventVO::getHandleStatus)));
            result.setEventTypeNum(list.stream().collect(Collectors.groupingBy(AssetEventVO::getEventType)));
            result.setEventClassNum(list.stream().collect(Collectors.groupingBy(AssetEventVO::getEventClass)));
            result.setSourceIpTop5(list.stream().collect(Collectors.groupingBy(AssetEventVO::getSourceIp)));
            result.setAssetEventTop5(list.stream().collect(Collectors.groupingBy(AssetEventVO::getAssetName)));
            Map<String, List<AssetEventVO>> dailyNum = list.stream().collect(Collectors.groupingBy(e -> e.getBeginTime().substring(0, 8)));
            result.setDailyEventNumList(dailyDataPadding(startTime, dailyNum));
        }
        return result;
    }

    @Override
    public EventMonitorVO getEventMonitor() {
        EventMonitorVO eventMonitorVO = new EventMonitorVO();
        eventMonitorVO.setNetworkEquipmentList(assetEventMapper.getEquipmentList("网络设备"));//网络设备
        eventMonitorVO.setSafetyDEquipmentList(assetEventMapper.getEquipmentList("安全设备"));//安全设备
        //TODO 怎么拿到数据 网络吞吐量怎么拿 先写假的了
        ITEquipmentVO itEquipmentList1 = new ITEquipmentVO()
                .setIpAddress("104.253.151.3")
                .setTypeName("交换机")
                .setOrgName("资产管理部")
                .setCpu("40")
                .setMemory("30")
                .setDisk("30")
                .setNetworkThroughput("86");
        ITEquipmentVO itEquipmentList2 = new ITEquipmentVO()
                .setIpAddress("104.253.151.3")
                .setTypeName("交换机")
                .setOrgName("资产管理部")
                .setCpu("28")
                .setMemory("16")
                .setDisk("30")
                .setNetworkThroughput("73");
        List<ITEquipmentVO> list = new ArrayList<>();
        list.add(itEquipmentList1);
        list.add(itEquipmentList2);
        eventMonitorVO.setItEquipmentList(list);
        eventMonitorVO.setTerminalEquipmentList(assetEventMapper.getEquipmentCount("终端设备"));//终端设备
        return eventMonitorVO;
    }

    private List<EventOverviewVO.TypeNum> dailyDataPadding(DateTime startDate, Map<String, List<AssetEventVO>> typeRiskListMap) {
        List<String> betweenDateList = getBetweenDateList(startDate != null ? startDate : new DateTime(), new DateTime());
        List<EventOverviewVO.TypeNum> result = new ArrayList<>(betweenDateList.size());
        for (String date : betweenDateList) {
            List<AssetEventVO> voList = typeRiskListMap.get(date.replaceAll("-", ""));
            if (CollUtil.isNotEmpty(voList)) {
                result.add(new EventOverviewVO.TypeNum(date, voList.size()));
            } else {
                result.add(new EventOverviewVO.TypeNum(date, 0));
            }
        }
        return result;
    }


}
