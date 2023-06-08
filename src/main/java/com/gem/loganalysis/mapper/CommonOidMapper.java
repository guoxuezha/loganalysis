package com.gem.loganalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gem.loganalysis.model.dto.edit.CommonOidBaseDTO;
import com.gem.loganalysis.model.dto.edit.CommonOidDTO;
import com.gem.loganalysis.model.dto.query.CommonOidQueryDTO;
import com.gem.loganalysis.model.entity.CommonOid;
import com.gem.loganalysis.model.vo.asset.CommonOidRespVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 通用MIB库OID列表设置 Mapper 接口
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@Mapper
public interface CommonOidMapper extends BaseMapper<CommonOid> {

    /**
     * 获得单个MIB库OID详情
     * @param dto
     * @return
     */
    CommonOidRespVO getOid(CommonOidBaseDTO dto);

    /**
     * 新增OID
     * @param dto
     * @return
     */
    int insertOid(CommonOidDTO dto);

    /**
     * 删除OID
     * @param dto
     * @return
     */
    int deleteOid(CommonOidBaseDTO dto);

    /**
     * 更新OID
     * @param dto
     * @return
     */
    int updateOid(CommonOidDTO dto);

    /**
     * 通用MIB库OID列表
     * @param dto
     * @return
     */
    List<CommonOidRespVO> getList(CommonOidQueryDTO dto);
}
