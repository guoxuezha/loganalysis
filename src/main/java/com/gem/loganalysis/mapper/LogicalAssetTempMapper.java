package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.dto.asset.LogicalAssetQueryDTO;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.model.vo.asset.LogicalAssetScannerRespVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 逻辑资产扫描结果 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Mapper
public interface LogicalAssetTempMapper extends BaseMapper<LogicalAssetTemp> {

    List<LogicalAssetScannerRespVO> getLogicalAssetList(LogicalAssetQueryDTO dto);
}
