package com.gem.loganalysis.model.vo;

import io.minio.messages.Item;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/17 16:41
 */
@Data
public class MinioFileVO {

    private String fileName;

    private String lastModified;

    private long size;

    public MinioFileVO(Item item) {
        this.fileName = item.objectName();
        this.lastModified = item.lastModified().toLocalTime().toString();
        this.size = item.size();
    }
}
