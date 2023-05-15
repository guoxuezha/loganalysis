package com.gem.loganalysis.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 2022/7/30 13:55
 *
 * @author GuoChao
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandleDTO<T> {

    @ApiModelProperty("执行结果")
    private Boolean success;

    @ApiModelProperty("异常提示")
    private String message;

    @ApiModelProperty("携带返回值")
    private T data;

    public static <T> HandleDTO<T> success() {
        return new HandleDTO<>(true, "", null);
    }

    public static <T> HandleDTO<T> success(String message) {
        return new HandleDTO<>(true, message, null);
    }

    public static <T> HandleDTO<T> success(T data) {
        return new HandleDTO<>(true, "", data);
    }

    public static <T> HandleDTO<T> success(String message, T data) {
        return new HandleDTO<>(true, message, data);
    }

    public static <T> HandleDTO<T> fail(String msg) {
        return new HandleDTO<>(false, msg, null);
    }
    public static <T> HandleDTO<T> fail(String message, T data) {
        return new HandleDTO<>(false, message, data);
    }

    public static <T> HandleDTO<T> fail(String msg, HandleDTO nestedHandleDto) {
        String message = msg + "\r\n嵌套错误为 : [" + nestedHandleDto.getMessage() + "]";
        return new HandleDTO<>(false, message, null);
    }

    public void appendMessage(String msg) {
        this.message += msg;
    }
}
