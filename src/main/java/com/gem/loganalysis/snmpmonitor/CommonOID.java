package com.gem.loganalysis.snmpmonitor;

/*
 * ex.
 * oid=".1.3.6.1.2.1.1.1.0"
 * name="sysDescr"
 * desc="A textual description of the entity. This value should include the full name and version identification of the system's hardware type,
 * software operating-system, and networking software. It is mandatory that this only contain printable ASCII characters."
 * dataType="STRING"
 * value=""   <SNMP调用后返回值，如："Linux yisu-6468b83770c4d 3.10.0-862.14.4.el7.x86_64 #1 SMP Wed Sep 26 15:12:11 UTC 2018 x86_64">
 * measureName="系统描述"
 */
public class CommonOID {
    private String oid = null;
    private String name = null;
    private String desc = null;
    private String dataType = null;
    private String value = null;
    private String unit = null;
    private String measureName = null;
    private String mibVersion = null;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMeasureName() {
        return measureName;
    }

    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    public String getMibVersion() {
        return mibVersion;
    }

    public void setMibVersion(String mibVersion) {
        this.mibVersion = mibVersion;
    }


}
