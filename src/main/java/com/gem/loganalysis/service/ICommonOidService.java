package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.dto.edit.CommonOidBaseDTO;
import com.gem.loganalysis.model.dto.edit.CommonOidDTO;
import com.gem.loganalysis.model.dto.query.CommonOidQueryDTO;
import com.gem.loganalysis.model.entity.CommonOid;
import com.gem.loganalysis.model.vo.asset.CommonOidRespVO;

import java.util.List;

/**
 * <p>
 * 通用MIB库OID列表设置 服务类
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
public interface ICommonOidService extends IService<CommonOid> {

    /**
     * 新增通用MIB库OID
     * @param dto
     * @return
     */
    int insertOID(CommonOidDTO dto);

    /**
     * 删除通用MIB库OID
     * @param dto
     * @return
     */
    int deleteOID(CommonOidBaseDTO dto);

    /**
     * 更新通用MIB库OID
     * @param dto
     * @return
     */
    int updateOID(CommonOidDTO dto);

    /**
     * 获得单个MIB库OID详情
     * @param dto
     * @return
     */
    CommonOidRespVO getOID(CommonOidBaseDTO dto);

    /**
     * 获得MIB库OID列表
     * @param dto
     * @return
     */
    List<CommonOidRespVO> getList(CommonOidQueryDTO dto);

    /**
     * 获得MIB库OID分页
     * @param dto
     * @return
     */
    PageResponse<CommonOidRespVO> getPage(PageRequest<CommonOidQueryDTO> dto);
}
