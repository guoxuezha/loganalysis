package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.vo.HomeOverviewVO;
import com.gem.loganalysis.model.vo.ImportRespVO;
import com.gem.loganalysis.model.vo.ScreeShowVO;
import com.gem.loganalysis.model.vo.asset.*;
import org.json.JSONException;

import java.util.List;

/**
 * 安全管理资产 Service 接口
 *
 * @author czw
 */
public interface IAssetService extends IService<Asset> {

    /**
     * 新增或更新安全管理资产
     *
     * @param dto
     * @return
     */
    Result<String> editAsset(AssetDTO dto);

    /**
     * 分页查询安全管理资产
     *
     * @param dto
     * @return
     */
    PageResponse<AssetRespVO> getPageList(PageRequest<AssetQueryDTO> dto) throws JSONException;

    AssetRespVO getAsset(String id);

    AssetAccountRespVO getAssetAccount(String id);

    /**
     * 资产总览
     *
     * @return
     */
    AssetOverviewVO getOverviewInfo() throws JSONException;

    List<AssetRespVO> getAssetList(AssetQueryDTO dto) throws JSONException;

    /**
     * 导入逻辑资产
     *
     * @param list
     * @return
     */
    ImportRespVO importLogicalExcel(List<LogicalAssetExcelVO> list);

    /**
     * 导入物理资产
     *
     * @param list
     * @return
     */
    ImportRespVO importPhysicalExcel(List<PhysicalAssetExcelVO> list);

    /**
     * 首页
     *
     * @return
     */
    HomeOverviewVO getHomeOverview() throws JSONException;

    /**
     * 大屏展示
     *
     * @return 展示内容
     */
    ScreeShowVO screenShow();

}
