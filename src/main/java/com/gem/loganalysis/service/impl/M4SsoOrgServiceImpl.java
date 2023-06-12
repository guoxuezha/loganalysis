package com.gem.loganalysis.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.convert.M4SsoConvert;
import com.gem.loganalysis.mapper.M4SsoOrgMapper;
import com.gem.loganalysis.model.dto.edit.OrgDTO;
import com.gem.loganalysis.model.dto.query.OrgQueryDTO;
import com.gem.loganalysis.model.entity.M4SsoOrg;
import com.gem.loganalysis.model.vo.OrgRespVO;
import com.gem.loganalysis.model.vo.TreeRespVO;
import com.gem.loganalysis.service.IM4SsoOrgService;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织机构 服务实现类
 * </p>
 *
 * @author czw
 * @since 2023-05-23
 */
@Service
public class M4SsoOrgServiceImpl extends ServiceImpl<M4SsoOrgMapper, M4SsoOrg> implements IM4SsoOrgService {

    @Getter
    //部门缓存
    private volatile Map<String, M4SsoOrg> orgCache;

    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<M4SsoOrg> list = this.list();
        // 第二步：构建缓存
        orgCache = new HashMap<>();
        list.forEach(e->{
            orgCache.put(e.getOrgId(),e);
        });
    }

    @Override
    public String changeOrgName(String orgId) {
        if(StringUtils.isBlank(orgId)){
            return "";
        }
        M4SsoOrg m4SsoOrg = orgCache.get(orgId);
        if(m4SsoOrg==null){
            return "";
        }else {
            return m4SsoOrg.getOrgName();
        }
    }

    @Override
    public Map<String, M4SsoOrg> getListByCache() {
        return orgCache;
    }


    @Override
    public boolean editOrg(OrgDTO dto) {
        M4SsoOrg convert = M4SsoConvert.INSTANCE.convert(dto);
        convert.setUpdateTime(DateUtil.format(new Date(),"yyyyMMddHHmmss"));
        boolean b = this.saveOrUpdate(convert);
        initLocalCache();
        return b;
    }

    @Override
    public List<OrgRespVO> OrgList(OrgQueryDTO dto) {
        List<M4SsoOrg> list = this.list();
        //树形结构不方便用数据库查询，因为查到其中一级也要返回上下层级，所以查所有数据之后过滤
        if(!StringUtils.isBlank(dto.getOrgName())){
            List<M4SsoOrg> newList = new ArrayList<>();
            List<M4SsoOrg> collect = list.stream().filter(e -> e.getOrgName().contains(dto.getOrgName())).collect(Collectors.toList());
            collect.forEach(e-> {
                addTreeList(list,newList,e);
            });
            return M4SsoConvert.INSTANCE.buildOrgTree(newList);
        }
        return M4SsoConvert.INSTANCE.buildOrgTree(list);
    }

    @Override
    public List<TreeRespVO> OrgSimpleList() {
        List<M4SsoOrg> list = this.list();
        return M4SsoConvert.INSTANCE.buildOrgTree01(list);
    }

    private List<M4SsoOrg> addTreeList(List<M4SsoOrg> oldList,List<M4SsoOrg> newList,M4SsoOrg org){
        newList.add(org);
        if(!StringUtils.isBlank(org.getParentOrg())&&!org.getParentOrg().equals("0")){
            List<M4SsoOrg> orgList = oldList.stream().filter(m -> m.getOrgId().equals(org.getParentOrg())).collect(Collectors.toList());
            if(orgList.size()>0){
                newList.add(orgList.get(0));
                addTreeList(oldList,newList,orgList.get(0));
            }
        }
        return newList;
    }

}