package com.gem.loganalysis.config;

import com.gem.loganalysis.gsaclient.GSAClientAgent;
import com.gem.loganalysis.gsaclient.GSACommand;
import com.gem.loganalysis.gsaclient.GVMScanReport;
import com.gem.loganalysis.gsaclient.GVMScanTask;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Queue;

@Service
public class GSAClientAgentService {
    private GSAClientAgent instance;

    public GSAClientAgentService() {
        this.instance = GSAClientAgent.getInstance();
        this.instance.init();
    }

    // 封装 GSAClientAgent 的方法

    //获取风险统计
    public JSONObject getAggregateForVulnBySeverity() {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getAggregateForVulnBySeverity();
    }

    //获取主机脆弱性
    public JSONObject getHostsSeverity() {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getHostsSeverity();
    }

    //根据reportId获取指定扫描报告
    public GVMScanReport getGVMScanReport(String reportId) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getGVMScanReport(reportId);
    }

    //获取按照漏洞严重性进行聚合的结果
    public JSONObject getAggregateForResultBySeverity() {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getAggregateForResultBySeverity();
    }

    //获得网络安全设备脆弱性数据
    public JSONObject getNetSecurityDeviceSeverities(){
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getNetSecurityDeviceSeverities();
    }

    //createHost
    public String createHost(String ip, String comment) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.createHost(ip,comment);
    }

    //deleteHost
    public boolean deleteHost(String host) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.deleteHost(host);
    }

    //importHosts
    public void importHosts(Map<String, String> hosts) {
        // 调用 GSAClientAgent 的方法并返回结果
        instance.importHosts(hosts);
    }

    //getHosts
    public String getHosts() {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getHosts();
    }

    //createTarget
    public String createTarget(String host) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.createTarget(host);
    }

    //createTarget
    public boolean deleteTarget(String host) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.deleteTarget(host);
    }

    //createTarget
    public String getTargets() {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getTargets();
    }

    //createTarget
    public String getConfigs() {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getConfigs();
    }

    //getPortLists
    public String getPortLists() {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getPortLists();
    }

    //getPortLists
    public String createTask(String host, String targetId) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.createTask(host,targetId);
    }

    //getPortLists
    public boolean deleteTask(String taskId) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.deleteTask(taskId);
    }

    //getTasks
    public String getTasks() {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getTasks();
    }

    //getTask
    public JSONObject getTask(String taskId) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getTask(taskId);
    }

    //getGVMScanTask
    public GVMScanTask getGVMScanTask(String taskId) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getGVMScanTask(taskId);
    }

    //getReport
    public JSONObject getReport(String reportId) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getReport(reportId);
    }

    //executeTask
    public boolean executeTask(String taskId) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.executeTask(taskId);
    }

    //executeTasks
    public void executeTasks(List<String> taskList) {
        // 调用 GSAClientAgent 的方法并返回结果
         instance.executeTasks(taskList);
    }

    //getGVMAssetIdByIp
    public String getGVMAssetIdByIp(String ip) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getGVMAssetIdByIp(ip);
    }

    //getGVMTargetIdByIp
    public String getGVMTargetIdByIp(String ip) {
        // 调用 GSAClientAgent 的方法并返回结果
       return instance.getGVMTargetIdByIp(ip);
    }

    //getGVMTaskIdByIp
    public String getGVMTaskIdByIp(String ip) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getGVMTaskIdByIp(ip);
    }

    //getTaskProgress
    public int getTaskProgress(String taskId) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getTaskProgress(taskId);
    }

    //getAggregateForBusiTask
    public int getAggregateForBusiTask() {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getAggregateForBusiTask();
    }

    //getAggregateForBusiTask
    public Queue<GSACommand> getCommandQueue() {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getCommandQueue();
    }

    //getAggregateForBusiTask
    public Map<String, GVMScanTask> getTaskMap() {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getTaskMap();
    }

    //executeTaskByHostIP
    public boolean executeTaskByHostIP(String hostIP) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.executeTaskByHostIP(hostIP);
    }

    //getTaskProgressByHostIP
    public String getTaskProgressByHostIP(String hostIP) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getTaskProgressByHostIP(hostIP);
    }

    //getReportByHostIP
    public JSONObject getReportByHostIP(String hostIP) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getReportByHostIP(hostIP);
    }

    //getReportByHostIP
    public JSONObject getTaskByHostIP(String hostIP) {
        // 调用 GSAClientAgent 的方法并返回结果
        return instance.getTaskByHostIP(hostIP);
    }
}
