package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.model.entity.DailyData;
import com.gem.loganalysis.model.vo.NetworkEquipmentVO;
import com.gem.loganalysis.model.vo.RiskOverviewRecordVO;
import com.gem.loganalysis.model.vo.TerminalEquipmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 每日数据 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-10
 */
@Mapper
public interface DailyDataMapper extends BaseMapper<DailyData> {


}
