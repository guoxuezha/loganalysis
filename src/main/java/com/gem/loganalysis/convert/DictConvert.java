package com.gem.loganalysis.convert;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.loganalysis.model.dto.asset.AssetDTO;
import com.gem.loganalysis.model.dto.asset.AssetGroupDTO;
import com.gem.loganalysis.model.dto.edit.DictDataDTO;
import com.gem.loganalysis.model.dto.edit.DictTypeDTO;
import com.gem.loganalysis.model.entity.*;
import com.gem.loganalysis.model.vo.DictDataRespVO;
import com.gem.loganalysis.model.vo.DictItemRespVO;
import com.gem.loganalysis.model.vo.DictTypeRespVO;
import com.gem.loganalysis.model.vo.OrgRespVO;
import com.gem.loganalysis.model.vo.asset.AssetGroupRespVO;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据字典转换 Convert
 *
 * @author czw
 */
@Mapper
public interface DictConvert {

    DictConvert INSTANCE = Mappers.getMapper(DictConvert.class);


    DictItemRespVO convert(DictItem bean);

    /**
     * 构建数据字典列表
     *
     * @param dictList 数组字典列表
     * @return 数据字典树
     */
    default List<DictItemRespVO> buildDictTree(List<DictItem> dictList) {
        // 构建数据字典树
        // 使用 LinkedHashMap 的原因，是为了排序 。实际也可以用 Stream API ，就是太丑了。
        Map<Integer, DictItemRespVO> treeNodeMap = new LinkedHashMap<>();
        dictList.forEach(dict -> treeNodeMap.put(dict.getId(), DictConvert.INSTANCE.convert(dict)));
        // 处理父子关系
        treeNodeMap.values().stream().filter(node -> node.getParentId()!=null).forEach(childNode -> {
            // 获得父节点
            DictItemRespVO parentNode = treeNodeMap.get(childNode.getParentId());
            if (parentNode == null) {
             /*   LoggerFactory.getLogger(getClass()).error("[buildRouterTree][resource({}) 找不到父资源({})]",
                        childNode.getOrgId(), childNode.getParentOrg());*/
                return;
            }
            // 将自己添加到父节点中
            if (parentNode.getChildren() == null) {
                parentNode.setChildren(new ArrayList<>());
            }
            parentNode.getChildren().add(childNode);
        });
        // 获得到所有的根节点
        if (CollUtil.isEmpty(treeNodeMap.values())) {
            return new ArrayList<>();
        }
        return treeNodeMap.values().stream().filter(node -> node.getParentId()==null).collect(Collectors.toList());
    }



}
