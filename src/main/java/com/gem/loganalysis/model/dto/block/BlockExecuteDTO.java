package com.gem.loganalysis.model.dto.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * 封堵/解封执行传输类
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/1 15:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockExecuteDTO {

    /**
     * 操作执行IP
     */
    private String operationHost;

    /**
     * 操作执行端口
     */
    private Integer operationPort;

    /**
     * ssh账号
     */
    private String userName;

    /**
     * ssh密码
     */
    private String password;

    /**
     * 要封堵/解封的IP
     */
    private String blockIp;

    /**
     * 要封堵/解封的端口
     */
    private Integer blockPort;

    /**
     * 封堵命令模板
     */
    private String blockCommand;

    /**
     * 解封命令模板
     */
    private String deBlockCommand;

    public BlockExecuteDTO(HashMap<String, String> map, String blockIp) {
        this.operationHost = map.get("IP_ADDRESS");
        this.blockPort = Integer.parseInt(map.get("NM_PORT"));
        this.userName = map.get("NM_ACCOUNT");
        this.password = map.get("NM_PASSWORD");
        this.blockIp = blockIp;
        this.blockCommand = map.get("BLOCK_COMMAND");
        this.deBlockCommand = map.get("DEBLOCK_COMMAND");
    }

}
