package com.gem.loganalysis.sshserver;

import cn.hutool.core.collection.CollUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.util.AESUtil;
import com.gem.utils.json.JsonFunc;
import com.gem.utils.net.SSHAgent;
import com.gem.utils.net.SSHShell;
import com.gem.utils.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;

@ServerEndpoint("/websocket/{handshake}")
@Component
@Slf4j
public class SSHAgentWebSocket extends WebSocket {
    SSHAgent sshAgent = null;
    SSHShell sshShell = null;

    public SSHAgentWebSocket() {
        super();
        this.setDecryptKey("SKVNMEIHJLOANSKI");
        System.out.println("SSHAgentWebSocket=================================================>");
    }

    /*WebSocket建立时执行*/
    @Override
    public String paramsParse(String arg0) {
        System.out.println("arg0 = " + arg0);
        try {
            JSONObject params = JsonFunc.toJSONObject(arg0);
            if (params != null) {
                String type = params.getString("type");
                String host = params.getString("host");
                String action = params.getString("action");
                if (type.equals("ssh") && action.equals("login")) {
                    Asset asset = getAsset(this.getUserId(), host);
                    //String host, int port, String userName, String password, String webSocketSessionId
                    sshAgent = new SSHAgent(host, asset.getNmPort(), asset.getNmAccount(), asset.getNmPassword(), this.getSessionId());
                    sshShell = sshAgent.openSSHShell();
                    if (sshAgent.isConnected()) {
                        System.out.println("WebSocket connected: userId = " + this.getUserId() + ", host = " + host + ", sessionId = " + this.getSessionId());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /*收到消息时执行*/
    @Override
    public void trigger(String message) {
        // TODO Auto-generated method stub
        if (sshShell != null && message != null) {
            sshShell.writeToTerm(message);
        }
    }

    @Override
    public boolean verifyToken(String arg0, String arg1, String arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    //验证host的访问权限
    private Asset getAsset(String userId, String host) {
        String querySql = String.format("SELECT IP_ADDRESS, NM_PORT, NM_ACCOUNT, NM_PASSWORD FROM SOP_ASSET WHERE IP_ADDRESS = '%s' AND ASSET_MANAGER = '%s' LIMIT 1", host, userId);
        ArrayList<HashMap<String, String>> dataSet = new DAO().getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql, 1, 1);
        if (CollUtil.isEmpty(dataSet)) {
            log.warn("未查询到当前用户管理的目标设备!");
            return null;
        }
        HashMap<String, String> assetInfo = dataSet.get(0);
        Asset asset = new Asset();
        asset.setIpAddress(host);
        asset.setAssetManager(userId);
        asset.setNmAccount(assetInfo.get("NM_ACCOUNT"));
        asset.setNmPassword(AESUtil.decrypt(assetInfo.get("NM_PASSWORD")));
        asset.setNmPort(22);
        return asset;

        /*try {
            if (SNMPMonitorServer.getInstance() != null) {
                for (Asset asset : SNMPMonitorServer.getInstance().getAssetMap().values()) {
                    if (asset != null && asset.getIpAddress().equals(host) && asset.getAssetManager().equals(userId)) {
                        return asset;
                    }
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }*/
    }

}
