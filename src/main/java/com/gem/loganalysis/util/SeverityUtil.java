package com.gem.loganalysis.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gem.loganalysis.model.vo.HostSeverityVO;
import com.gem.loganalysis.model.vo.HostVO;

import java.io.InputStream;

public  class SeverityUtil {


    public static void main(String[] args) {
        try {
            // 使用ClassLoader加载资源文件
            InputStream inputStream = SeverityUtil.class.getClassLoader().getResourceAsStream("hostSeverity.json");

            // 将InputStream转换为字符串
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, bytesRead));
            }
            String jsonString = stringBuilder.toString();



            ObjectMapper mapper = new ObjectMapper();
            HostSeverityVO dataStructure = mapper.readValue(jsonString, HostSeverityVO.class);

            // 处理解析后的数据结构对象

            // 示例输出
            System.out.println("Severity: " + dataStructure.getSeverity());
            System.out.println("Number of hosts: " + dataStructure.getHosts().size());
            for (HostVO host : dataStructure.getHosts()) {
                System.out.println("Host IP: " + host.getIp());
                System.out.println("Host Comment: " + host.getComment());
                System.out.println("Host Severity: " + host.getSeverity());
            }

            // 关闭输入流
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


