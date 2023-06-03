package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.model.dto.BlockExecuteDTO;
import com.gem.utils.net.SSHAgent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 封堵执行器
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/1 14:16
 */
@Slf4j
public class BlockExecutor {

    public static void main(String[] args) {
        BlockExecuteDTO dto = new BlockExecuteDTO("123.249.94.31", 22, "root", "Gc6209933", "123.249.42.58", null);
//        System.out.println("block result: " + block(dto));
        deBlock(dto);
    }

    public static Boolean block(HashMap<String, String> info) {
        String operationAssetId = info.get("OPERATION_ASSET_ID");
        if (StrUtil.isEmpty(operationAssetId)) {
            log.warn("执行封堵的设备ID不得为空!");
            return false;
        }
        DAO dao = new DAO();
        StringBuilder querySql = new StringBuilder("SELECT IP_ADDRESS, NM_PORT, NM_ACCOUNT, NM_PASSWORD FROM SOP_ASSET WHERE ASSET_ID IN (");
        for (String assetId : operationAssetId.trim().split(",")) {
            querySql.append("'").append(assetId).append("',");
            saveBlockRecord(assetId, info.get("EVENT_ID"), info.get("SOURCE_IP"), info.get("BLOCK_TYPE"), info.get("BLOCK_DURATION"));
        }
        querySql.delete(querySql.length() - 1, querySql.length())
                .append(")");
        ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql.toString(), 0, 0);
        if (CollUtil.isNotEmpty(dataSet)) {
            for (HashMap<String, String> map : dataSet) {
                BlockExecuteDTO executeDTO = new BlockExecuteDTO(map.get("IP_ADDRESS"), Integer.parseInt(map.get("NM_PORT")), map.get("NM_ACCOUNT"), map.get("NM_PASSWORD"), map.get("SOURCE_IP"), null);
                block(executeDTO);
            }
        }
        return true;
    }

    /**
     * 执行封堵
     *
     * @param dto 参数
     * @return 命令执行结果
     */
    private static String block(BlockExecuteDTO dto) {
        SSHAgent sshAgent = new SSHAgent(dto.getOperationHost(), dto.getOperationPort(), dto.getUserName(), dto.getPassword(), null);
        String lineNumStr = searchBlockRuleLineNum(sshAgent, dto.getBlockIp());
        if (lineNumStr == null) {
            String blockCommand = String.format("iptables -t filter -A INPUT -s %s -j REJECT", dto.getBlockIp());
            return sshAgent.exec(blockCommand);
        } else {
            log.warn("目标IP : {} 在设备 {} 上已执行过封堵,不可重复操作!", dto.getBlockIp(), dto.getOperationHost());
            return null;
        }
    }

    /**
     * 执行解封
     *
     * @param dto 参数
     * @return 命令执行结果
     */
    public static void deBlock(BlockExecuteDTO dto) {
        // 先查询是否存在REJECT记录
        SSHAgent sshAgent = new SSHAgent(dto.getOperationHost(), dto.getOperationPort(), dto.getUserName(), dto.getPassword(), null);
        String lineNumStr = searchBlockRuleLineNum(sshAgent, dto.getBlockIp());
        if (StrUtil.isNotEmpty(lineNumStr)) {
            String deBlockCommand = String.format("iptables -t filter -D INPUT %s", lineNumStr);
            System.out.println("deBlock result: " + sshAgent.exec(deBlockCommand));
        } else {
            log.info("未配置对该IP的REJECT规则,跳过解封!");
        }
    }

    /**
     * 查询已有的INPUT - filter - REJECT规则列表
     *
     * @param sshAgent SSH代理对象
     * @param blockIp  封堵目标IP
     * @return 封堵目标IP已有REJECT规则的行号
     */
    private static String searchBlockRuleLineNum(SSHAgent sshAgent, String blockIp) {
        String searchCommand = "iptables -nL INPUT -t filter --line";
        String result = sshAgent.exec(searchCommand);
        String[] records = result.split("\\n");
        Map<String, String> ruleLineNumMap = new HashMap<>();
        for (int i = 2; i < records.length; i++) {
            ArrayList<String> items = new ArrayList<>();
            for (String item : records[i].split(" ")) {
                if (StrUtil.isNotEmpty(item)) {
                    items.add(item);
                }
            }
            if ("REJECT".equals(items.get(1))) {
                ruleLineNumMap.put(items.get(4), items.get(0));
            }
        }
        return ruleLineNumMap.get(blockIp);
    }

    /**
     * 新增封堵记录
     *
     * @param assetId       执行封堵操作的资产ID
     * @param eventId       触发封堵的事件ID
     * @param blockIp       被封堵的目标IP
     * @param blockType     封堵类型(手动/自动)
     * @param blockDuration 封堵时长(用于计算解封时间)
     */
    private static void saveBlockRecord(String assetId, String eventId, String blockIp, String blockType, String blockDuration) {
        StringBuilder insertSql = new StringBuilder("INSERT INTO `SOP_BLOCK_RECORD` (`BLOCK_RECORD_ID`, `BLOCK_IP`, `BLOCK_TYPE`, `BLOCK_MODE`, `BLOCK_OPERATOR`, `BLOCK_BEGIN_TIME`, `BLOCK_END_TIME`, `BLOCK_STATE`, `ASSET_ID`, `EVENT_ID`) VALUES (");
        DateTime endTime = DateUtil.offset(new Date(), DateField.MINUTE, Integer.parseInt(blockDuration));
        insertSql.append("'").append(IdUtil.fastSimpleUUID()).append("',")
                .append("'").append(blockIp).append("',")
                .append("'").append(blockType).append("',")
                .append("0,")
                .append("'system',")
                .append("'").append(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN)).append("',")
                .append("'").append(DateUtil.format(endTime, DatePattern.PURE_DATETIME_PATTERN)).append("',")
                .append("1,")
                .append("'").append(assetId).append("',")
                .append("'").append(eventId).append("')");
        new DAO().execCommand(BaseConstant.DEFAULT_POOL_NAME, insertSql.toString());
    }


}
