package com.gem.loganalysis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VulnCountVO {

    private String count;
    private String value;
    private String cCount;
}
