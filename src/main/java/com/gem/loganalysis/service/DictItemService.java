package com.gem.loganalysis.service;


import com.gem.loganalysis.model.dto.query.DictItemQueryDTO;
import com.gem.loganalysis.model.entity.DictItem;
import com.gem.loganalysis.model.vo.DictItemRespVO;

import java.util.List;

/**
 * 字典数据服务层
 *
 * @author czw
 * @since 2023-02-27
 */
public interface DictItemService {

    /**
     * 字典数据创建检验
     * @param reqVO
     * @return
     */
    String checkCreateOrUpdate(DictItem reqVO);

    /**
     * 创建字典数据
     * @param reqVO
     * @return
     */
    int createDictItem(DictItem reqVO);

    /**
     * 修改字典数据
     * @param reqVO
     * @return
     */
    int updateDictType(DictItem reqVO);

    /**
     * 字典数据删除效验
     * @param id
     * @return
     */
    String deleteCheck(String id);

    /**
     * 删除字典数据
     * @param id
     * @return
     */
    int deleteDictItem(String id);

    /**
     * 效验是否存在
     * @param id
     * @return
     */
    String checkDictItemExist(String id);

    /**
     * 根据字典类型ID查询字典数据多级详细(传进来的是字典类型的ID！！！！！！)
     * @param id
     * @return
     */
    List<DictItemRespVO> getDictItem(String id);

    /**
     * 条件查询单层数据
     * @param reqVO
     * @return
     */
    List<DictItem> getDictItemList(DictItemQueryDTO reqVO);

    /**
     * 开启/关闭数据字典
     * @param id
     * @return
     */
    String changeStatus(String id);

    /**
     * 获取开启状态的所有数据结构并排序,主要用于前端缓存
     * @return
     */
    List<DictItemRespVO> getSimpleDictItem();
}
