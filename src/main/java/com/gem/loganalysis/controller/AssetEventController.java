package com.gem.loganalysis.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.GetDTO;
import com.gem.loganalysis.model.dto.edit.EventTypeInsertDTO;
import com.gem.loganalysis.model.dto.edit.EventTypeUpdateDTO;
import com.gem.loganalysis.model.dto.query.EventQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.dto.query.LogContentQueryDTO;
import com.gem.loganalysis.model.dto.query.OverviewQueryDTO;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.model.entity.AssetMergeLog;
import com.gem.loganalysis.model.entity.EventType;
import com.gem.loganalysis.model.vo.EventMonitorVO;
import com.gem.loganalysis.model.vo.EventOverviewVO;
import com.gem.loganalysis.service.IAssetEventService;
import com.gem.loganalysis.service.IAssetMergeLogService;
import com.gem.loganalysis.service.IEventTypeService;
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
@RequestMapping("/sop")
@AllArgsConstructor
@Slf4j
public class AssetEventController {

    private final IEventTypeService iEventTypeService;

    private final IAssetEventService iAssetEventService;

    private final IAssetMergeLogService iAssetMergeLogService;

    /**
     * 总览
     *
     * @param dto 查询参数
     * @return 返回对象
     */
    @PostMapping("/assetEvent/overview")
    @ApiOperation("事件总览")
    public Result<EventOverviewVO> overview(@RequestBody OverviewQueryDTO dto) {
        return Result.ok(iAssetEventService.geOverviewInfo(dto));
    }


    /**
     * 事件监控
     *
     * @param dto 查询参数
     * @return 返回对象
     */
    @PostMapping("/assetEvent/eventMonitor")
    @ApiOperation("事件监控")
    public Result<EventMonitorVO> eventMonitor() {
        return Result.ok(iAssetEventService.getEventMonitor());
    }

    @PostMapping("/eventType/pageList")
    @ApiOperation("分页查询事件类型")
    public Result<Page<EventType>> eventTypePageList(@RequestBody PageRequest<String> dto) {
        Page<EventType> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapperX<EventType> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eqIfPresent(EventType::getBlockRuleId, dto.getData());
        return Result.ok(iEventTypeService.page(page, wrapperX));
    }

    @PostMapping("/eventType/insert")
    @ApiOperation("新增事件类型")
    public Result<String> eventTypeInsert(@RequestBody EventTypeInsertDTO dto) {
        boolean result = iEventTypeService.save(new EventType(dto));
        return result ? Result.ok("新增成功!") : Result.failed("新增失败!");
    }

    @PostMapping("/eventType/update")
    @ApiOperation("修改事件类型")
    public Result<String> eventTypeUpdate(@RequestBody EventTypeUpdateDTO dto) {
        LambdaQueryWrapper<EventType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EventType::getEventType, dto.getEventTypeOld())
                .eq(EventType::getEventClass, dto.getEventClassOld())
                .eq(EventType::getBlockRuleId, dto.getBlockRuleIdOld());
        boolean result = iEventTypeService.update(new EventType(dto), wrapper);
        return result ? Result.ok("修改成功!") : Result.failed("修改失败!");
    }

    @PostMapping("/eventType/delete")
    @ApiOperation("删除事件类型")
    public Result<String> delete(@RequestBody EventTypeInsertDTO dto) {
        LambdaQueryWrapper<EventType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EventType::getEventType, dto.getEventType())
                .eq(EventType::getEventClass, dto.getEventClass())
                .eq(EventType::getBlockRuleId, dto.getBlockRuleId());
        boolean result = iEventTypeService.remove(wrapper);
        return result ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    /**
     * 条件分页查询事件
     *
     * @param dto 入参对象
     * @return 事件列表
     */
    @PostMapping("/assetEvent/pageList")
    @ApiOperation("条件分页查询事件")
    public Result<Page<AssetEvent>> eventPageList(@RequestBody PageRequest<EventQueryDTO> dto) {
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
    @PostMapping("/assetEvent/getOriginLog")
    @ApiOperation("查询触发事件的归并日志信息")
    public Result<AssetMergeLog> getOriginLog(@RequestBody GetDTO dto) {
        return Result.ok(iAssetMergeLogService.getById(dto.getId()));
    }

    /**
     * 查询与事件相关的原始日志信息
     *
     * @param dto 归并日志ID
     * @return 原始日志记录
     */
    @PostMapping("/assetEvent/getSourceLog")
    @ApiOperation("查询与事件相关的原始日志信息")
    public Result<List<String>> getSourceLog(@RequestBody GetDTO dto) {
        AssetMergeLog mergeLog = iAssetMergeLogService.getById(dto.getId());
        List<String> result = iAssetMergeLogService.getSourceLog(new LogContentQueryDTO(mergeLog.getRuleRelaId(), mergeLog.getUpdateTime(), mergeLog.getLogId()));
        return Result.ok(result);
    }

}
