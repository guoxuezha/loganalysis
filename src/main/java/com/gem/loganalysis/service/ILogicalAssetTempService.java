package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.asset.LogicalAssetQueryDTO;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.model.vo.LogicalScannerVO;

import java.util.List;

/**
 * <p>
 * 逻辑资产扫描结果 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
public interface ILogicalAssetTempService extends IService<LogicalAssetTemp> {

    LogicalScannerVO getLogicalAssetList(LogicalAssetQueryDTO dto);
}
