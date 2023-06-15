package com.gem.loganalysis.convert;

import cn.hutool.core.collection.CollUtil;
import com.gem.loganalysis.model.dto.edit.OrgDTO;
import com.gem.loganalysis.model.entity.M4SsoOrg;
import com.gem.loganalysis.model.vo.OrgRespVO;
import com.gem.loganalysis.model.vo.TreeRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据字典转换 Convert
 *
 * @author czw
 */
@Mapper
public interface M4SsoConvert {

    M4SsoConvert INSTANCE = Mappers.getMapper(M4SsoConvert.class);

    M4SsoOrg convert(OrgDTO bean);

    OrgRespVO convert(M4SsoOrg bean);

    @Mappings({
            @Mapping(source = "orgId", target = "value"),
            @Mapping(source = "orgName", target = "label"),
            @Mapping(source = "parentOrg", target = "parentId")
    })
    TreeRespVO convert01(M4SsoOrg bean);


    /**
     * 构建组织部门列表
     *
     * @param orgList 组织部门列表
     * @return 组织部门树
     */
    default List<OrgRespVO> buildOrgTree(List<M4SsoOrg> orgList) {
        // 构建组织部门树
        // 使用 LinkedHashMap 的原因，是为了排序 。实际也可以用 Stream API ，就是太丑了。
        Map<String, OrgRespVO> treeNodeMap = new LinkedHashMap<>();
        orgList.forEach(org -> treeNodeMap.put(org.getOrgId(), M4SsoConvert.INSTANCE.convert(org)));
        // 处理父子关系
        treeNodeMap.values().stream().filter(node -> node.getParentOrg()!=null&&!node.getParentOrg().trim().equals("")&&!node.getParentOrg().equals("0")).forEach(childNode -> {
            // 获得父节点
            OrgRespVO parentNode = treeNodeMap.get(childNode.getParentOrg());
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
        return treeNodeMap.values().stream().filter(node -> node.getParentOrg()==null
                ||node.getParentOrg().trim().equals("")
                ||"0".equals(node.getParentOrg())).collect(Collectors.toList());
    }



    /**
     * 构建组织部门简单下拉列表
     *
     * @param orgList 组织部门列表
     * @return 组织部门树
     */
    default List<TreeRespVO> buildOrgTree01(List<M4SsoOrg> orgList) {
        // 构建组织部门树
        // 使用 LinkedHashMap 的原因，是为了排序 。实际也可以用 Stream API ，就是太丑了。
        Map<String, TreeRespVO> treeNodeMap = new LinkedHashMap<>();
        orgList.forEach(org -> treeNodeMap.put(org.getOrgId(), M4SsoConvert.INSTANCE.convert01(org)));
        // 处理父子关系
        treeNodeMap.values().stream().filter(node -> node.getParentId()!=null&&!node.getParentId().trim().equals("")&&!node.getParentId().equals("0")).forEach(childNode -> {
            // 获得父节点
            TreeRespVO parentNode = treeNodeMap.get(childNode.getParentId());
            if (parentNode == null) {
/*                LoggerFactory.getLogger(getClass()).error("[buildRouterTree][resource({}) 找不到父资源({})]",
                        childNode.getValue(), childNode.getParentId());*/
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
        return treeNodeMap.values().stream().filter(node -> node.getParentId()==null
                ||node.getParentId().trim().equals("")
                ||"0".equals(node.getParentId())).collect(Collectors.toList());
    }

}
