package com.gem.loganalysis.snmpmonitor;

import com.gem.utils.command.Shell;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class SNMPMonitorThread extends Thread {
    private final SNMPMonitorServer server;
    private final String assetId;
    private final SNMPConfig snmpConfig;
    private final int taskCycle;
    private final List<String> snmpGetOIDList = new ArrayList<>();
    private final List<String> snmpWalkOIDList = new ArrayList<>();
    private final Map<String, CommonOID> measureValues = new HashMap<>();           //<measureName, CommonOID>
    private boolean running = true;

    public SNMPMonitorThread(String assetId, SNMPConfig snmpConfig, int taskCycle) {
        server = SNMPMonitorServer.getInstance();
        this.assetId = assetId;
        this.snmpConfig = snmpConfig;
        this.taskCycle = taskCycle;
        assembleOids();
    }

    private void assembleOids() {
        if (server != null && server.getDeviceMIBMap() != null) {
            snmpGetOIDList.clear();
            snmpWalkOIDList.clear();
            for (DeviceMIB mib : server.getDeviceMIBMap().values()) {
                if (mib != null) {
                    if (mib.getSnmpMethod().equals("snmpget")) {
                        snmpGetOIDList.add(mib.getOID());
                    } else if (mib.getSnmpMethod().equals("snmpwalk")) {
                        snmpWalkOIDList.add(mib.getOID());
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                snmpGet();
                snmpWalk();
                sleep(taskCycle);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void isRunning(boolean running) {
        this.running = running;
    }

    public boolean running() {
        return running;
    }

    private void snmpGet() {
        try {
            if (snmpGetOIDList.size() > 0) {
//                log.info("snmpGetOIDList: {}", Arrays.toString(new List[]{snmpGetOIDList}));
                List<String> result = Shell.snmpget(snmpConfig.getVersion(), snmpConfig.getCommunity(), snmpConfig.getOutput(), snmpConfig.getIPAddress(), snmpGetOIDList);
                parseData(result);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void snmpWalk() {
        try {
            if (snmpWalkOIDList.size() > 0) {
//                log.info("snmpWalkOIDList: {}", Arrays.toString(new List[]{snmpWalkOIDList}));
                List<String> result = Shell.snmpwalk(snmpConfig.getVersion(), snmpConfig.getCommunity(), snmpConfig.getOutput(), snmpConfig.getIPAddress(), snmpWalkOIDList);
                parseData(result);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<String> parseResponse(String response) {
        List<String> res = new ArrayList<>();
        try {
            int equalsPos = response.indexOf("=");
            if (equalsPos > 0 && response.length() > equalsPos + 1) {
                res.add(response.substring(0, equalsPos).trim());
                String value = response.substring(equalsPos + 1).trim();
                int colonPos = value.indexOf(":");
                if (colonPos > 0 && value.length() > colonPos + 1) {
                    res.add(value.substring(0, colonPos).trim().toUpperCase());
                    res.add(value.substring(colonPos + 1).trim());
                } else {
                    System.out.println("snmp return exception:\n" + response);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    /**
     * 将snmp Shell命令返回的响应字符串处理为易读信息
     *
     * @param result responseStr List
     */
    public void parseData(List<String> result) {
        //log.info("result : {}", Arrays.toString(new List[]{result}));
        try {
            if (result != null && result.size() > 0) {
                for (String s : result) {
                    List<String> responseStr = strToList(s);
                    for (String response : responseStr) {
                        List<String> res = parseResponse(response);
                        if (res.size() == 3 && server.getCommonOIDMap() != null) {
                            CommonOID oid = server.getCommonOID(res.get(0));
                            if (oid != null && oid.getDataType().equals(res.get(1))) {
                                CommonOID val = new CommonOID();
                                val.setOid(oid.getOid());
                                val.setDesc(oid.getDesc());
                                val.setDataType(oid.getDataType());
                                val.setName(oid.getName());
                                val.setMeasureName(oid.getMeasureName());
                                switch (val.getDataType()) {
                                    case "INTEGER":
                                    case "INTEGER32":
                                    case "UINTEGER32":
                                        String temp = res.get(2);
                                        String value;
                                        String unit;
                                        if (temp.contains(" ")) {
                                            value = temp.substring(0, temp.indexOf(" ")).trim();
                                            unit = temp.substring(temp.indexOf(" ") + 1).trim();
                                        } else if (temp.contains("(") && temp.contains(")")) {
                                            value = temp.substring(temp.indexOf("(") + 1, temp.indexOf("")).trim();
                                            unit = temp.substring(0, temp.indexOf("(")).trim();
                                        } else {
                                            value = temp;
                                            unit = "";
                                        }
                                        val.setValue(value);
                                        val.setUnit(unit);
                                        break;
                                    default:
                                        val.setValue(res.get(2));
                                        val.setUnit("");
                                        break;
                                }
                                log.info("measureName: {}, value:{}", val.getMeasureName(), val.getValue());
                                measureValues.put(val.getMeasureName(), val);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<String> strToList(String response) {
        String[] split = response.split(",");
        return Arrays.asList(split);
    }

    public Map<String, CommonOID> getMeasureValues() {
        return measureValues;
    }

    public CommonOID getMeasureValue(String measureName) {
        return measureValues.get(measureName);
    }

    public String getAssetId() {
        return assetId;
    }
}
