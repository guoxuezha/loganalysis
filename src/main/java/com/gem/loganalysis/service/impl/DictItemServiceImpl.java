package com.gem.loganalysis.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gem.loganalysis.convert.DictConvert;
import com.gem.loganalysis.mapper.DictItemMapper;
import com.gem.loganalysis.mapper.DictType2Mapper;
import com.gem.loganalysis.model.dto.query.DictItemQueryDTO;
import com.gem.loganalysis.model.entity.DictItem;
import com.gem.loganalysis.model.entity.DictType2;
import com.gem.loganalysis.model.vo.DictItemRespVO;
import com.gem.loganalysis.service.DictItemService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

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

    @Getter
    //资产类型缓存
    private volatile List<DictItem> dictItemCache;

    /**
     * 排序 dictType > sort
     */
    private static final Comparator<DictItem> COMPARATOR_TYPE_AND_SORT = Comparator
            .comparing(DictItem::getTypeId)
            .thenComparingInt(DictItem::getSort);


    @Override
    @PostConstruct
    public void initLocalCache() {
        //构建所有数据的缓存
        dictItemCache = dictItemMapper.selectList(null);
    }

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
        int insert = dictItemMapper.insert(reqVO);
        initLocalCache();
        return insert;
    }

    @Override
    public int updateDictType(DictItem reqVO) {
        // 更新字典类型
        DictType2 dictType = dictTypeMapper.selectById(reqVO.getTypeId());
        reqVO.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        reqVO.setCode(dictType.getCode());
        int i = dictItemMapper.updateById(reqVO);
        initLocalCache();
        return i;
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
        int i = dictItemMapper.deleteById(id);
        initLocalCache();
        return i;
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
        List<DictItem> dictItem = dictItemMapper.selectList(new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getTypeId, id).orderByAsc(DictItem::getSort));
        return DictConvert.INSTANCE.buildDictTree(dictItem);
    }

    @Override
    public List<DictItemRespVO> getDictItemList(DictItemQueryDTO reqVO) {
        List<DictItem> list = new ArrayList<>();
        if(!StringUtils.isBlank(reqVO.getCode())){
            list = dictItemCache.stream().filter(e -> e.getCode().equals(reqVO.getCode()) && e.getStatus() == 0).collect(Collectors.toList());
        }else{
            list = dictItemCache.stream().filter(e -> e.getStatus() == 0).collect(Collectors.toList());

        }
        return DictConvert.INSTANCE.buildDictTree(list);
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
        initLocalCache();
        return "操作失败";
    }

    @Override
    public Map<String, List<DictItem>> getDictItemMap() {
        return dictItemCache.stream().collect(Collectors.groupingBy(DictItem::getCode));
    }


    @Override
    public String getDictData(String code, String value) {
        if(StringUtils.isBlank(code) ||StringUtils.isBlank(value)){
            return "";
        }
        Map<String, List<DictItem>> dictDataMap = getDictItemMap();
        List<DictItem> dictData = dictDataMap.get(code);
        if(dictData==null){
            return "";
        }
        List<DictItem> collect = dictData.stream().filter(e -> e.getValue().equals(value)&&e.getLevel()==0).collect(Collectors.toList());
        if(collect.size()>0){
            return collect.get(0).getText();
        }else{
            return "";
        }
    }

    @Override
    public String getValueByText(String code, String text) {
        if(StringUtils.isBlank(code) ||StringUtils.isBlank(text)){
            return null;
        }
        Map<String, List<DictItem>> dictDataMap = getDictItemMap();
        List<DictItem> dictData = dictDataMap.get(code);
        if(dictData==null){
            return null;
        }
        List<DictItem> collect = dictData.stream().filter(e -> e.getText().equals(text)&&e.getLevel()==0).collect(Collectors.toList());
        if(collect.size()>0){
            return collect.get(0).getValue();
        }else{
            return null;
        }
    }

    @Override
    public String getChildDictData(String code, String value, Integer parentId) {
        if(StringUtils.isBlank(code) ||StringUtils.isBlank(value)||parentId==null){
            return "";
        }
        Map<String, List<DictItem>> dictDataMap = getDictItemMap();
        List<DictItem> dictData = dictDataMap.get(code);
        if(dictData==null){
            return "";
        }
        List<DictItem> collect = dictData.stream().filter(e -> e.getParentId().equals(parentId) &&e.getValue().equals(value)&&e.getLevel()==0).collect(Collectors.toList());
        if(collect.size()>0){
            return collect.get(0).getText();
        }else{
            return "";
        }
    }


}
