package com.gem.loganalysis.snmpmonitor;

/**
 * 要监听的SNMP设备配置信息
 */
public class SNMPConfig {
    private String IPAddress = null;
    private String version = null;

    //v1/v2c
    private String community = null;
    //v3
    private String userName = null;
    private String authPassword = null;
    private String privPassword = null;

    private String output = "fn";
    private String assetId = null;
    private String assetOrg = null;

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String iPAddress) {
        IPAddress = iPAddress;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public String getPrivPassword() {
        return privPassword;
    }

    public void setPrivPassword(String privPassword) {
        this.privPassword = privPassword;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetOrg() {
        return assetOrg;
    }

    public void setAssetOrg(String assetOrg) {
        this.assetOrg = assetOrg;
    }


}
