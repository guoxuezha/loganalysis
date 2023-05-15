package com.gem.loganalysis.model;

import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/15 15:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private Integer total;

    private List<T> records;

    public PageResponse(Page<T> page) {
        this.total = Math.toIntExact(page.getTotal());
        this.records = page.getResult();
    }

}
