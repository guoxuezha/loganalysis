package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.convert.SnmpConvert;
import com.gem.loganalysis.exception.ServiceException;
import com.gem.loganalysis.mapper.AssetMapper;
import com.gem.loganalysis.mapper.AssetSnmpConfigMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigDTO;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigDeleteDTO;
import com.gem.loganalysis.model.dto.edit.AssetSnmpConfigUpdateDTO;
import com.gem.loganalysis.model.dto.query.AssetSnmpConfigQueryDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.AssetSnmpConfig;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import com.gem.loganalysis.model.vo.asset.AssetSnmpConfigRespVO;
import com.gem.loganalysis.service.IAssetSnmpConfigService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Random;

import static com.gem.loganalysis.util.UserUtil.getLoginUserOrgId;

/**
 * <p>
 * SNMP参数设置 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@Service
public class AssetSnmpConfigServiceImpl extends ServiceImpl<AssetSnmpConfigMapper, AssetSnmpConfig> implements IAssetSnmpConfigService {

    @Resource
    private AssetSnmpConfigMapper snmpConfigMapper;
    @Resource
    private AssetMapper assetMapper;

    @Override
    public int insertConfig(AssetSnmpConfigDTO dto) {
        String loginUserOrgId = getLoginUserOrgId();
        if(StringUtils.isBlank(loginUserOrgId)){
            throw new ServiceException("该用户的部门为空，无法添加");
        }
        //若资产ID不存在，则验证资产存不存在
        if(!StringUtils.isBlank(dto.getAssetId())){
            Asset asset = assetMapper.selectById(dto.getAssetId());
            if(asset==null){
                throw new ServiceException("该资产不存在");
            }
        }
        AssetSnmpConfig convert = SnmpConvert.INSTANCE.convert(dto);
        convert.setAssetOrg(loginUserOrgId);
        return snmpConfigMapper.insertSnmpConfig(convert);
    }

    @Override
    public int deleteConfig(AssetSnmpConfigDeleteDTO dto) {
        //验证设置是否存在
        AssetSnmpConfigRespVO config = getConfig(dto);
        if(config==null){
            throw new ServiceException("要删除的SNMP参数设置不存在，请确认部门ID和IP地址正确");
        }
        return snmpConfigMapper.deleteSnmpConfig(dto);
    }

    @Override
    public int updateConfig(AssetSnmpConfigUpdateDTO dto) {
        //验证设置是否存在
        AssetSnmpConfigRespVO config = getConfig(dto);
        if(config==null){
            throw new ServiceException("要删除的SNMP参数设置不存在，请编辑的部门ID和IP地址正确");
        }
        //若资产ID不存在，则验证资产存不存在
        if(!StringUtils.isBlank(dto.getAssetId())){
            Asset asset = assetMapper.selectById(dto.getAssetId());
            if(asset==null){
                throw new ServiceException("该资产不存在");
            }
        }
        AssetSnmpConfig convert = SnmpConvert.INSTANCE.convert(dto);
        return snmpConfigMapper.updateSnmpConfig(convert);
    }

    @Override
    public AssetSnmpConfigRespVO getConfig(AssetSnmpConfigDeleteDTO dto) {
        return snmpConfigMapper.getSnmpConfig(dto);
    }

    @Override
    public List<AssetSnmpConfigRespVO> getList(AssetSnmpConfigQueryDTO dto) {
        return snmpConfigMapper.getList(dto);
    }

    @Override
    public PageResponse<AssetSnmpConfigRespVO> getPage(PageRequest<AssetSnmpConfigQueryDTO> dto) {
        Page<AssetSnmpConfigRespVO> result = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        snmpConfigMapper.getList(dto.getData());
        return new PageResponse<>(result);
    }


}
