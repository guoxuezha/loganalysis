package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupQueryDTO;
import com.gem.loganalysis.model.entity.AssetGroup;
import com.gem.loganalysis.model.entity.DailyData;
import com.gem.loganalysis.model.vo.asset.AssetGroupRespVO;

import java.util.List;

/**
 * 每日数据 Service 接口
 *
 * @author czw
 */
public interface IDailyDataService extends IService<DailyData> {


}
