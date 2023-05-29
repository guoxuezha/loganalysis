package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.dto.edit.SwitchJobStatusDTO;
import com.gem.loganalysis.model.entity.SopJob;

/**
 * <p>
 * 调度任务-工作 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-26
 */
public interface ISopJobService extends IService<SopJob> {

    /**
     * 切换Job的工作状态
     *
     * @param dto 入参
     * @return 结果
     */
    String switchJobStatus(SwitchJobStatusDTO dto);

}
