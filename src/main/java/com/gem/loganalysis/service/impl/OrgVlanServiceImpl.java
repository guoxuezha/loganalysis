package com.gem.loganalysis.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.convert.AssetConvert;
import com.gem.loganalysis.mapper.LogicalAssetTempMapper;
import com.gem.loganalysis.mapper.OrgVlanMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.asset.*;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.AssetGroup;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.model.entity.OrgVlan;
import com.gem.loganalysis.model.vo.asset.OrgVlanRespVO;
import com.gem.loganalysis.service.ILogicalAssetTempService;
import com.gem.loganalysis.service.IOrgVlanService;
import com.gem.loganalysis.util.JsonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * vlan配置 服务实现类
 * </p>
 *
 * @author czw
 * @since 2023-05-12
 */
@Service
@DS("slave")
public class OrgVlanServiceImpl extends ServiceImpl<OrgVlanMapper, OrgVlan> implements IOrgVlanService {
    @Resource
    private OrgVlanMapper orgVlanMapper;


    @Override
    public Page<OrgVlanRespVO> getVlanPage(PageRequest<OrgVlanQueryDTO> dto) {
        Page<OrgVlan> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        OrgVlanQueryDTO data = dto.getData();
        LambdaQueryWrapperX<OrgVlan> wrapper = new LambdaQueryWrapperX<OrgVlan>()
                .eqIfPresent(OrgVlan::getOrgId, data.getOrgId());
        Page<OrgVlan> resp = this.page(page, wrapper);
        Page<OrgVlanRespVO> orgVlanRespVOPage = AssetConvert.INSTANCE.convertPage05(resp);
        List<OrgVlanRespVO> records = orgVlanRespVOPage.getRecords();
        records.forEach(e->{
            //TODO 先写死 等有部门表了放入
            if(e.getOrgId().equals("1")){
                e.setOrgName("资产管理部");
            }else if(e.getOrgId().equals("2")){
                e.setOrgName("客户运营部");
            }
            e.setVlanList(JsonUtils.parseArray(e.getVlan(),VlanDTO.class));
        });
        return orgVlanRespVOPage;
    }

    @Override
    public boolean editVlan(OrgVlanDTO dto) {
        String s = JsonUtils.toJsonString(dto.getVlanList());//转成JSON格式放入数据库
        return this.saveOrUpdate(new OrgVlan().setVlan(s).setOrgId(dto.getOrgId()));
    }

    @Override
    public List<OrgVlanRespVO> getVlanList(OrgVlanQueryDTO dto) {
        LambdaQueryWrapperX<OrgVlan> wrapper = new LambdaQueryWrapperX<OrgVlan>()
                .eqIfPresent(OrgVlan::getOrgId, dto.getOrgId());
        List<OrgVlan> list = this.list(wrapper);
        List<OrgVlanRespVO> orgVlanRespVOPage = AssetConvert.INSTANCE.convertList05(list);
        orgVlanRespVOPage.forEach(e->{
            //TODO 先写死 等有部门表了放入
            if(e.getOrgId().equals("1")){
                e.setOrgName("资产管理部");
            }else if(e.getOrgId().equals("2")){
                e.setOrgName("客户运营部");
            }
            e.setVlanList(JsonUtils.parseArray(e.getVlan(),VlanDTO.class));
        });
        return orgVlanRespVOPage;
    }
}
