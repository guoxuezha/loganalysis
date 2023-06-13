package com.gem.loganalysis.controller;

import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.query.OverviewQueryDTO;
import com.gem.loganalysis.service.IAssetRiskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/23 15:46
 */
@Api(tags = "风险管理")
@RestController
@RequestMapping("/sop/assetRisk")
@AllArgsConstructor
@Slf4j
public class AssetRiskController {

    private final IAssetRiskService iAssetRiskService;

    /**
     * 总览
     *
     * @param dto 查询参数
     * @return 返回对象
     */
    @ApiOperation("风险总览")
    @PostMapping("/overview")
    public Result<Object> overview(@RequestBody OverviewQueryDTO dto) {
        return Result.ok(iAssetRiskService.geOverviewInfo(dto));
    }

    /**
     * TODO 目前风险模块没有查询价值,所呈现的信息与事件高度重合
     *
     * @param dto
     * @return
     */
    @ApiOperation("风险分页查询")
    @PostMapping("/pageList")
    public Result<Object> pageList(@RequestBody OverviewQueryDTO dto) {
        return Result.ok(iAssetRiskService.geOverviewInfo(dto));
    }

}
