package com.gem.loganalysis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gem.loganalysis.model.dto.query.DictTypeQueryPageDTO;
import com.gem.loganalysis.model.entity.DictType2;

import java.util.List;

/**
 * 字典类型服务层
 *
 * @author czw
 * @since 2023-02-27
 */
public interface DictTypeService {

    /**
     * 创建字典类型
     * @param reqVO
     * @return
     */
    int createDictType(DictType2 reqVO);

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    String checkDictTypeUnique(DictType2 dictType);

    /**
     * 校验字典类型是否存在
     * @param id
     * @return
     */
    String checkDictTypeExist(String id);

    /**
     * 修改字典类型
     * @param reqVO
     * @return
     */
    int updateDictType(DictType2 reqVO);

    /**
     * 校验字典类型是否可删除
     * @param id
     * @return
     */
    String deleteCheck(String id);

    /**
     * 删除字典类型
     * @param id
     * @return
     */
    int deleteDictType(String id);

    /**
     * 获得字典类型分页
     * @param reqVO
     * @return
     */
    IPage<DictType2> pageDictTypes(DictTypeQueryPageDTO reqVO);

    /**
     * 字典类型详细
     * @param id
     * @return
     */
    DictType2 getDictType(String id);

    /**
     * 获得全部字典类型列表
     * @return
     */
    List<DictType2> getDictTypeList();

    /**
     * 开启/关闭数据字典类型
     * @param id
     * @return
     */
    String changeStatus(String id);
}
