package com.gem.loganalysis.model.dto;

import lombok.Data;

/**
 * Description: 日志内容查询参数封装类
 * Date: 2023/4/27 11:27
 *
 * @author GuoChao
 **/
@Data
public class LogQueryDTO {

    private String host;

    private String severity;

    private String facility;

}
