package com.gem.loganalysis.handler;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Objects;

import static com.gem.loganalysis.util.UserUtil.getLoginUserId;

/**
 * 通用参数填充实现类
 *
 * @author czw
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject)) {
            // 创建时间
            Object createTime = getFieldValByName("createTime", metaObject);
            if (Objects.isNull(createTime)) {
                setFieldValByName("createTime", DateUtil.now(), metaObject);
            }
            // 修改人
            Object creator = getFieldValByName("createBy", metaObject);
            if (Objects.isNull(creator)) {
                setFieldValByName("createBy",getLoginUserId(), metaObject);
            }
            // 更新时间
            Object modifyTime = getFieldValByName("updateTime", metaObject);
            if (Objects.isNull(modifyTime)) {
                setFieldValByName("updateTime", DateUtil.now(), metaObject);
            }
            // 修改人
            Object modifier = getFieldValByName("updateBy", metaObject);
            if (Objects.isNull(modifier)) {
                setFieldValByName("updateBy",getLoginUserId(), metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject)) {
            // 更新时间
            Object modifyTime = getFieldValByName("updateTime", metaObject);
            if (Objects.isNull(modifyTime)) {
                setFieldValByName("updateTime", DateUtil.now(), metaObject);
            }
            // 修改人
            Object modifier = getFieldValByName("updateBy", metaObject);
            if (Objects.isNull(modifier)) {
                setFieldValByName("updateBy",getLoginUserId(), metaObject);
            }
        }
    }
}
