package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.model.vo.EventMonitorVO;
import com.gem.loganalysis.model.vo.NetworkEquipmentVO;
import com.gem.loganalysis.model.vo.RiskOverviewRecordVO;
import com.gem.loganalysis.model.vo.TerminalEquipmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资产事件记录 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-10
 */
@Mapper
public interface AssetEventMapper extends BaseMapper<AssetEvent> {

    /**
     * 查询风险概览信息
     *
     * @param startTime 起始时间
     * @param endTime   截止时间
     * @return 查询结果
     */
    List<RiskOverviewRecordVO> getOverviewInfo(@Param("startTime") String startTime,
                                               @Param("endTime") String endTime);

    List<NetworkEquipmentVO> getEquipmentList(String assetTypeName);

    List<TerminalEquipmentVO> getEquipmentCount(String assetTypeName);
}
