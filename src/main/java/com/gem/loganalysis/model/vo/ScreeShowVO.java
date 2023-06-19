package com.gem.loganalysis.model.vo;

import cn.hutool.core.collection.CollUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 大屏显示数据视图类
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/15 21:36
 */
@Data
public class ScreeShowVO {

    @ApiModelProperty("安全设备")
    private Integer safeEquipmentNum;

    @ApiModelProperty("网络设备")
    private Integer networkEquipmentNum;

    @ApiModelProperty("IT设备")
    private Integer ITEquipmentNum;

    @ApiModelProperty("终端设备")
    private Integer terminalEquipmentNum;

    @ApiModelProperty("风险资产Top")
    private List<RiskAsset> riskAssetTop;

    @ApiModelProperty("风险类型分布")
    private List<TypeNum> riskTypeDistribution;

    @ApiModelProperty("日志告警")
    private List<Object[]> eventList;

    @ApiModelProperty("封堵处置")
    private List<TypeNum> blockList;

    @ApiModelProperty("黑白名单")
    private List<TypeNum> blackAndWhiteList;

    public ScreeShowVO() {
        DAO dao = new DAO();

        riskTypeDistribution = new ArrayList<>();
        String s2 = "SELECT B.EVENT_TYPE, COUNT(1) AS NUM FROM SOP_ASSET_RISK A LEFT JOIN SOP_ASSET_EVENT B ON A.REF_EVENT_ID = B.EVENT_ID GROUP BY B.EVENT_TYPE";
        ArrayList<HashMap<String, String>> dataSet2 = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, s2);
        if (CollUtil.isNotEmpty(dataSet2)) {
            for (HashMap<String, String> map : dataSet2) {
                riskTypeDistribution.add(new TypeNum(map.get("EVENT_TYPE"), Integer.parseInt(map.get("NUM"))));
            }
        }

        eventList = new ArrayList<>();
        String s3 = "SELECT A.ASSET_ID, A.ASSET_NAME FROM SOP_ASSET A LEFT JOIN SOP_ASSET_EVENT B ON A.ASSET_ID = B.ASSET_ID WHERE B.EVENT_ID IS NOT NULL GROUP BY A.ASSET_ID ORDER BY COUNT(1) DESC LIMIT 7";
        ArrayList<HashMap<String, String>> dataSet3 = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, s3);
        String[] assetNames = new String[7];
        Integer[] low = new Integer[]{0, 0, 0, 0, 0, 0, 0};
        Integer[] medium = new Integer[]{0, 0, 0, 0, 0, 0, 0};
        Integer[] high = new Integer[]{0, 0, 0, 0, 0, 0, 0};
        if (CollUtil.isNotEmpty(dataSet3)) {
            for (int i = 0; i < dataSet3.size(); i++) {
                HashMap<String, String> map = dataSet3.get(i);
                String assetId = map.get("ASSET_ID");
                String assetName = map.get("ASSET_NAME");
                assetNames[i] = assetName;
                ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, "SELECT EVENT_CLASS, COUNT(1) AS NUM FROM SOP_ASSET_EVENT WHERE ASSET_ID = '" + assetId + "' GROUP BY EVENT_CLASS");
                if (CollUtil.isNotEmpty(dataSet)) {
                    for (HashMap<String, String> hashMap : dataSet) {
                        String num = hashMap.get("NUM");
                        switch (hashMap.get("EVENT_CLASS")) {
                            case "1":
                                low[i] = Integer.parseInt(num);
                                break;
                            case "2":
                                medium[i] = Integer.parseInt(num);
                                break;
                            case "3":
                                high[i] = Integer.parseInt(num);
                                break;
                            default:
                                high[i] = 0;
                                break;
                        }
                    }
                }
            }
        }
        eventList.add(assetNames);
        eventList.add(low);
        eventList.add(medium);
        eventList.add(high);

        blockList = new ArrayList<>();
        String s4 = "SELECT BLOCK_STATE, COUNT(1) AS NUM FROM SOP_BLOCK_RECORD GROUP BY BLOCK_STATE";
        ArrayList<HashMap<String, String>> dataSet4 = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, s4);
        if (CollUtil.isNotEmpty(dataSet4)) {
            for (HashMap<String, String> map : dataSet4) {
                blockList.add(new TypeNum(map.get("BLOCK_STATE"), Integer.parseInt(map.get("NUM"))));
            }
        }

        blackAndWhiteList = new ArrayList<>();
        long blackListSize = dao.getDataSetCount(BaseConstant.DEFAULT_POOL_NAME, "SELECT 1 FROM M4_SSO_BLACK_LIST");
        long whiteListSize = dao.getDataSetCount(BaseConstant.DEFAULT_POOL_NAME, "SELECT 1 FROM M4_SSO_WHITE_LIST");
        blackAndWhiteList.add(new TypeNum("黑名单", (int) blackListSize));
        blackAndWhiteList.add(new TypeNum("白名单", (int) whiteListSize));
    }

    @Data
    static class RiskAsset {

        private String assetName;

        private String ip;

        private Double weakMeasure;

        private Integer highNum;

        private Integer mediumNum;

        private Integer lowNum;

    }

    //放入风险资产
    public void setRiskAsset(List<HostsSeverityVO> list) {
        List<RiskAsset> result = new ArrayList<>();
        list.forEach(e->{
            RiskAsset r = new RiskAsset();
            r.setAssetName(e.getAssetName());
            r.setWeakMeasure(e.getSeverity());
            r.setIp(e.getIp());
            result.add(r);
        });
        this.riskAssetTop = result;
    }

    @Data
    @AllArgsConstructor
    static class TypeNum {

        private String name;

        private Integer num;

    }

}
