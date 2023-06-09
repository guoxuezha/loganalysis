package com.gem.loganalysis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VulnDataVO {
    private String date;
    private List<VulnCountVO> vulnCountList;
    private double score;
    private int high;
    private int middle;
    private int log;
    private int low;
}
