package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.dto.query.LogContentQueryDTO;
import com.gem.loganalysis.model.entity.AssetMergeLog;

import java.util.List;

/**
 * <p>
 * 归并日志内容 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-10
 */
public interface IAssetMergeLogService extends IService<AssetMergeLog> {


    /**
     * 从文件中读取原始日志信息
     *
     * @param dto 查询参数
     * @return 日志记录
     */
    List<String> getSourceLog(LogContentQueryDTO dto);

}
