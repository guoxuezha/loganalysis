package com.gem.loganalysis.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.mapper.DictItemMapper;
import com.gem.loganalysis.mapper.DictType2Mapper;
import com.gem.loganalysis.model.dto.query.DictTypeQueryPageDTO;
import com.gem.loganalysis.model.entity.DictItem;
import com.gem.loganalysis.model.entity.DictType2;
import com.gem.loganalysis.service.DictTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 字典类型服务实现层
 *
 * @author czw
 * @since 2023-02-27
 */
@Service
@Slf4j
@AllArgsConstructor
public class DictType2ServiceImpl implements DictTypeService {

    private final DictType2Mapper dictTypeMapper;

    private final DictItemMapper dictItemMapper;

    @Override
    //创建字典类型
    public int createDictType(DictType2 reqVO) {
        //设为开启状态
        reqVO.setStatus(0);
        reqVO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        return dictTypeMapper.insert(reqVO);
    }

    //校验字典类型是否唯一
    @Override
    public String checkDictTypeUnique(DictType2 dict) {
        if(dict.getName()==null){
            return "请传入字典名称";
        }
        if(dict.getCode()==null){
            return "请传入字典编码";
        }
        //判断名称是否重复
        DictType2 dictType = dictTypeMapper.selectOne(new LambdaQueryWrapper<DictType2>()
                .eq(DictType2::getName, dict.getName()));
        if (dictType!=null && !dictType.getId().equals(dict.getId())) {
            return "已经存在该名字的字典类型";
        }
        //判断类型是否重复
        DictType2 dictTypeByName = dictTypeMapper.selectOne(new LambdaQueryWrapper<DictType2>()
                .eq(DictType2::getCode, dict.getCode()));
        if (dictTypeByName!=null && !dictTypeByName.getId().equals(dict.getId())) {
            return "已经存在该类型的字典类型";
        }
        return "正确";
    }

    //校验字典类型是否存在
    @Override
    public String checkDictTypeExist(String id) {
        //判断该字典类型是否存在
        DictType2 dictType = dictTypeMapper.selectById(id);
        if (dictType==null) {
            return "当前字典类型不存在";
        }
        return "正确";
    }

    @Override
    public int updateDictType(DictType2 reqVO) {
        reqVO.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        return dictTypeMapper.updateById(reqVO);
    }

    @Override
    public String deleteCheck(String id) {
        //判断该字典类型是否存在
        DictType2 dictType = dictTypeMapper.selectById(id);
        if (dictType==null) {
            return "当前字典类型不存在";
        }
        //判断类型是否重复
        if (dictItemMapper.selectCount(new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getTypeId, dictType.getId())) > 0) {
            return "无法删除，该字典类型还有字典数据";
        }
        return "正确";
    }

    @Override
    public int deleteDictType(String id) {
        return dictTypeMapper.deleteById(id);
    }


    @Override
    public IPage<DictType2> pageDictTypes(DictTypeQueryPageDTO reqVO) {
        LambdaQueryWrapper<DictType2> lqw = new LambdaQueryWrapper<DictType2>()
                .like(StringUtils.isNotBlank(reqVO.getCode()), DictType2::getCode, reqVO.getCode())
                .like(StringUtils.isNotBlank(reqVO.getName()), DictType2::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DictType2::getStatus, reqVO.getStatus())
                .orderByDesc(DictType2::getId);
        IPage<DictType2> page = new Page<>(reqVO.getCurPage(), reqVO.getPageSize());
        return dictTypeMapper.selectPage(page, lqw);
    }

    @Override
    public DictType2 getDictType(String id) {
        return dictTypeMapper.selectById(id);
    }

    @Override
    public List<DictType2> getDictTypeList() {
        return dictTypeMapper.selectList(null);
    }

    @Override
    public String changeStatus(String id) {
        DictType2 dictType = dictTypeMapper.selectById(id);
        if (dictType==null) {
            return "当前字典数据不存在";
        }
        if(dictType.getStatus()==0){
            dictType.setStatus(1);
            dictTypeMapper.updateById(dictType);
            return "关闭成功";
        }
        if(dictType.getStatus()==1){
            dictType.setStatus(0);
            dictTypeMapper.updateById(dictType);
            return "开启成功";
        }
        return "操作失败";
    }
}
