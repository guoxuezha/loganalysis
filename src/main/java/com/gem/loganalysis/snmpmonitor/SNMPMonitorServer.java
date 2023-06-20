package com.gem.loganalysis.snmpmonitor;

import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.entity.Asset;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Based MIB get Devices information and running state by SNMP.
 *
 * @author James King
 */

@Service
public class SNMPMonitorServer {
    private static final int TIMEOUT = 10000;
    private static final int WAIT_INTERVAL = 1000;
    private static final int taskCycle = 60000;
    private static final String defaultPoolName = "sop";

    private static final Logger logger = Logger.getLogger("Scanner");
    private static SNMPMonitorServer instance = null;
    private Map<String, Asset> assets = null;                          // <assetId, Asset>
    private Map<String, SNMPConfig> configs = null;                    // <assetId, SNMPConfig>
    private Map<String, DeviceMIB> mibs = null;                        // <oid, DeviceMIB>
    private Map<String, CommonOID> oids = null;                        // <oid, CommonOID>
    private SNMPMonitorThreadPool threadPool = null;

    private SNMPMonitorServer() {
        loadBaseInfo();
    }

    public static SNMPMonitorServer getInstance() {
        if (instance == null) {
            instance = new SNMPMonitorServer();
        }
        return instance;
    }

    public void loadBaseInfo() {
        if (assets != null) {
            assets.clear();
        }
        if (configs != null) {
            configs.clear();
        }
        if (mibs != null) {
            mibs.clear();
        }
        if (oids != null) {
            oids.clear();
        }
        loadAssets();
        loadSNMPConfig();
        loadDeviceMIB();
        loadCommonOID();
//        createThreadPool();
    }

    /**
     * 加载物理资产列表，获取物理资产的编号、分组号、IP地址和MIB版本号（默认为COMMON）
     */
    private void loadAssets() {
        if (assets == null) {
            assets = new HashMap<>();
        }
        try {
            String SQL = "SELECT * FROM SOP_ASSET WHERE ASSET_CLASS = '1'";
            DAO dao = new DAO();
            ArrayList<HashMap<String, String>> result = dao.getDataSet(defaultPoolName, SQL, 0, 0);
            if (result != null) {
                for (HashMap<String, String> record : result) {
                    Asset asset = new Asset();
                    asset.setAssetId(record.get("ASSET_ID"));
                    asset.setAssetGroupId(record.get("ASSET_GROUP_ID"));
                    asset.setIpAddress(record.get("IP_ADDRESS"));
                    asset.setMibVersion(record.get("MIB_VERSION"));
                    assets.put(asset.getAssetId(), asset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 加载物理设备的SNMP参数
     */
    private void loadSNMPConfig() {
        if (configs == null) {
            configs = new HashMap<>();
        }
        try {
            String SQL = "SELECT * FROM SOP_ASSET_SNMP_CONFIG";
            DAO dao = new DAO();
            ArrayList<HashMap<String, String>> result = dao.getDataSet(defaultPoolName, SQL, 0, 0);
            if (result != null) {
                for (HashMap<String, String> record : result) {
                    SNMPConfig config = new SNMPConfig();
                    config.setAssetId(record.get("ASSET_ID"));
                    config.setAssetOrg(record.get("ASSET_ORG"));
                    config.setIPAddress(record.get("IP_ADDRESS"));
                    config.setVersion(record.get("SNMP_VERSION"));
                    config.setCommunity(record.get("SNMP_COMMUNITY"));
                    config.setUserName(record.get("SNMP_USERNAME"));
                    config.setAuthPassword(record.get("AUTH_PASSWORD"));
                    config.setPrivPassword(record.get("PRIV_PASSWORD"));
                    config.setOutput(record.get("OUTPUT_PARAM"));
                    configs.put(config.getAssetId(), config);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 加载每个物理设备要执行查询的MIB
     */
    private void loadDeviceMIB() {
        if (mibs == null) {
            mibs = new HashMap<>();
        }
        try {
            String SQL = "SELECT * FROM SOP_MIB_CONFIG";
            DAO dao = new DAO();
            ArrayList<HashMap<String, String>> result = dao.getDataSet(defaultPoolName, SQL, 0, 0);
            if (result != null) {
                for (HashMap<String, String> record : result) {
                    DeviceMIB mib = new DeviceMIB();
                    mib.setMibVersion(record.get("MIB_VERSION"));
                    mib.setOID(record.get("OID"));
                    mib.setSnmpMethod(record.get("SNMP_METHOD"));
                    mib.setMeasureType(record.get("MEASURE_TYPE"));
                    mibs.put(mib.getOID(), mib);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 加载OID的列表
     */
    private void loadCommonOID() {
        if (oids == null) {
            oids = new HashMap<>();
        }
        try {
            String SQL = "SELECT * FROM SOP_COMMON_OID";
            DAO dao = new DAO();
            ArrayList<HashMap<String, String>> result = dao.getDataSet(defaultPoolName, SQL, 0, 0);
            if (result != null) {
                for (HashMap<String, String> record : result) {
                    CommonOID oid = new CommonOID();
                    oid.setMibVersion(record.get("MIB_VERSION"));
                    oid.setOid(record.get("OID"));
                    oid.setName(record.get("OID_NAME"));
                    oid.setDataType(record.get("OID_DATATYPE"));
                    oid.setDesc(record.get("OID_DESC"));
                    oid.setMeasureName(record.get("MEASURE_NAME"));
                    oids.put(oid.getOid(), oid);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void createThreadPool() {
        try {
            if (threadPool != null) {
                threadPool.stop();
                int duration = 0;
                while (threadPool.running()) {
                    Thread.sleep(WAIT_INTERVAL);
                    duration += WAIT_INTERVAL;
                    if (duration >= TIMEOUT) {
                        break;
                    }
                }
                threadPool.destroy();
            }
            if (configs != null && configs.size() > 0) {
                threadPool = new SNMPMonitorThreadPool();
                threadPool.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String addAsset(Asset asset) {
        if (assets == null) {
            assets = new HashMap<>();
        }
        if (asset != null && assets.get(asset.getAssetId()) == null) {
            assets.put(asset.getAssetId(), asset);
            return asset.getAssetId();
        } else {
            return null;
        }
    }

    public String addMIB(DeviceMIB mib) {
        if (mibs == null) {
            mibs = new HashMap<>();
        }
        if (mib != null && mibs.get(mib.getMibVersion()) == null) {
            mibs.put(mib.getMibVersion(), mib);
            return mib.getMibVersion();
        } else {
            return null;
        }
    }

    public SNMPMonitorThreadPool getThreadPool() {
        return threadPool;
    }

    public Map<String, Asset> getAssetMap() {
        return assets;
    }

    public Map<String, DeviceMIB> getDeviceMIBMap() {
        return mibs;
    }

    public Map<String, SNMPConfig> getSNMPConfigMap() {
        return configs;
    }

    public Map<String, CommonOID> getCommonOIDMap() {
        return oids;
    }


    public SNMPConfig getSNMPConfig(String assetId) {
        if (configs == null || configs.size() == 0) {
            return null;
        } else {
            return configs.get(assetId);
        }
    }

    public int getTaskCycle() {
        return taskCycle;
    }

    public Map<String, Map<String, CommonOID>> getAllCommonOIDValues() {
        if (threadPool == null || threadPool.getThreadPool() == null) {
            return null;
        }
        Map<String, Map<String, CommonOID>> result = new HashMap<>();
        for (SNMPMonitorThread thread : threadPool.getThreadPool().values()) {
            result.put(thread.getAssetId(), thread.getMeasureValues());
        }
        return result;
    }

    public Map<String, CommonOID> getCommonOIDValues(String assetId) {
        if (threadPool == null || threadPool.getThread(assetId) == null) {
            return null;
        }
        return threadPool.getThread(assetId).getMeasureValues();
    }

    public String defaultPoolName() {
        return defaultPoolName;
    }

    public void addOID(CommonOID oid) {
        if (oids != null && oid != null && oids.get(oid.getOid()) == null) {
            oids.put(oid.getOid(), oid);
        }
    }

    public CommonOID removeOID(String oid) {
        if (oids != null && oid != null && oids.get(oid) != null) {
            return oids.remove(oid);
        } else {
            return null;
        }
    }

    public CommonOID getCommonOID(String oid) {
        if (!oid.startsWith(".")) {
            oid = "." + oid;
        }
        if (oids != null) {
            return oids.get(oid);
        } else {
            return null;
        }
    }

}
