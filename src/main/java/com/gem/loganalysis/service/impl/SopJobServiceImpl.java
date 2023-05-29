package com.gem.loganalysis.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.SopJobMapper;
import com.gem.loganalysis.mapper.SopTaskMapper;
import com.gem.loganalysis.model.dto.edit.SwitchJobStatusDTO;
import com.gem.loganalysis.model.entity.SopJob;
import com.gem.loganalysis.model.entity.SopTask;
import com.gem.loganalysis.service.ISopJobService;
import com.gem.utils.schedule.Job;
import com.gem.utils.schedule.Schedule;
import com.gem.utils.schedule.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 调度任务-工作 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-26
 */
@Service
@Slf4j
public class SopJobServiceImpl extends ServiceImpl<SopJobMapper, SopJob> implements ISopJobService {

    @Resource
    private SopTaskMapper sopTaskMapper;

    @Override
    public String switchJobStatus(SwitchJobStatusDTO dto) {
        String jobId = dto.getJobId();
        Integer currentStatus = dto.getCurrentStatus();
        // 先判断工作列表中是否已存在该Job
        Schedule instance = Schedule.getInstance();
        boolean jobInMemory = false;
        for (Job job : instance.jobList) {
            if (jobId.equals(job.name)) {
                jobInMemory = true;
                // 有则直接操作内存对象
                job.setStatus(currentStatus);
                break;
            }
        }
        // 否则执行Job创建
        if (!jobInMemory) {
            SopJob sopJob = this.getById(jobId);
            if (sopJob == null) {
                return "未查询到目标Job!";
            }
            Job targetJob = jobBuilder(sopJob, currentStatus);
            if (targetJob == null) {
                return "Job创建失败!";
            }
            instance.appendJob(targetJob);
        }
        // 最后修改数据库中Job信息
        SopJob sopJob = new SopJob();
        sopJob.setJobId(jobId);
        sopJob.setCurrentStatus(currentStatus);
        sopJob.setUpdateTime(DateUtil.now());
        boolean b = this.updateById(sopJob);
        return b ? null : "Job状态修改失败!";
    }

    /**
     * Job对象构建器
     *
     * @param sopJob    job实体类
     * @param jobStatus job工作状态
     * @return 构建结果
     */
    public Job jobBuilder(SopJob sopJob, Integer jobStatus) {
        Job job = new Job();
        job.name = sopJob.getJobId();
        job.executeOnStart = sopJob.getExecuteOnStart() == 1;
        job.firstFiredTime = sopJob.getFirstFiredTime();
        job.lastFiredTime = sopJob.getLastFiredTime();
        job.nextFiredTime = sopJob.getNextFiredTime();
        job.executeOnStart = sopJob.getExecuteOnStart() == 1;
        job.cycleDefine = sopJob.getCycleDefine();
        job.setStatus(jobStatus);

        LambdaQueryWrapper<SopTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SopTask::getJobId, sopJob.getJobId());
        List<SopTask> sopTasks = sopTaskMapper.selectList(wrapper);
        if (CollUtil.isEmpty(sopTasks)) {
            log.warn("未查询到Job关联子任务!");
            return null;
        }
        for (SopTask sopTask : sopTasks) {
            job.appendTask(taskBuilder(sopTask));
        }
        return job;
    }

    /**
     * Task对象构建器
     *
     * @param sopTask Task实体类
     * @return 构建结果
     */
    public Task taskBuilder(SopTask sopTask) {
        Task task = new Task();
        task.key = sopTask.getTaskId();
        task.callMode = sopTask.getCallMode();
        task.className = sopTask.getClassName();
        task.methodName = sopTask.getMethodName();
        return task;
    }

}
