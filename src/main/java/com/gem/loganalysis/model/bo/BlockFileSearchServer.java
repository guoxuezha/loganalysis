package com.gem.loganalysis.model.bo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.gem.loganalysis.model.vo.asset.AssetLogFileVO;
import com.gem.loganalysis.util.MinioUtil;
import com.gem.loganalysis.util.SpringContextUtil;
import io.minio.messages.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gem.loganalysis.util.BlockFileUtil.getBlockFileRootPath;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/11 22:32
 */
public class BlockFileSearchServer {

    private static BlockFileSearchServer INSTANCE;

    private List<File> classPathFileList;

    private List<Item> minioFileList;

    private BlockFileSearchServer() {
        reload();
    }

    public static BlockFileSearchServer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlockFileSearchServer();
        }
        return INSTANCE;
    }

    public static void main(String[] args) {
        String name = "0220230526.DAT";
        System.out.println(name.substring(name.length() - 12, name.length() - 4));
    }

    public void reload() {
        // 获取项目资源路径下的日志文件
        List<File> files = FileUtil.loopFiles(getBlockFileRootPath());
        classPathFileList = files == null ? new ArrayList<>() : files;
        // 获取Minio上的日志原始文件信息
        List<Item> items = SpringContextUtil.getBean(MinioUtil.class).listObjects();
        minioFileList = items == null ? new ArrayList<>() : items;
    }

    public Set<AssetLogFileVO> getFileInfoByRuleRelaId(String ruleRelaId, String severity, String facility) {
        if (StrUtil.isEmpty(ruleRelaId)) {
            return null;
        }
        HashSet<AssetLogFileVO> result = new HashSet<>();
        HashSet<String> fileNameSet = new HashSet<>();
        for (File file : classPathFileList) {
            fileNameSet.add(file.getName());
        }
        for (Item item : minioFileList) {
            fileNameSet.add(item.objectName());
        }
        for (String name : fileNameSet) {
            if (name.endsWith(".DAT") && ruleRelaId.equals(name.substring(0, name.length() - 12))) {
                result.add(new AssetLogFileVO(severity, facility, name.substring(name.length() - 12, name.length() - 4), name));
            }
        }
        return result;
    }

}
