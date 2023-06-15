package com.gem.loganalysis.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class UserUtil {

    public static String getLoginUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return  request.getHeader("userId");
    }

    public static String getLoginUserOrgId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return  request.getHeader("orgId");
    }

    //获得用户权限的ID，就是如果是管理员权限，会返回null，就不会拼接进SQL，如果是普通权限，则会返回自己的userId
    public static String getAuthorityUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String roleIdList = request.getHeader("roleIdList");
        if(StringUtils.isBlank(roleIdList)){
            return null;
        }
        List<String> roleIds = Arrays.asList(roleIdList.split(","));
        if (roleIds.isEmpty() || !roleIds.contains("1")) {
           return getLoginUserId();
        } else {
           return null;
        }
    }

}
