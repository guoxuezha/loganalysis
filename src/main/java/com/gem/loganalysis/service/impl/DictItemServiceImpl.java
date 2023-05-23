package com.gem.loganalysis.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.gem.loganalysis.mapper.DictItemMapper;
import com.gem.loganalysis.mapper.DictType2Mapper;
import com.gem.loganalysis.model.dto.query.DictItemQueryDTO;
import com.gem.loganalysis.model.entity.DictItem;
import com.gem.loganalysis.model.entity.DictType2;
import com.gem.loganalysis.model.vo.DictItemRespVO;
import com.gem.loganalysis.service.DictItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 字典数据服务实现层
 *
 * @author czw
 * @since 2023-02-27
 */
@Service
@Slf4j
@AllArgsConstructor
public class DictItemServiceImpl implements DictItemService {

    private final DictType2Mapper dictTypeMapper;

    private final DictItemMapper dictItemMapper;

    /**
     * 排序 dictType > sort
     */
    private static final Comparator<DictItem> COMPARATOR_TYPE_AND_SORT = Comparator
            .comparing(DictItem::getTypeId)
            .thenComparingInt(DictItem::getSort);


    @Override
    public String checkCreateOrUpdate(DictItem reqVO) {
        if(reqVO.getId()!=null){
            DictItem dictItemUpdate = dictItemMapper.selectById(reqVO.getId());
            if (dictItemUpdate == null) {
                return "当前字典数据不存在";
            }
        }
        if(reqVO.getTypeId()==null){
            return "请传入字典类型ID";
        }
        if(reqVO.getSort()==null){
            return "请传入字典排序";
        }
        if(reqVO.getValue()==null){
            return "请传入字典项键值";
        }
        if(reqVO.getText()==null){
            return "请传入字典项内容";
        }
        //效验字典类型不存在
        DictType2 dictType = dictTypeMapper.selectById(reqVO.getTypeId());
        if (dictType == null) {
           return "当前字典类型不存在";
        }
        if (dictType.getStatus()!=0) {
            return "字典类型不处于开启状态，不允许选择";
        }
        //效验VALUE的唯一性
        DictItem dictItem = dictItemMapper.selectOne(new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getTypeId, reqVO.getTypeId())
                .eq(DictItem::getValue, reqVO.getValue())
                .eq(reqVO.getParentId()!=null, DictItem::getParentId, reqVO.getParentId())
                .last("LIMIT 1"));
        if(dictItem!=null){
            //新增的情况
            if(reqVO.getId()==null){
                return "已经存在该值的字典数据";
            }else{//更新的情况
                if(!reqVO.getId().equals(dictItem.getId())){
                    return "已经存在该值的字典数据";
                }
            }
        }
        return "正确";
    }

    @Override
    public int createDictItem(DictItem reqVO) {
        //创建时设置状态为开启
        reqVO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        reqVO.setStatus(0);
        // 插入字典类型
        DictType2 dictType = dictTypeMapper.selectById(reqVO.getTypeId());
        reqVO.setCode(dictType.getCode());
        return dictItemMapper.insert(reqVO);
    }

    @Override
    public int updateDictType(DictItem reqVO) {
        // 更新字典类型
        DictType2 dictType = dictTypeMapper.selectById(reqVO.getTypeId());
        reqVO.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        reqVO.setCode(dictType.getCode());
        return dictItemMapper.updateById(reqVO);
    }

    @Override
    public String deleteCheck(String id) {
        //判断该字典类型是否存在
        DictItem dictItem = dictItemMapper.selectById(id);
        if (dictItem==null) {
            return "当前字典数据不存在";
        }
        //判断类型是否重复
        if (dictItemMapper.selectCount(new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getParentId, id)) > 0) {
            return "无法删除，该字典数据下还有字数据，请先删除子数据";
        }
        return "正确";
    }

    @Override
    public int deleteDictItem(String id) {
        return dictItemMapper.deleteById(id);
    }

    @Override
    public String checkDictItemExist(String id) {
        //判断该字典数据是否存在
        DictItem dictItem = dictItemMapper.selectById(id);
        if (dictItem==null) {
            return "当前字典数据不存在";
        }
        return "正确";
    }

    @Override
    public List<DictItemRespVO> getDictItem(String id) {
        List<DictItemRespVO> dictItemRespVOS = new ArrayList<>();
        List<DictItem> dictItem = dictItemMapper.selectList(new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getTypeId, id).isNull(DictItem::getParentId).orderByAsc(DictItem::getSort));
        dictItem.forEach(e->{
            DictItemRespVO dictItemRespVO = new DictItemRespVO();
            BeanUtils.copyProperties(e,dictItemRespVO);
            //没有写转换类，只能笨笨的用BeanUtils然后循环,可优化
            List<DictItem> dictSonItem = dictItemMapper.selectList(new LambdaQueryWrapper<DictItem>()
                    .eq(DictItem::getParentId, e.getId()).orderByAsc(DictItem::getSort));
            if(dictSonItem!=null&&dictSonItem.size()!=0){
                List<DictItemRespVO> dictItemSonRespVOS = new ArrayList<>();
                dictSonItem.forEach(son->{
                    DictItemRespVO dictItemSonRespVO = new DictItemRespVO();
                    BeanUtils.copyProperties(son,dictItemSonRespVO);
                    dictItemSonRespVOS.add(dictItemSonRespVO);
                });
                dictItemRespVO.setChildren(dictItemSonRespVOS);
            }
            dictItemRespVOS.add(dictItemRespVO);
        });
        return dictItemRespVOS;
    }

    @Override
    public List<DictItem> getDictItemList(DictItemQueryDTO reqVO) {
        LambdaQueryWrapper<DictItem> lqw = new LambdaQueryWrapper<DictItem>()
                .like(StringUtils.isNotBlank(reqVO.getCode()), DictItem::getCode, reqVO.getCode())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DictItem::getStatus, reqVO.getStatus())
                .eq(reqVO.getParentId()!=null, DictItem::getParentId, reqVO.getParentId())
                .eq(reqVO.getTypeId()!=null, DictItem::getTypeId, reqVO.getTypeId())
                .orderByAsc(DictItem::getSort);
        return dictItemMapper.selectList(lqw);
    }

    @Override
    public String changeStatus(String id) {
        DictItem dictItem = dictItemMapper.selectById(id);
        dictItem.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        if (dictItem==null) {
            return "当前字典数据不存在";
        }
        if(dictItem.getStatus()==0){
            dictItem.setStatus(1);
            dictItemMapper.updateById(dictItem);
            return "关闭成功";
        }
        if(dictItem.getStatus()==1){
            dictItem.setStatus(0);
            dictItemMapper.updateById(dictItem);
            return "开启成功";
        }
        return "操作失败";
    }

    @Override
    public List<DictItemRespVO> getSimpleDictItem() {
        List<DictItemRespVO> dictItemRespVOS = new ArrayList<>();
        List<DictItem> dictItem = dictItemMapper.selectList(new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getStatus, 0).isNull(DictItem::getParentId));
        dictItem.sort(COMPARATOR_TYPE_AND_SORT);
        dictItem.forEach(e->{
            DictItemRespVO dictItemRespVO = new DictItemRespVO();
            BeanUtils.copyProperties(e,dictItemRespVO);
            //没有写转换类，只能笨笨的用BeanUtils然后循环,可优化
            List<DictItem> dictSonItem = dictItemMapper.selectList(new LambdaQueryWrapper<DictItem>()
                    .eq(DictItem::getParentId, e.getId()).eq(DictItem::getStatus, 0));
            if(dictSonItem!=null&&dictSonItem.size()!=0){
                List<DictItemRespVO> dictItemSonRespVOS = new ArrayList<>();
                dictSonItem.forEach(son->{
                    DictItemRespVO dictItemSonRespVO = new DictItemRespVO();
                    BeanUtils.copyProperties(son,dictItemSonRespVO);
                    dictItemSonRespVOS.add(dictItemSonRespVO);
                });
                dictItemRespVO.setChildren(dictItemSonRespVOS);
            }
            dictItemRespVOS.add(dictItemRespVO);
        });
        return dictItemRespVOS;
    }


}
