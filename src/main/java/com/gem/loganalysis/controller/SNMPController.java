package com.gem.loganalysis.controller;

import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.bo.SNMPCollectInfo;
import com.gem.loganalysis.model.vo.asset.ITAssetMonitorInfoVO;
import com.gem.loganalysis.snmpmonitor.SNMPMonitorServer;
import com.gem.loganalysis.snmpmonitor.SNMPMonitorThread;
import com.gem.loganalysis.snmpmonitor.SNMPMonitorThreadPool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 与SNMP相关的所有接口
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/19 22:49
 */
@Api(tags = "SNMP模块")
@RestController
@RequestMapping("/sop/snmp")
@AllArgsConstructor
public class SNMPController {

    public static void main(String[] args) {
        List<String> result = new ArrayList<>();
        result.add(".1.3.6.1.2.1.25.2.3.1.1.1 = INTEGER: 1, .1.3.6.1.2.1.25.2.3.1.1.3 = INTEGER: 3, .1.3.6.1.2.1.25.2.3.1.1.6 = INTEGER: 6, .1.3.6.1.2.1.25.2.3.1.1.7 = INTEGER: 7, .1.3.6.1.2.1.25.2.3.1.1.8 = INTEGER: 8, .1.3.6.1.2.1.25.2.3.1.1.10 = INTEGER: 10, .1.3.6.1.2.1.25.2.3.1.1.35 = INTEGER: 35, .1.3.6.1.2.1.25.2.3.1.1.37 = INTEGER: 37, .1.3.6.1.2.1.25.2.3.1.1.38 = INTEGER: 38, .1.3.6.1.2.1.25.2.3.1.1.52 = INTEGER: 52, .1.3.6.1.2.1.25.2.3.1.1.63 = INTEGER: 63, .1.3.6.1.2.1.25.2.3.1.2.1 = OID: .1.3.6.1.2.1.25.2.1.2, .1.3.6.1.2.1.25.2.3.1.2.3 = OID: .1.3.6.1.2.1.25.2.1.3, .1.3.6.1.2.1.25.2.3.1.2.6 = OID: .1.3.6.1.2.1.25.2.1.1, .1.3.6.1.2.1.25.2.3.1.2.7 = OID: .1.3.6.1.2.1.25.2.1.1, .1.3.6.1.2.1.25.2.3.1.2.8 = OID: .1.3.6.1.2.1.25.2.1.1, .1.3.6.1.2.1.25.2.3.1.2.10 = OID: .1.3.6.1.2.1.25.2.1.3, .1.3.6.1.2.1.25.2.3.1.2.35 = OID: .1.3.6.1.2.1.25.2.1.4, .1.3.6.1.2.1.25.2.3.1.2.37 = OID: .1.3.6.1.2.1.25.2.1.4, .1.3.6.1.2.1.25.2.3.1.2.38 = OID: .1.3.6.1.2.1.25.2.1.4, .1.3.6.1.2.1.25.2.3.1.2.52 = OID: .1.3.6.1.2.1.25.2.1.4, .1.3.6.1.2.1.25.2.3.1.2.63 = OID: .1.3.6.1.2.1.25.2.1.4, .1.3.6.1.2.1.25.2.3.1.3.1 = STRING: Physical memory, .1.3.6.1.2.1.25.2.3.1.3.3 = STRING: Virtual memory, .1.3.6.1.2.1.25.2.3.1.3.6 = STRING: Memory buffers, .1.3.6.1.2.1.25.2.3.1.3.7 = STRING: Cached memory, .1.3.6.1.2.1.25.2.3.1.3.8 = STRING: Shared memory, .1.3.6.1.2.1.25.2.3.1.3.10 = STRING: Swap space, .1.3.6.1.2.1.25.2.3.1.3.35 = STRING: /dev/shm, .1.3.6.1.2.1.25.2.3.1.3.37 = STRING: /run, .1.3.6.1.2.1.25.2.3.1.3.38 = STRING: /sys/fs/cgroup, .1.3.6.1.2.1.25.2.3.1.3.52 = STRING: /, .1.3.6.1.2.1.25.2.3.1.3.63 = STRING: /run/user/0, .1.3.6.1.2.1.25.2.3.1.4.1 = INTEGER: 1024 Bytes, .1.3.6.1.2.1.25.2.3.1.4.3 = INTEGER: 1024 Bytes, .1.3.6.1.2.1.25.2.3.1.4.6 = INTEGER: 1024 Bytes, .1.3.6.1.2.1.25.2.3.1.4.7 = INTEGER: 1024 Bytes, .1.3.6.1.2.1.25.2.3.1.4.8 = INTEGER: 1024 Bytes, .1.3.6.1.2.1.25.2.3.1.4.10 = INTEGER: 1024 Bytes, .1.3.6.1.2.1.25.2.3.1.4.35 = INTEGER: 4096 Bytes, .1.3.6.1.2.1.25.2.3.1.4.37 = INTEGER: 4096 Bytes, .1.3.6.1.2.1.25.2.3.1.4.38 = INTEGER: 4096 Bytes, .1.3.6.1.2.1.25.2.3.1.4.52 = INTEGER: 4096 Bytes, .1.3.6.1.2.1.25.2.3.1.4.63 = INTEGER: 4096 Bytes, .1.3.6.1.2.1.25.2.3.1.5.1 = INTEGER: 3879792, .1.3.6.1.2.1.25.2.3.1.5.3 = INTEGER: 3879792, .1.3.6.1.2.1.25.2.3.1.5.6 = INTEGER: 3879792, .1.3.6.1.2.1.25.2.3.1.5.7 = INTEGER: 777548, .1.3.6.1.2.1.25.2.3.1.5.8 = INTEGER: 131636, .1.3.6.1.2.1.25.2.3.1.5.10 = INTEGER: 0, .1.3.6.1.2.1.25.2.3.1.5.35 = INTEGER: 484974, .1.3.6.1.2.1.25.2.3.1.5.37 = INTEGER: 484974, .1.3.6.1.2.1.25.2.3.1.5.38 = INTEGER: 484974, .1.3.6.1.2.1.25.2.3.1.5.52 = INTEGER: 10288184, .1.3.6.1.2.1.25.2.3.1.5.63 = INTEGER: 96995, .1.3.6.1.2.1.25.2.3.1.6.1 = INTEGER: 3734408, .1.3.6.1.2.1.25.2.3.1.6.3 = INTEGER: 3734408, .1.3.6.1.2.1.25.2.3.1.6.6 = INTEGER: 136988, .1.3.6.1.2.1.25.2.3.1.6.7 = INTEGER: 777548, .1.3.6.1.2.1.25.2.3.1.6.8 = INTEGER: 131636, .1.3.6.1.2.1.25.2.3.1.6.10 = INTEGER: 0, .1.3.6.1.2.1.25.2.3.1.6.35 = INTEGER: 0, .1.3.6.1.2.1.25.2.3.1.6.37 = INTEGER: 32908, .1.3.6.1.2.1.25.2.3.1.6.38 = INTEGER: 0, .1.3.6.1.2.1.25.2.3.1.6.52 = INTEGER: 1396251, .1.3.6.1.2.1.25.2.3.1.6.63 = INTEGER: 0");
        String s = result.get(0);
        String[] split = s.split(",");
        for (String s1 : split) {
            System.out.println(s1);
        }
    }

    @ApiOperation("测试接口")
    @PostMapping("/test")
    public Result<Object> test() {
        List<String> result = new ArrayList<>();
        String response = ".1.3.6.1.4.1.2021.11.11.0 = INTEGER: 99, .1.3.6.1.4.1.2021.11.10.0 = INTEGER: 0";
        result.add(response);
        SNMPMonitorServer instance = SNMPMonitorServer.getInstance();
        SNMPMonitorThreadPool threadPool = instance.getThreadPool();
        Map<String, SNMPMonitorThread> pool = threadPool.getThreadPool();
        for (SNMPMonitorThread thread : pool.values()) {
            thread.parseData(result);
        }
        return Result.ok(instance.getAllCommonOIDValues());
    }

    @ApiOperation("刷新SNMP相关内存对象")
    @PostMapping("/refreshSnmpConfig")
    public Result<Object> refreshSnmpConfig() {
        SNMPMonitorServer instance = SNMPMonitorServer.getInstance();
        instance.loadBaseInfo();
        instance.createThreadPool();
        return Result.ok();
    }

    @ApiOperation("获取IT设备监听信息")
    @PostMapping("/getIpAssetMonitorInfo")
    public Result<List<ITAssetMonitorInfoVO>> getIpAssetMonitorInfo() {
        return Result.ok(SNMPCollectInfo.getInstance().getITLoadInfoList());
    }

    @ApiOperation("获取近两小时出口设备负荷")
    @PostMapping("/getOutPutAssetLoad")
    public Result<ArrayList<SNMPCollectInfo.NetIOUnit>> getOutPutAssetLoad() {
        return Result.ok(SNMPCollectInfo.getInstance().getAllOutLoad());
    }

}
