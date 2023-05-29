package com.gem.loganalysis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.loganalysis.mapper.DictDataMapper;
import com.gem.loganalysis.model.entity.DictData;
import com.gem.loganalysis.service.IDictDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author czw
 * @since 2023-05-10
 */
@Service
@DS("slave")
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements IDictDataService {

    @Resource
    private DictDataMapper dictDataMapper;


    @Override
    public Map<String, List<DictData>> getDictDataMap() {
        List<DictData> list = this.list();
        return list.stream().collect(Collectors.groupingBy(DictData::getDictType));
    }

    @Override
    public String getDictData(String dictType, String value) {
        if(value==null||value.trim().equals("")){
            return "";
        }
        Map<String, List<DictData>> dictDataMap = getDictDataMap();
        List<DictData> dictData = dictDataMap.get(dictType);
        if(dictData==null){
            return "";
        }
        List<DictData> collect = dictData.stream().filter(e -> e.getValue().equals(value)).collect(Collectors.toList());
        if(collect.size()>0){
            return collect.get(0).getLabel();
        }else{
            return "";
        }
    }
}
