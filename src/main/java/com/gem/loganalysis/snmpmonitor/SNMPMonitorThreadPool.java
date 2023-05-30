package com.gem.loganalysis.snmpmonitor;

import java.util.HashMap;
import java.util.Map;

public class SNMPMonitorThreadPool {

    private final SNMPMonitorServer server;

    private Map<String, SNMPMonitorThread> threadPool = null;                //<threadId, thread>

    public SNMPMonitorThreadPool() {
        server = SNMPMonitorServer.getInstance();
        if (server != null && (server.getSNMPConfigMap() != null)) {
            if (threadPool == null) {
                threadPool = new HashMap<>();
            }
            for (Map.Entry<String, SNMPConfig> entry : server.getSNMPConfigMap().entrySet()) {
                if (entry != null) {
                    SNMPMonitorThread thread = new SNMPMonitorThread(entry.getKey(), entry.getValue(), server.getTaskCycle());
                    threadPool.put(entry.getKey(), thread);
                }
            }
        }
    }

    public void destroy() {
        stop();
        threadPool = null;
    }

    public void start() {
        if (threadPool != null) {
            for (SNMPMonitorThread thread : threadPool.values()) {
                if (!thread.isAlive()) {
                    thread.start();
                    thread.isRunning(true);
                }
            }
        }
    }

    public void stop() {
        if (threadPool != null) {
            for (SNMPMonitorThread thread : threadPool.values()) {
                thread.isRunning(false);
            }
        }
    }

    public SNMPMonitorThread getThread(String threadId) {
        if (threadPool != null) {
            return threadPool.get(threadId);
        } else {
            return null;
        }
    }

    public boolean running() {
        boolean ret = false;
        for (SNMPMonitorThread thread : threadPool.values()) {
            if (thread.running()) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public void put(String assetId, SNMPConfig snmpConfig) {
        try {
            if (threadPool == null) {
                threadPool = new HashMap<>();
            }
            if (assetId != null && snmpConfig != null && threadPool.get(assetId) == null) {
                SNMPMonitorThread thread = new SNMPMonitorThread(assetId, snmpConfig, server.getTaskCycle());
                threadPool.put(assetId, thread);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void remove(String threadId) {
        try {
            if (threadPool != null && threadId != null) {
                SNMPMonitorThread thread = threadPool.remove(threadId);
                if (thread != null) {
                    thread.isRunning(false);
                    thread.interrupt();
                    thread = null;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getThreadCount() {
        if (threadPool == null) {
            return 0;
        } else {
            return threadPool.size();
        }
    }

    public Map<String, SNMPMonitorThread> getThreadPool() {
        return threadPool;
    }

}
