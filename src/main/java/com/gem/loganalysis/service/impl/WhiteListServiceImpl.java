package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.convert.WhiteBlackListConvert;
import com.gem.loganalysis.exception.ServiceException;
import com.gem.loganalysis.mapper.BlackListMapper;
import com.gem.loganalysis.mapper.WhiteListMapper;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.dto.BlackWhiteListDeleteDTO;
import com.gem.loganalysis.model.dto.edit.BlackWhiteListDTO;
import com.gem.loganalysis.model.dto.query.BlackWhiteListQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.BlackList;
import com.gem.loganalysis.model.entity.WhiteList;
import com.gem.loganalysis.model.vo.BlackWhiteListVO;
import com.gem.loganalysis.service.IWhiteListService;
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
 * 白名单 服务实现类
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Service
public class WhiteListServiceImpl extends ServiceImpl<WhiteListMapper, WhiteList> implements IWhiteListService {

    @Resource
    private BlackListMapper blackListMapper;
    @Resource
    private WhiteListMapper whiteListMapper;

    @Override
    public boolean createWhiteList(BlackWhiteListDTO dto) {
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
        long count = this.count(new LambdaQueryWrapperX<WhiteList>()
                .eqIfPresent(WhiteList::getAssetId, dto.getAssetId())
                .eqIfPresent(WhiteList::getIpAddress, dto.getIpAddress())
                .eqIfPresent(WhiteList::getOrgId, dto.getOrgId()));
        if(count>0){
            throw new ServiceException("该IP已加入该资产的白名单，请勿重复添加");
        }
        //若用户在黑名单,解除该用户的黑名单
        List<BlackList> blackLists = blackListMapper.selectList(new LambdaQueryWrapperX<BlackList>()
                .eqIfPresent(BlackList::getAssetId, dto.getAssetId())
                .eqIfPresent(BlackList::getIpAddress, dto.getIpAddress())
                .eqIfPresent(BlackList::getOrgId, dto.getOrgId()));
        if(blackLists.size()>0){
            blackLists.forEach(e->{
                blackListMapper.deleteBlackList(new BlackWhiteListDeleteDTO()
                        .setAssetId(dto.getAssetId()).setIpAddress(dto.getIpAddress()).setOrgId(dto.getOrgId()));
            });
        }
        return this.save(WhiteBlackListConvert.INSTANCE.convert01(dto));
    }

    @Override
    public boolean updateWhiteList(BlackWhiteListDTO dto) {
        if(StringUtils.isBlank(dto.getOrgId())){
            throw new ServiceException("组织机构唯一编码不能为空");
        }
        long count = this.count(new LambdaQueryWrapperX<WhiteList>()
                .eqIfPresent(WhiteList::getAssetId, dto.getAssetId())
                .eqIfPresent(WhiteList::getIpAddress, dto.getIpAddress())
                .eqIfPresent(WhiteList::getOrgId, dto.getOrgId()));
        if(count==0){
            throw new ServiceException("该白名单不存在");
        }
        return whiteListMapper.updateWhiteList(dto);
    }

    @Override
    public boolean deleteWhiteList(BlackWhiteListDeleteDTO dto) {
        long count = this.count(new LambdaQueryWrapperX<WhiteList>()
                .eqIfPresent(WhiteList::getAssetId, dto.getAssetId())
                .eqIfPresent(WhiteList::getIpAddress, dto.getIpAddress())
                .eqIfPresent(WhiteList::getOrgId, dto.getOrgId()));
        if(count==0){
            throw new ServiceException("该白名单不存在");
        }
        return whiteListMapper.deleteWhiteList(dto);
    }

    @Override
    public List<BlackWhiteListVO> whiteList(BlackWhiteListQueryDTO dto) {
        return whiteListMapper.whiteList(dto);
    }

    @Override
    public PageInfo<BlackWhiteListVO> whiteListPage(PageRequest<BlackWhiteListQueryDTO> dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<BlackWhiteListVO> list = whiteListMapper.whiteList(dto.getData());
        return new PageInfo<>(list);
    }

}
