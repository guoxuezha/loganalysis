package com.gem.loganalysis.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
