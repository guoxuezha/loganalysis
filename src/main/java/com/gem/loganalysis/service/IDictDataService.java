package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gem.loganalysis.model.Result;
import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.entity.DictData;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author czw
 * @since 2023-05-10
 */
public interface IDictDataService extends IService<DictData> {

    /**
     * 获得数据字典根据名称分类的MAP
     * @return
     */
    Map<String, List<DictData>> getDictDataMap();


    /**
     * 获得数据字典的中文
     * @param dictType
     * @param value
     * @return
     */
    String getDictData(String dictType,String value);
}
