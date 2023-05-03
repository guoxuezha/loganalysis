package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.OrganizationMapper;
import com.gem.loganalysis.model.entity.Organization;
import com.gem.loganalysis.service.IOrganizationService;
import org.springframework.stereotype.Service;

/**
 * 组织机构 服务实现类
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

}
