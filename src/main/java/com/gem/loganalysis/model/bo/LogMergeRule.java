package com.gem.loganalysis.model.bo;

import lombok.*;

import java.util.List;

/**
 * Description:
 * Date: 2023/4/24 9:49
 *
 * @author GuoChao
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogMergeRule {

    /**
     * 是否需要合并
     */
    private boolean merge;

    /**
     * 合并主键
     */
    private List<String> mergeKeys;

    public LogMergeRule(List<String> mergeKeys) {
        this.mergeKeys = mergeKeys;
    }

    public String getKeyFormat() {
        StringBuilder keyFormat = new StringBuilder();
        if (merge) {
            for (String mergeKey : mergeKeys) {
                keyFormat.append(mergeKey).append("~");
            }
            return keyFormat.delete(keyFormat.length() - 1, keyFormat.length()).toString();
        }
        return null;
    }


}
