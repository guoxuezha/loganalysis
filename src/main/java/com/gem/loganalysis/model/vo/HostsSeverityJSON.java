package com.gem.loganalysis.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostsSeverityJSON {
    private double severity;
    private List<HostsSeverityVO> hosts;
}
