package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.exception.ServiceException;
import com.gem.loganalysis.mapper.CommonOidMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.dto.edit.CommonOidBaseDTO;
import com.gem.loganalysis.model.dto.edit.CommonOidDTO;
import com.gem.loganalysis.model.dto.query.CommonOidQueryDTO;
import com.gem.loganalysis.model.entity.CommonOid;
import com.gem.loganalysis.model.vo.asset.AssetSnmpConfigRespVO;
import com.gem.loganalysis.model.vo.asset.CommonOidRespVO;
import com.gem.loganalysis.service.ICommonOidService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 通用MIB库OID列表设置 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@Service
public class CommonOidServiceImpl extends ServiceImpl<CommonOidMapper, CommonOid> implements ICommonOidService {

    @Resource
    private CommonOidMapper commonOidMapper;

    @Override
    public int insertOID(CommonOidDTO dto) {
        CommonOidRespVO oid = getOID(dto);
        //验证是否存在
        if(oid!=null){
            throw new ServiceException("该OID已存在");
        }
        return commonOidMapper.insertOid(dto);
    }

    @Override
    public int deleteOID(CommonOidBaseDTO dto) {
        CommonOidRespVO oid = getOID(dto);
        //验证是否存在
        if(oid==null){
            throw new ServiceException("该OID不存在，请确认MIB版本和OID传入正确");
        }
        return commonOidMapper.deleteOid(dto);
    }

    @Override
    public int updateOID(CommonOidDTO dto) {
        CommonOidRespVO oid = getOID(dto);
        //验证是否存在
        if(oid==null){
            throw new ServiceException("该OID不存在，请确认MIB版本和OID传入正确");
        }
        return commonOidMapper.updateOid(dto);
    }

    @Override
    public CommonOidRespVO getOID(CommonOidBaseDTO dto) {
        return commonOidMapper.getOid(dto);
    }

    @Override
    public List<CommonOidRespVO> getList(CommonOidQueryDTO dto) {
        return commonOidMapper.getList(dto);
    }

    @Override
    public PageResponse<CommonOidRespVO> getPage(PageRequest<CommonOidQueryDTO> dto) {
        Page<CommonOidRespVO> result = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        commonOidMapper.getList(dto.getData());
        return new PageResponse<>(result);
    }
}
