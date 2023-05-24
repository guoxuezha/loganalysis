package com.gem.loganalysis.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.GetDTO;
import com.gem.loganalysis.model.dto.query.EventQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.dto.query.LogContentQueryDTO;
import com.gem.loganalysis.model.dto.query.RiskOverviewQueryDTO;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.model.entity.AssetMergeLog;
import com.gem.loganalysis.service.IAssetEventService;
import com.gem.loganalysis.service.IAssetMergeLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.gem.loganalysis.util.CustomDateFormatUtil.format;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/22 14:58
 */
@Api(tags = "事件管理")
@RestController
@RequestMapping("/sop/assetEvent")
@AllArgsConstructor
@Slf4j
public class AssetEventController {

    private final IAssetEventService iAssetEventService;

    private final IAssetMergeLogService iAssetMergeLogService;

    /**
     * 总览
     *
     * @param dto 查询参数
     * @return 返回对象
     */
    @PostMapping("/overview")
    @ApiOperation("事件总览")
    public Result<Object> overview(@RequestBody RiskOverviewQueryDTO dto) {
        return Result.ok(iAssetEventService.geOverviewInfo(dto));
    }

    /**
     * 条件分页查询事件
     *
     * @param dto 入参对象
     * @return 事件列表
     */
    @PostMapping("/pageList")
    @ApiOperation("条件分页查询事件")
    public Result<Object> pageList(@RequestBody PageRequest<EventQueryDTO> dto) {
        Page<AssetEvent> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        EventQueryDTO data = dto.getData();
        LambdaQueryWrapperX<AssetEvent> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eqIfPresent(AssetEvent::getAssetId, data.getAssetId())
                .eqIfPresent(AssetEvent::getHandleStatus, data.getHandleStatus())
                .eqIfPresent(AssetEvent::getEventOrigin, data.getEventOrigin())
                .eqIfPresent(AssetEvent::getEventType, data.getEventType())
                .eqIfPresent(AssetEvent::getEventClass, data.getEventClass())
                .eqIfPresent(AssetEvent::getSourceIp, data.getSourceIp())
                .geIfPresent(AssetEvent::getBeginTime, format(data.getBeginTime()))
                .leIfPresent(AssetEvent::getBeginTime, format(data.getEndTime()));
        return Result.ok(iAssetEventService.page(page, wrapperX));
    }

    /**
     * 查询触发事件的归并日志信息
     *
     * @param dto 归并日志ID
     * @return 归并日志内容
     */
    @PostMapping("/getOriginLog")
    @ApiOperation("查询触发事件的归并日志信息")
    public Result<Object> getOriginLog(@RequestBody GetDTO dto) {
        return Result.ok(iAssetMergeLogService.getById(dto.getId()));
    }

    /**
     * 查询与事件相关的原始日志信息
     *
     * @param dto 归并日志ID
     * @return 原始日志记录
     */
    @PostMapping("/getSourceLog")
    @ApiOperation("查询与事件相关的原始日志信息")
    public Result<Object> getSourceLog(@RequestBody GetDTO dto) {
        AssetMergeLog mergeLog = iAssetMergeLogService.getById(dto.getId());
        List<String> result = iAssetMergeLogService.getSourceLog(new LogContentQueryDTO(mergeLog.getRuleRelaId(), mergeLog.getUpdateTime(), mergeLog.getLogId()));
        return Result.ok(result);
    }

}
