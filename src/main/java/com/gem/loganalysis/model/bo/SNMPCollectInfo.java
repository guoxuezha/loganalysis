package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.model.vo.asset.ITAssetMonitorInfoVO;
import com.gem.loganalysis.snmpmonitor.CommonOID;
import com.gem.loganalysis.snmpmonitor.SNMPMonitorServer;
import com.gem.loganalysis.snmpmonitor.SNMPMonitorThread;
import com.gem.loganalysis.snmpmonitor.SNMPMonitorThreadPool;
import com.gem.loganalysis.util.CustomDateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * SNMP监听到的网络出入数据
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/20 0:34
 */
@Slf4j
public class SNMPCollectInfo {

    private static final int MAX_MINUTE = 120;

    private static SNMPCollectInfo INSTANCE = null;

    private final HashMap<String, AssetIOInfo> cache = new HashMap<>();

    private final List<ITAssetMonitorInfoVO> ITLoadInfoList = new ArrayList<>();

    /**
     * 出口设备资产ID,若有多个使用逗号分隔
     * 尽量只配一个
     */
    private String outPutAssetId = null;

    private SNMPCollectInfo() {
        // 初始化outPutAssetId信息
        DAO dao = new DAO();
        String querySql = "SELECT ASSET_ID FROM SOP_ASSET WHERE ASSET_TAG = '出口设备'";
        ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql);
        if (CollUtil.isNotEmpty(dataSet)) {
            StringBuilder assetIds = new StringBuilder();
            for (HashMap<String, String> map : dataSet) {
                assetIds.append(map.get("ASSET_ID")).append(",");
            }
            assetIds.delete(assetIds.length() - 1, assetIds.length());
            outPutAssetId = assetIds.toString();
        }
    }

    public static SNMPCollectInfo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SNMPCollectInfo();
        }
        return INSTANCE;
    }

    /**
     * 获取每秒的网络吞吐量
     *
     * @param assetId 资产ID
     * @return 每秒网络吞吐量
     */
    public Double getInAndOutRate(String assetId) {
        double totalByte = 0;
        AssetIOInfo assetIOInfo = cache.get(assetId);
        if (assetIOInfo == null) {
            return totalByte;
        }
        ArrayList<NetIOUnit> inOctetIncr = assetIOInfo.getInOctetIncr();
        if (CollUtil.isNotEmpty(inOctetIncr)) {
            totalByte += inOctetIncr.get(inOctetIncr.size() - 1).getSize();
        }
        ArrayList<NetIOUnit> outOctetIncr = assetIOInfo.getOutOctetIncr();
        if (CollUtil.isNotEmpty(outOctetIncr)) {
            totalByte += outOctetIncr.get(outOctetIncr.size() - 1).getSize();
        }
        return totalByte / 60;
    }

    public void updateLoadInfo() {
        ITLoadInfoList.clear();
        // 查询配置了SNMP信息的IT资产列表
        String querySql = "SELECT A.ASSET_ID, A.ASSET_NAME, A.IP_ADDRESS, ASSET_NAME, D.TEXT, GROUP_NAME FROM SOP_ASSET A " +
                "LEFT JOIN SOP_ASSET_GROUP B ON A.ASSET_GROUP_ID = B.GROUP_ID " +
                "LEFT JOIN SOP_ASSET_SNMP_CONFIG C ON A.ASSET_ID = C.ASSET_ID " +
                "LEFT JOIN SYS_DICT_ITEM D ON A.ASSET_TYPE = D.ID " +
                "WHERE SNMP_COMMUNITY IS NOT NULL ";
        ArrayList<HashMap<String, String>> dataSet = new DAO().getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql, 0, 0);
        if (CollUtil.isNotEmpty(dataSet)) {
            for (HashMap<String, String> map : dataSet) {
                ITLoadInfoList.add(new ITAssetMonitorInfoVO(map));
            }
        }
        Collections.sort(ITLoadInfoList);
    }

    public void updateIOInfo(DateTime now) {
        SNMPMonitorThreadPool threadPool = SNMPMonitorServer.getInstance().getThreadPool();
        for (SNMPMonitorThread thread : threadPool.getThreadPool().values()) {
            String assetId = thread.getAssetId();
            if (cache.get(assetId) == null) {
                cache.put(assetId, new AssetIOInfo());
            }
            AssetIOInfo assetIOInfo = cache.get(assetId);
            CommonOID eth0InOctet = thread.getMeasureValue("eht0接口收到的字节数");
            if (eth0InOctet != null) {
                String byteSize = eth0InOctet.getValue();
                assetIOInfo.appendInOctet(new NetIOUnit(Long.parseLong(byteSize), now));
            }
            CommonOID eth0OutOctet = thread.getMeasureValue("eht0接口发送的字节数");
            if (eth0OutOctet != null) {
                String byteSize = eth0OutOctet.getValue();
                assetIOInfo.appendOutOctet(new NetIOUnit(Long.parseLong(byteSize), now));
            }
            log.info("assetId: {}, assetIOInfo: {}", assetId, assetIOInfo);
        }

    }

    public List<ITAssetMonitorInfoVO> getITLoadInfoList() {
        if (CollUtil.isEmpty(ITLoadInfoList)) {
            updateLoadInfo();
        }
        return ITLoadInfoList;
    }

    /**
     * 查询出口设备总负荷
     *
     * @return 每分钟负荷值
     */
    public ArrayList<NetIOUnit> getAllOutLoad() {
        ArrayList<NetIOUnit> result = new ArrayList<>();
        if (StrUtil.isEmpty(outPutAssetId)) {
            log.warn("出口设备ID为空!");
            return result;
        }

        // 载入输出设备
        List<AssetIOInfo> outPutAssetIOInfoList = new ArrayList<>();
        if (outPutAssetId.contains(",")) {
            String[] split = outPutAssetId.split(",");
            for (String id : split) {
                if (cache.get(id) != null) {
                    outPutAssetIOInfoList.add(cache.get(id));
                }
            }
        } else {
            if (cache.get(outPutAssetId) != null) {
                outPutAssetIOInfoList.add(cache.get(outPutAssetId));
            }
        }
        if (CollUtil.isEmpty(outPutAssetIOInfoList)) {
            log.warn("出口设备列表为空!");
            return result;
        }

        // 获取输出流量负荷
        for (int i = 0; i < outPutAssetIOInfoList.size(); i++) {
            AssetIOInfo assetIOInfo = outPutAssetIOInfoList.get(i);
            ArrayList<NetIOUnit> outOctet = assetIOInfo.getOutOctetIncr();
            // 如果只有一台输出设备,则直接返回该设备的增量发送字节数
            if (i == 0) {
                log.info("出口设备数量为1,返回每分钟发送字节数: {}", Arrays.toString(outOctet.toArray()));
                result = outOctet;
            } else {
                // 否则根据time做累加
                if (outOctet.size() == 0) {
                    continue;
                }
                boolean flag = result.get(0).getTime().before(outOctet.get(0).getTime());
                int j = 0, k = 0;
                while (j < result.size() && k < outOctet.size()) {
                    NetIOUnit unit = result.get(j);
                    NetIOUnit netIOUnit = outOctet.get(k);
                    if (unit.time.equals(netIOUnit.getTime())) {
                        unit.setSize(unit.getSize() + netIOUnit.getSize());
                        j++;
                        k++;
                    } else {
                        if (flag) {
                            k++;
                        } else {
                            j++;
                        }
                    }
                }
            }
        }
        log.info("result: {}", Arrays.toString(result.toArray()));
        if (result.size() < MAX_MINUTE) {
            return fillEmptyData(result);
        }
        return result;
    }

    /**
     * 填充0值
     *
     * @param sourceList 根据采集到的数据计算得出的时分负荷数据
     * @return 补0后的数据
     */
    private ArrayList<NetIOUnit> fillEmptyData(ArrayList<NetIOUnit> sourceList) {
        ArrayList<NetIOUnit> result = new ArrayList<>(MAX_MINUTE);
        DateTime endTime = sourceList.get(sourceList.size() - 1).getTime();
        List<DateTime> dateTimeList = CustomDateUtil.getBetweenTimeList(endTime, MAX_MINUTE);
        Map<String, NetIOUnit> map = sourceList.stream().collect(Collectors.toMap(NetIOUnit::getTimeStr, Function.identity()));
        for (DateTime dateTime : dateTimeList) {
            if (map.get(dateTime.toString("HH:mm")) != null) {
                result.add(map.get(dateTime.toString("HH:mm")));
            } else {
                result.add(new NetIOUnit(0L, dateTime));
            }
        }
        return result;
    }

    @Data
    private static class AssetIOInfo {

        @ApiModelProperty("收到的字节数(累计值)")
        private final ArrayList<NetIOUnit> inOctetSum;

        @ApiModelProperty("收到的字节数(增量)")
        private final ArrayList<NetIOUnit> inOctetIncr;

        @ApiModelProperty("发送的字节数(累计值)")
        private final ArrayList<NetIOUnit> outOctetSum;

        @ApiModelProperty("发送的字节数(增量)")
        private final ArrayList<NetIOUnit> outOctetIncr;

        protected AssetIOInfo() {
            inOctetSum = new ArrayList<>();
            outOctetSum = new ArrayList<>();
            inOctetIncr = new ArrayList<>();
            outOctetIncr = new ArrayList<>();
        }

        protected void appendInOctet(NetIOUnit unit) {
            if (inOctetSum.isEmpty()) {
                inOctetSum.add(unit);
            } else {
                NetIOUnit lastUnit = inOctetSum.get(inOctetSum.size() - 1);
                if (lastUnit.before(unit)) {
                    inOctetSum.add(unit);
                    inOctetIncr.add(lastUnit.getIncrUnit(unit));
                }
            }
            while (inOctetSum.size() > MAX_MINUTE) {
                inOctetSum.remove(0);
            }
        }

        protected void appendOutOctet(NetIOUnit unit) {
            if (outOctetSum.isEmpty()) {
                outOctetSum.add(unit);
            } else {
                NetIOUnit lastUnit = outOctetSum.get(outOctetSum.size() - 1);
                if (lastUnit.before(unit)) {
                    outOctetSum.add(unit);
                    outOctetIncr.add(lastUnit.getIncrUnit(unit));
                }
            }
            while (outOctetSum.size() > MAX_MINUTE) {
                outOctetSum.remove(0);
            }
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NetIOUnit {

        @ApiModelProperty("字节数")
        private long size;

        @ApiModelProperty("时间戳")
        private DateTime time;

        @ApiModelProperty("时间戳字符串")
        private String timeStr;

        public NetIOUnit(DateTime t) {
            time = t;
            timeStr = t.toString("HH:mm");
            size = RandomUtil.randomLong(60, 90);
        }

        NetIOUnit(Long byteSize, DateTime date) {
            size = byteSize;
            time = date;
            timeStr = time.toString("HH:mm");
        }

        /**
         * 判断目标数据的产生是否早于当前数据单元
         *
         * @param unit 目标数据单元
         * @return 结果
         */
        boolean before(NetIOUnit unit) {
            return this.time.before(unit.time);
        }

        /**
         * 获取增量单元
         *
         * @param endUnit endUnit
         * @return incrUnit
         */
        NetIOUnit getIncrUnit(NetIOUnit endUnit) {
            NetIOUnit incrUnit = new NetIOUnit();
            incrUnit.size = endUnit.getSize() - this.size;
            incrUnit.time = endUnit.time;
            incrUnit.timeStr = endUnit.timeStr;
            return incrUnit;
        }
    }

}
