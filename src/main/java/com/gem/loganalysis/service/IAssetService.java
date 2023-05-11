package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.BlockOffRecord;

import java.util.*;
import javax.validation.*;

/**
 * 安全管理资产 Service 接口
 *
 * @author czw
 */
public interface IAssetService extends IService<Asset> {

    /**
     * 新增或更新安全管理资产
     * @param dto
     * @return
     */
    Result<String> editAsset(AssetDTO dto);

    /**
     * 分页查询安全管理资产
     * @param dto
     * @return
     */
    Page<Asset> getPageList(PageRequest<AssetQueryDTO> dto);
}
