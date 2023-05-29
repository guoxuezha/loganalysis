package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.convert.WhiteBlackListConvert;
import com.gem.loganalysis.exception.ServiceException;
import com.gem.loganalysis.mapper.BlackListMapper;
import com.gem.loganalysis.mapper.WhiteListMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.PageResponse;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.BlackWhiteListDeleteDTO;
import com.gem.loganalysis.model.dto.edit.BlackWhiteListDTO;
import com.gem.loganalysis.model.dto.query.BlackWhiteListQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.BlackList;
import com.gem.loganalysis.model.entity.WhiteList;
import com.gem.loganalysis.model.vo.BlackWhiteListVO;
import com.gem.loganalysis.service.IBlackListService;
import com.gem.loganalysis.service.IWhiteListService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.regex.Pattern;

import static com.gem.loganalysis.util.UserUtil.getLoginUserOrgId;

/**
 * <p>
 * 黑名单 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList> implements IBlackListService {

    @Resource
    private BlackListMapper blackListMapper;
    @Resource
    private WhiteListMapper whiteListMapper;

    @Override
    public boolean createBlackList(BlackWhiteListDTO dto) {
        //如果前端没有传入orgId且userinfo里没有orgId，则返回fail
        if(StringUtils.isBlank(dto.getOrgId())&&StringUtils.isBlank(getLoginUserOrgId())){
            throw new ServiceException("组织机构唯一编码不能为空");
        }
        if (dto.getIpAddress() != null && !dto.getIpAddress().trim().equals("")) {
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            if (!Pattern.matches(regex, dto.getIpAddress())) {
                //效验IP地址格式是否正确
                throw new ServiceException("请输入正确的IP地址格式");
            }
        }
        //前端没有传入就放入userinfo里的orgId
        if(StringUtils.isBlank(dto.getOrgId())){
            dto.setOrgId(getLoginUserOrgId());
        }
        long count = this.count(new LambdaQueryWrapperX<BlackList>()
                .eqIfPresent(BlackList::getAssetId, dto.getAssetId())
                .eqIfPresent(BlackList::getIpAddress, dto.getIpAddress())
                .eqIfPresent(BlackList::getOrgId, dto.getOrgId()));
        if(count>0){
            throw new ServiceException("该IP已加入该资产的黑名单，请勿重复添加");
        }

        //若用户在白名单,解除该用户的白名单
        List<WhiteList> whiteLists = whiteListMapper.selectList(new LambdaQueryWrapperX<WhiteList>()
                .eqIfPresent(WhiteList::getAssetId, dto.getAssetId())
                .eqIfPresent(WhiteList::getIpAddress, dto.getIpAddress())
                .eqIfPresent(WhiteList::getOrgId, dto.getOrgId()));
        if(whiteLists.size()>0){
            whiteLists.forEach(e->{
                whiteListMapper.deleteWhiteList(new BlackWhiteListDeleteDTO()
                        .setAssetId(dto.getAssetId()).setIpAddress(dto.getIpAddress()).setOrgId(dto.getOrgId()));
            });
        }
        return this.save(WhiteBlackListConvert.INSTANCE.convert(dto));
    }

    @Override
    public boolean updateBlackList(BlackWhiteListDTO dto) {
        if(StringUtils.isBlank(dto.getOrgId())){
            throw new ServiceException("组织机构唯一编码不能为空");
        }
        long count = this.count(new LambdaQueryWrapperX<BlackList>()
                .eqIfPresent(BlackList::getAssetId, dto.getAssetId())
                .eqIfPresent(BlackList::getIpAddress, dto.getIpAddress())
                .eqIfPresent(BlackList::getOrgId, dto.getOrgId()));
        if(count==0){
            throw new ServiceException("该黑名单不存在");
        }
        return blackListMapper.updateBlackList(dto);
    }

    @Override
    public boolean deleteBlackList(BlackWhiteListDeleteDTO dto) {
        long count = this.count(new LambdaQueryWrapperX<BlackList>()
                .eqIfPresent(BlackList::getAssetId, dto.getAssetId())
                .eqIfPresent(BlackList::getIpAddress, dto.getIpAddress())
                .eqIfPresent(BlackList::getOrgId, dto.getOrgId()));
        if(count==0){
            throw new ServiceException("该黑名单不存在");
        }
        return blackListMapper.deleteBlackList(dto);
    }

    @Override
    public List<BlackWhiteListVO> blackList(BlackWhiteListQueryDTO dto) {
        return blackListMapper.blackList(dto);
    }

    @Override
    public PageInfo<BlackWhiteListVO> blackListPage(PageRequest<BlackWhiteListQueryDTO> dto) {
       PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<BlackWhiteListVO> list = blackListMapper.blackList(dto.getData());
        return new PageInfo<>(list);
    }

}
