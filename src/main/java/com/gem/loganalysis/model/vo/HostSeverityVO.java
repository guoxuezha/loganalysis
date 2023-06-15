package com.gem.loganalysis.model.vo;

import com.gem.loganalysis.util.SeverityUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostSeverityVO {

    private String severity;
    private List<HostVO> hosts;
}
