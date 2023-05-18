package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupQueryDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.AssetGroup;
import com.gem.loganalysis.model.vo.asset.AssetGroupRespVO;

import java.util.List;

/**
 * 安全管理资产分组 Service 接口
 *
 * @author czw
 */
public interface IAssetGroupService extends IService<AssetGroup> {

    /**
     * 创建/编辑资产分组
     * @param dto
     * @return
     */
    Result<String> editGroup(AssetGroupDTO dto);
    /**
     * 资产分组分页
     * @param dto
     * @return
     */
    Page<AssetGroupRespVO> getPageList(PageRequest<AssetGroupQueryDTO> dto);

    /**
     * 资产分组列表
     * @param dto
     * @return
     */
    List<AssetGroupRespVO> getList(AssetGroupQueryDTO dto);


    AssetGroupRespVO getAssetGroup(String id);
}
