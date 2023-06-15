package com.gem.loganalysis.sshserver;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.kafka.TempConsumerRunnable;
import com.gem.loganalysis.kafka.TempConsumerRunnablePool;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.util.SpringContextUtil;
import com.gem.utils.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.State.NEW;

/**
 * 临时消费者
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/15 16:37
 */
@ServerEndpoint("/tempConsumer/{handshake}")
@Component
@Slf4j
public class KafkaTempConsumerWebSocket extends WebSocket {

    public KafkaTempConsumerWebSocket() {
        super();
        this.setDecryptKey("SKVNMEIHJLOANSKI");
    }

    @Override
    public String paramsParse(String arg0) {
        TempConsumerRunnablePool tempConsumerRunnablePool = SpringContextUtil.getBean(TempConsumerRunnablePool.class);
        log.info("arg0: {}", arg0);
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(arg0);
        String assetId = (String) jsonObject.get("assetId");
        ArrayList<HashMap<String, String>> dataSet = new DAO().getDataSet(BaseConstant.DEFAULT_POOL_NAME, "SELECT IP_ADDRESS FROM SOP_ASSET WHERE ASSET_ID = '" + assetId + "' LIMIT 1");
        if (CollUtil.isNotEmpty(dataSet)) {
            String ipAddress = dataSet.get(0).get("IP_ADDRESS");
            String topicName = BaseConstant.CHILD_TOPIC_PREFIX + ipAddress;
            TempConsumerRunnable tempConsumerRunnable = tempConsumerRunnablePool.get(topicName, this);
            if (tempConsumerRunnable == null) {
                return "不存在目标Topic!";
            }
            Thread.State state = tempConsumerRunnable.getState();
            if (NEW.equals(state)) {
                tempConsumerRunnable.start();
            }
            return "订阅成功!";
        }
        return "不存在目标资产!";
    }

    @Override
    public boolean verifyToken(String s, String s1, String s2) {
        return true;
    }

    @Override
    public void trigger(String message) {

    }

}
