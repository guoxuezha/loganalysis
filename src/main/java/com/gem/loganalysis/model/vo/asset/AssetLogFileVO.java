package com.gem.loganalysis.model.vo.asset;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 资产相关原始日志文件信息
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/11 22:06
 */
@Data
@AllArgsConstructor
public class AssetLogFileVO {

    /**
     * 安全等级
     */
    private String severity;

    /**
     * 输出子系统
     */
    private String facility;

    /**
     * 文件日期
     */
    private String fileDate;

    /**
     * 文件名称
     */
    private String fileName;

}
