package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.dto.asset.PhysicalAssetQueryDTO;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;
import com.gem.loganalysis.model.vo.asset.PhysicalAssetScannerRespVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * IP设备扫描结果 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Mapper
public interface PhysicalAssetTempMapper extends BaseMapper<PhysicalAssetTemp> {

    List<PhysicalAssetScannerRespVO> getPhysicalAssetList(PhysicalAssetQueryDTO dto);
}
