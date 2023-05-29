package com.gem.loganalysis.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.edit.JobDTO;
import com.gem.loganalysis.model.dto.edit.SwitchJobStatusDTO;
import com.gem.loganalysis.model.dto.edit.TaskDTO;
import com.gem.loganalysis.model.dto.query.JobQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.dto.query.TaskQueryDTO;
import com.gem.loganalysis.model.entity.SopJob;
import com.gem.loganalysis.model.entity.SopTask;
import com.gem.loganalysis.service.ISopJobService;
import com.gem.loganalysis.service.ISopTaskService;
import com.gem.utils.schedule.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gem.loganalysis.util.CustomDateFormatUtil.format;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/26 14:43
 */
@Api(tags = "调度任务")
@RestController
@RequestMapping("/sop")
@AllArgsConstructor
public class ScheduleController {

    private final ISopJobService iSopJobService;

    private final ISopTaskService iSopTaskService;

    @ApiOperation("分页查询调度任务信息")
    @PostMapping("/job/pageList")
    public Result<Page<SopJob>> jobPageList(@RequestBody PageRequest<JobQueryDTO> dto) {
        Page<SopJob> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapperX<SopJob> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.likeIfPresent(SopJob::getJobName, dto.getData().getJobName())
                .eqIfPresent(SopJob::getCurrentStatus, dto.getData().getCurrentStatus())
                .geIfPresent(SopJob::getFirstFiredTime, format(dto.getData().getFirstFiredStartTime()))
                .leIfPresent(SopJob::getFirstFiredTime, format(dto.getData().getFirstFiredEndTime()));
        return Result.ok(iSopJobService.page(page, wrapperX));
    }

    @ApiOperation("查看调度器中的Job信息")
    @PostMapping("/scheduleJobList")
    public Result<Object> scheduleJobList() {
        return Result.ok(Schedule.getInstance().jobList);
    }

    @ApiOperation("编辑工作")
    @PostMapping("/job/edit")
    public Result<Object> jobEdit(@RequestBody JobDTO dto) {
        boolean insert = StrUtil.isEmpty(dto.getJobId());
        SopJob sopJob = new SopJob(dto, insert);
        boolean result;
        if (insert) {
            result = iSopJobService.save(sopJob);
        } else {
            result = iSopJobService.updateById(sopJob);
        }
        return result ? Result.ok("编辑成功!") : Result.failed("编辑失败!");
    }

    @ApiOperation("删除工作")
    @PostMapping("/job/delete")
    public Result<String> jobDelete(@RequestBody DeleteDTO dto) {
        boolean result = iSopJobService.removeById(dto.getId());
        return result ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    @ApiOperation("启用/停用调度任务")
    @PostMapping("/job/switchJobStatus")
    public Result<String> updateJobStatus(@RequestBody SwitchJobStatusDTO dto) {
        String message = iSopJobService.switchJobStatus(dto);
        return message == null ? Result.ok() : Result.failed(message);
    }

    @ApiOperation("分页查询任务列表")
    @PostMapping("/task/pageList")
    public Result<Page<SopTask>> taskPageList(@RequestBody PageRequest<TaskQueryDTO> dto) {
        Page<SopTask> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapperX<SopTask> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eqIfPresent(SopTask::getJobId, dto.getData().getJobId())
                .likeIfPresent(SopTask::getTaskName, dto.getData().getTaskName())
                .eqIfPresent(SopTask::getCallMode, dto.getData().getCallMode());
        return Result.ok(iSopTaskService.page(page, wrapperX));
    }

    @ApiOperation("编辑任务")
    @PostMapping("/task/edit")
    public Result<Object> taskEdit(@RequestBody TaskDTO dto) {
        boolean result;
        boolean insert = StrUtil.isEmpty(dto.getTaskId());
        SopTask sopTask = new SopTask(dto, insert);
        if (insert) {
            result = iSopTaskService.save(sopTask);
        } else {
            result = iSopTaskService.updateById(sopTask);
        }
        return result ? Result.ok("编辑成功!") : Result.failed("编辑失败!");
    }

    @ApiOperation("删除")
    @PostMapping("/task/delete")
    public Result<String> taskDelete(@RequestBody DeleteDTO dto) {
        boolean result = iSopTaskService.removeById(dto.getId());
        return result ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }


}
