package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 资产类型树
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/19 9:58
 */
public class AssetTypeTree {

    private static AssetTypeTree INSTANCE = null;

    private AssetTypeNode root;

    private AssetTypeTree() {
        init();
    }

    public static AssetTypeTree getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AssetTypeTree();
        }
        return INSTANCE;
    }

    public boolean init() {
        root = new AssetTypeNode();
        root.id = -1;
        root.sort = 0;
        root.text = "root";
        root.level = -1;
        root.pid = null;
        root.children = new ArrayList<>();
        String querySql = "SELECT ID, SORT, TEXT, `LEVEL`, PARENT_ID FROM SYS_DICT_ITEM ORDER BY `LEVEL`, PARENT_ID, ID";
        ArrayList<HashMap<String, String>> dataSet = new DAO().getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql, 0, 0);
        if (CollUtil.isNotEmpty(dataSet)) {
            for (HashMap<String, String> map : dataSet) {
                append(new AssetTypeNode(map));
            }
        }
        return true;
    }

    public AssetTypeNode search(Integer id) {
        return search(id, root);
    }

    private AssetTypeNode search(Integer id, AssetTypeNode node) {
        if (Objects.equals(id, node.id)) {
            return node;
        }
        AssetTypeNode result = null;
        if (CollUtil.isNotEmpty(node.children)) {
            for (AssetTypeNode child : node.children) {
                result = search(id, child);
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    public void append(AssetTypeNode assetTypeNode) {
        if (assetTypeNode.firstLevel()) {
            root.children.add(assetTypeNode);
        } else {
            AssetTypeNode parent = search(assetTypeNode.pid);
            if (parent.children == null) {
                parent.children = new ArrayList<>();
            }
            parent.children.add(assetTypeNode);
        }
    }

    public Integer getParentNodeId(Integer id) {
        AssetTypeNode node = search(id);
        if (node.level == 0) {
            return node.id;
        } else {
            return getParentNodeId(node.pid);
        }
    }

    public AssetTypeNode getParentNode(Integer id) {
        AssetTypeNode node = search(id);
        return search(node.pid);
    }

    public void delete(Integer id) {
        AssetTypeNode parentNode = getParentNode(id);
        if (CollUtil.isNotEmpty(parentNode.children)) {
            for (int i = parentNode.children.size() - 1; i > 0; i--) {
                if (Objects.equals(id, parentNode.children.get(i).id)) {
                    parentNode.children.remove(i);
                }
            }
        }
    }

    public void update(AssetTypeNode node) {
        AssetTypeNode assetTypeNode = search(node.id);
        assetTypeNode = node;
    }

    @NoArgsConstructor
    public static class AssetTypeNode {

        private Integer id;

        private Integer sort;

        private String text;

        private Integer level;

        private Integer pid;

        private List<AssetTypeNode> children;

        private AssetTypeNode(HashMap<String, String> map) {
            id = Integer.valueOf(map.get("ID"));
            sort = Integer.valueOf(map.get("SORT"));
            text = map.get("TEXT");
            level = Integer.valueOf(map.get("LEVEL"));
            String parentId = map.get("PARENT_ID");
            if (StrUtil.isNotEmpty(parentId)) {
                pid = Integer.valueOf(parentId);
            }
        }

        private boolean firstLevel() {
            return 0 == level;
        }

    }
}
