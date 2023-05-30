package com.gem.loganalysis.snmpmonitor;

public class DeviceMIB {
    private String mibVersion = null;                            //mib版本
    private String OID = null;                                    //发送的OID
    private String snmpMethod = null;                            //发送指令
    private String measureType = null;

    public String getMibVersion() {
        return mibVersion;
    }

    public void setMibVersion(String mibVersion) {
        this.mibVersion = mibVersion;
    }

    public String getOID() {
        return OID;
    }

    public void setOID(String oID) {
        OID = oID;
    }

    public String getSnmpMethod() {
        return snmpMethod;
    }

    public void setSnmpMethod(String snmpMethod) {
        this.snmpMethod = snmpMethod;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }


}
