package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.exception.ServiceException;
import com.gem.loganalysis.mapper.MibConfigMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.dto.edit.MibConfigBaseDTO;
import com.gem.loganalysis.model.dto.edit.MibConfigDTO;
import com.gem.loganalysis.model.dto.query.MibConfigQueryDTO;
import com.gem.loganalysis.model.entity.MibConfig;
import com.gem.loganalysis.model.vo.asset.CommonOidRespVO;
import com.gem.loganalysis.model.vo.asset.MibConfigRespVO;
import com.gem.loganalysis.service.IMibConfigService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * MIB指令集设置 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@Service
public class MibConfigServiceImpl extends ServiceImpl<MibConfigMapper, MibConfig> implements IMibConfigService {

    @Resource
    private MibConfigMapper mibConfigMapper;

    @Override
    public int insertConfig(MibConfigDTO dto) {
        MibConfigRespVO config = getConfig(dto);
        if(config!=null){
            throw new ServiceException("该MIB指令集已存在");
        }
        return mibConfigMapper.insertConfig(dto);
    }

    @Override
    public int deleteConfig(MibConfigBaseDTO dto) {
        MibConfigRespVO config = getConfig(dto);
        if(config==null){
            throw new ServiceException("该MIB指令集不存在,请确认传入的OID和MIB版本正确");
        }
        return mibConfigMapper.deleteConfig(dto);
    }

    @Override
    public int updateConfig(MibConfigDTO dto) {
        MibConfigRespVO config = getConfig(dto);
        if(config==null){
            throw new ServiceException("该MIB指令集不存在,请确认传入的OID和MIB版本正确");
        }
        return mibConfigMapper.updateConfig(dto);
    }

    @Override
    public MibConfigRespVO getConfig(MibConfigBaseDTO dto) {
        return mibConfigMapper.getConfig(dto);
    }

    @Override
    public List<MibConfigRespVO> getList(MibConfigQueryDTO dto) {
        return mibConfigMapper.getList(dto);
    }

    @Override
    public PageResponse<MibConfigRespVO> getPage(PageRequest<MibConfigQueryDTO> dto) {
        Page<MibConfigRespVO> result = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        mibConfigMapper.getList(dto.getData());
        return new PageResponse<>(result);
    }
}
