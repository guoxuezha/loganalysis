package com.gem.loganalysis.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.model.PageRequest;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.DeleteDTO;
import com.gem.loganalysis.model.dto.edit.PasswordResetDTO;
import com.gem.loganalysis.model.dto.edit.RoleAllocationDTO;
import com.gem.loganalysis.model.dto.edit.RoleDTO;
import com.gem.loganalysis.model.dto.edit.UserDTO;
import com.gem.loganalysis.model.dto.query.LambdaQueryWrapperX;
import com.gem.loganalysis.model.dto.query.UserQueryDTO;
import com.gem.loganalysis.model.entity.M4SsoPackageRole;
import com.gem.loganalysis.model.entity.M4SsoUser;
import com.gem.loganalysis.model.entity.M4SsoUserRole;
import com.gem.loganalysis.model.vo.UserInfoVO;
import com.gem.loganalysis.service.IM4SsoOrgService;
import com.gem.loganalysis.service.IM4SsoPackageRoleService;
import com.gem.loganalysis.service.IM4SsoUserRoleService;
import com.gem.loganalysis.service.IM4SsoUserService;
import com.gem.loganalysis.util.AESUtil;
import com.gem.utils.crypto.MD5;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/24 14:30
 */

@Api(tags = "用户模块")
@RestController
@RequestMapping("/sop")
@AllArgsConstructor
public class UserController {

    private final IM4SsoUserService im4SsoUserService;

    private final IM4SsoPackageRoleService im4SsoPackageRoleService;

    private final IM4SsoUserRoleService im4SsoUserRoleService;

    private final IM4SsoOrgService im4SsoOrgService;

    private DAO dao;

    @ApiOperation("获取加密结果")
    @PostMapping("/getEncryptStr")
    public String getEncryptStr(@RequestParam("param") String param) {
        return AESUtil.aesEncrypt(param, AESUtil.KEY);
    }

    @ApiOperation("获取解密结果")
    @PostMapping("/getDecryptStr")
    public String getDecryptStr(@RequestParam("param") String param) {
        return AESUtil.aesDecrypt(param, AESUtil.KEY);
    }

    @ApiOperation("用户列表(分页)")
    @PostMapping("/user/pageList")
    public Result<Page<UserInfoVO>> userPageList(@RequestBody PageRequest<UserQueryDTO> dto) {
        Page<UserInfoVO> result = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapperX<M4SsoUser> wrapperX = new LambdaQueryWrapperX<>();
        UserQueryDTO data = dto.getData();
        wrapperX.eqIfPresent(M4SsoUser::getAccount, data.getAccount()).
                eqIfPresent(M4SsoUser::getOrgId, data.getOrgId())
                .eqIfPresent(M4SsoUser::getMobile, data.getMobile())
                .eqIfPresent(M4SsoUser::getEmail, data.getEmail());
        Page<M4SsoUser> userPage = im4SsoUserService.page(new Page<>(dto.getPageNum(), dto.getPageSize()), wrapperX);
        result.setTotal(userPage.getTotal());
        List<UserInfoVO> records = new ArrayList<>();
        for (M4SsoUser record : userPage.getRecords()) {
            records.add(new UserInfoVO(record));
        }
        records.forEach(e -> e.setOrgName(im4SsoOrgService.changeOrgName(e.getOrgId())));//转义部门ID
        result.setRecords(records);
        return Result.ok(result);
    }

    @ApiOperation("编辑用户")
    @PostMapping("/user/edit")
    public Result<String> userEdit(@RequestBody UserDTO dto) {
        boolean insert = false;
        if (StrUtil.isEmpty(dto.getUserId())) {
            insert = true;
            dto.setUserId(IdUtil.fastSimpleUUID());
        }
        M4SsoUser m4SsoUser = new M4SsoUser(dto);
        boolean result = insert ? im4SsoUserService.save(m4SsoUser) : im4SsoUserService.updateById(m4SsoUser);
        if (result) {
            // 用户编辑成功后需要在login表中添加多条记录,以保证该账号可正常登录
            dao.execCommand(BaseConstant.DEFAULT_POOL_NAME, "DELETE FROM M4_SSO_LOGIN WHERE USER_ID = '" + m4SsoUser.getUserId() + "'");
            String insertTemplate = "INSERT INTO M4_SSO_LOGIN(LOGIN_ID, USER_ID) VALUE(?, ?)";
            int[] types = new int[]{Types.VARCHAR, Types.VARCHAR};
            ArrayList<Object[]> records = m4SsoUser.generateLoginInfo();
            dao.execBatch(BaseConstant.DEFAULT_POOL_NAME, insertTemplate, types, records, 10);
        }
        return result ? Result.ok("编辑成功!") : Result.failed("编辑失败!");
    }

    public static void main(String[] args) {
        System.out.println(MD5.encryptPwd("admin", "Ag!Sf3964@_", "GEM#SHA512"));
    }

    @ApiOperation("重置用户密码")
    @PostMapping("/user/resetPwd")
    public Result<Object> resetPwd(@RequestBody PasswordResetDTO dto) {
        M4SsoUser byId = im4SsoUserService.getById(dto.getUserId());
        if (byId != null) {
            String userName = byId.getUserName();
            LambdaQueryWrapper<M4SsoUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(M4SsoUser::getUserId, dto.getUserId())
                    .eq(M4SsoUser::getPassword, MD5.encryptPwd(userName, dto.getOldPassword(), "GEM#SHA512"));
            List<M4SsoUser> list = im4SsoUserService.list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                M4SsoUser m4SsoUser = list.get(0);
                m4SsoUser.setPassword(MD5.encryptPwd(userName, dto.getNewPassword(), "GEM#SHA512"));
                boolean b = im4SsoUserService.updateById(m4SsoUser);
                if (b) {
                    return Result.ok("修改成功!");
                }
            }
        }
        return Result.failed("修改失败!");
    }

    @ApiOperation("删除用户")
    @PostMapping("/user/delete")
    public Result<String> userDelete(@RequestBody DeleteDTO dto) {
        boolean result = im4SsoUserService.removeById(dto.getId());
        return result ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    @ApiOperation("角色列表(分页)")
    @PostMapping("/role/pageList")
    public Result<Page<M4SsoPackageRole>> rolePageList(@RequestBody PageRequest<String> dto) {
        Page<M4SsoPackageRole> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapperX<M4SsoPackageRole> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eqIfPresent(M4SsoPackageRole::getRoleName, dto.getData());
        return Result.ok(im4SsoPackageRoleService.page(page, wrapperX));
    }

    @ApiOperation("编辑角色")
    @PostMapping("/role/edit")
    public Result<String> roleEdit(@RequestBody RoleDTO dto) {
        boolean result;
        boolean insert = StrUtil.isEmpty(dto.getRoleId());
        M4SsoPackageRole packageRole = new M4SsoPackageRole(dto);
        if (insert) {
            result = im4SsoPackageRoleService.save(packageRole);
        } else {
            LambdaQueryWrapper<M4SsoPackageRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(M4SsoPackageRole::getRoleId, dto.getRoleId())
                    .eq(M4SsoPackageRole::getPackageId, "01");
            result = im4SsoPackageRoleService.update(packageRole, wrapper);
        }
        return result ? Result.ok("编辑成功!") : Result.failed("编辑失败!");
    }

    @ApiOperation("删除角色")
    @PostMapping("/role/delete")
    public Result<String> roleDelete(@RequestBody DeleteDTO dto) {
        boolean result = im4SsoPackageRoleService.removeById(dto.getId());
        return result ? Result.ok("删除成功!") : Result.failed("删除失败!");
    }

    @ApiOperation("为用户分配角色")
    @PostMapping("/user/allocationRole")
    public Result<String> allocationRole(@RequestBody RoleAllocationDTO dto) {
        LambdaQueryWrapper<M4SsoUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(M4SsoUserRole::getUserId, dto.getUserId());
        im4SsoUserRoleService.remove(wrapper);
        ArrayList<M4SsoUserRole> userRoles = new ArrayList<>();
        if (CollUtil.isNotEmpty(dto.getRoleIdList())) {
            for (String roleId : dto.getRoleIdList()) {
                userRoles.add(new M4SsoUserRole(dto.getUserId(), roleId));
            }
            boolean result = im4SsoUserRoleService.saveBatch(userRoles);
            return result ? Result.ok("分配成功!") : Result.failed("分配失败!");
        } else {
            return Result.failed("请选择正确的角色信息!");
        }
    }

}
