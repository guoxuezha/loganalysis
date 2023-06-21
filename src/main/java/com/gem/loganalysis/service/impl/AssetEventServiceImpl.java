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
import com.gem.loganalysis.util.CustomDateUtil;
import com.gem.loganalysis.util.MapToBeanUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
            result.setEvenOriginNum(list.stream().collect(Collectors.groupingBy(AssetEventVO::getEventOrigin)));
            result.setEventTypeNum(list.stream().collect(Collectors.groupingBy(AssetEventVO::getEventType)));
            result.setEventClassNum(list.stream().collect(Collectors.groupingBy(AssetEventVO::getEventClass)));
            result.setHandleStatusNum(list.stream().collect(Collectors.groupingBy(AssetEventVO::getHandleStatus)));
            result.setSourceIpTop5(list.stream().collect(Collectors.groupingBy(AssetEventVO::getSourceIp)));
            result.setAssetEventTop5(list.stream().collect(Collectors.groupingBy(AssetEventVO::getAssetName)));
            result.setTargetIpTop5(list.stream().collect(Collectors.groupingBy(AssetEventVO::getTargetIp)));

            Map<String, List<AssetEventVO>> dailyNum = list.stream().collect(Collectors.groupingBy(e -> e.getBeginTime().substring(0, 8)));
            result.setDailyEventNumMap(dailyDataPadding(startTime, dailyNum));
            result.setUndisposedEventList(list.stream().filter(e -> e.getHandleStatus() == 0).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public EventMonitorVO getEventMonitor() {
        EventMonitorVO eventMonitorVO = new EventMonitorVO();
        eventMonitorVO.setNetworkEquipmentList(assetEventMapper.getEquipmentList("网络设备"));//网络设备
        eventMonitorVO.setSafetyDEquipmentList(assetEventMapper.getEquipmentList("安全设备"));//安全设备
        //已经不从这里取数据了，这里的假数据没用了，单独写了接口
        ITEquipmentVO itEquipmentList1 = new ITEquipmentVO()
                .setIpAddress("104.253.151.3")
                .setTypeName("交换机")
                .setOrgName("资产管理部")
                .setCpu("40")
                .setMemory("30")
                .setDisk("30")
                .setNetworkThroughput("86")
                .setRunningState("正常");
        ITEquipmentVO itEquipmentList2 = new ITEquipmentVO()
                .setIpAddress("104.253.151.4")
                .setTypeName("交换机")
                .setOrgName("资产管理部")
                .setCpu("28")
                .setMemory("16")
                .setDisk("30")
                .setNetworkThroughput("73");
        ITEquipmentVO itEquipmentList3 = new ITEquipmentVO()
                .setIpAddress("104.253.151.5")
                .setTypeName("交换机")
                .setOrgName("资产管理部")
                .setCpu("54")
                .setMemory("24")
                .setDisk("46")
                .setNetworkThroughput("86")
                .setRunningState("正常");
        ITEquipmentVO itEquipmentList4 = new ITEquipmentVO()
                .setIpAddress("104.253.151.6")
                .setTypeName("交换机")
                .setOrgName("资产管理部")
                .setCpu("70")
                .setMemory("88")
                .setDisk("60")
                .setNetworkThroughput("87")
                .setRunningState("异常");
        ITEquipmentVO itEquipmentList5 = new ITEquipmentVO()
                .setIpAddress("104.253.151.7")
                .setTypeName("交换机")
                .setOrgName("资产管理部")
                .setCpu("21")
                .setMemory("30")
                .setDisk("30")
                .setNetworkThroughput("84")
                .setRunningState("正常");
        List<ITEquipmentVO> list = new ArrayList<>();
        list.add(itEquipmentList1);
        list.add(itEquipmentList2);
        list.add(itEquipmentList3);
        list.add(itEquipmentList4);
        list.add(itEquipmentList5);
        eventMonitorVO.setItEquipmentList(list);
        eventMonitorVO.setTerminalEquipmentList(assetEventMapper.getEquipmentCount("终端设备"));//终端设备
        return eventMonitorVO;
    }

    private HashMap<String, List<EventOverviewVO.TypeNum>> dailyDataPadding(DateTime startDate, Map<String, List<AssetEventVO>> typeRiskListMap) {
        HashMap<String, List<EventOverviewVO.TypeNum>> result = new HashMap<>(4);
        List<String> betweenDateList = CustomDateUtil.getBetweenDateList(startDate != null ? startDate : new DateTime(), new DateTime());
        for (int i = 1; i <= 3; i++) {
            List<EventOverviewVO.TypeNum> list = new ArrayList<>(betweenDateList.size());
            for (String date : betweenDateList) {
                String eventClass = String.valueOf(i);
                List<AssetEventVO> assetEventVOS = typeRiskListMap.get(date.replaceAll("-", ""));
                EventOverviewVO.TypeNum typeNum = new EventOverviewVO.TypeNum(date, 0);
                if (CollUtil.isNotEmpty(assetEventVOS)) {
                    List<AssetEventVO> voList = assetEventVOS.stream().filter(e -> eventClass.equals(e.getEventClass())).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(voList)) {
                        typeNum = new EventOverviewVO.TypeNum(date, voList.size());
                    }
                }
                list.add(typeNum);
            }
            String key;
            switch (i) {
                case 1:
                    key = "low";
                    break;
                case 2:
                    key = "medium";
                    break;
                case 3:
                    key = "high";
                    break;
                default:
                    key = null;
            }
            result.put(key, list);
        }
        return result;
    }

}
