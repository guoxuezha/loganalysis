package com.gem.loganalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.convert.WhiteBlackListConvert;
import com.gem.loganalysis.exception.ServiceException;
import com.gem.loganalysis.mapper.BlackListMapper;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.BlackWhiteListDeleteDTO;
import com.gem.loganalysis.model.dto.edit.BlackWhiteListDTO;
import com.gem.loganalysis.model.dto.query.BlackWhiteListQueryDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.entity.BlackList;
import com.gem.loganalysis.model.vo.BlackWhiteListVO;
import com.gem.loganalysis.service.IBlackListService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

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


    @Override
    public Result<String> createBlackList(BlackWhiteListDTO dto) {
        //如果前端没有传入orgId且userinfo里没有orgId，则返回fail
        if(StringUtils.isBlank(dto.getOrgId())&&StringUtils.isBlank(getLoginUserOrgId())){
            return Result.failed("组织机构唯一编码不能为空");
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
            return Result.failed("该IP已封禁该资产，请勿重复添加");
        }
        return this.save(WhiteBlackListConvert.INSTANCE.convert(dto))
                ?Result.ok("新增成功"):Result.failed("新增失败");
    }

    @Override
    public Result<String> updateBlackList(BlackWhiteListDTO dto) {
        if(StringUtils.isBlank(dto.getOrgId())){
            return Result.failed("组织机构唯一编码不能为空");
        }
        long count = this.count(new LambdaQueryWrapperX<BlackList>()
                .eqIfPresent(BlackList::getAssetId, dto.getAssetId())
                .eqIfPresent(BlackList::getIpAddress, dto.getIpAddress())
                .eqIfPresent(BlackList::getOrgId, dto.getOrgId()));
        if(count==0){
            return Result.failed("该黑名单不存在");
        }
        return blackListMapper.updateBlackList(dto)
                ?Result.ok("修改成功"):Result.failed("修改失败");
    }

    @Override
    public Result<String> deleteBlackList(BlackWhiteListDeleteDTO dto) {
        long count = this.count(new LambdaQueryWrapperX<BlackList>()
                .eqIfPresent(BlackList::getAssetId, dto.getAssetId())
                .eqIfPresent(BlackList::getIpAddress, dto.getIpAddress())
                .eqIfPresent(BlackList::getOrgId, dto.getOrgId()));
        if(count==0){
            return Result.failed("该黑名单不存在");
        }
        return blackListMapper.deleteBlackList(dto)
                ?Result.ok("删除成功"):Result.failed("删除失败");
    }

    @Override
    public List<BlackWhiteListVO> blackList(BlackWhiteListQueryDTO dto) {
        return blackListMapper.blackList(dto);
    }
}
