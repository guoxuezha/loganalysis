package com.gem.loganalysis.exception;

import com.gem.loganalysis.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {

    /** 自定义业务异常 */
    @ExceptionHandler(ServiceException.class)
    public Result serviceExceptionHandler(ServiceException ex) {
        log.error("自定义业务异常：{}", ex.getMessage(), ex);
        return Result.failed(ex.getMessage());
    }

    /** 运行时异常 */
    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(RuntimeException ex) {
        log.error("运行时异常：{}", ex.getMessage(), ex);
        return Result.failed("运行时异常");
    }

    /** 空指针异常 */
    @ExceptionHandler(NullPointerException.class)
    public Result nullPointerExceptionHandler(NullPointerException ex) {
        log.error("空指针异常：{} ", ex.getMessage(), ex);
        return Result.failed("空指针异常");
    }

    /** 类型转换异常 */
    @ExceptionHandler(ClassCastException.class)
    public Result classCastExceptionHandler(ClassCastException ex) {
        log.error("类型转换异常：{} ", ex.getMessage(), ex);
        return Result.failed("类型转换异常");
    }
    /** 文件未找到异常 */
    @ExceptionHandler(FileNotFoundException.class)
    public Result FileNotFoundException(FileNotFoundException ex) {
        log.error("文件未找到异常：{} ", ex.getMessage(), ex);
        return Result.failed("文件未找到异常");
    }
    /** 数字格式异常 */
    @ExceptionHandler(NumberFormatException.class)
    public Result NumberFormatException(NumberFormatException ex) {
        log.error("数字格式异常：{} ", ex.getMessage(), ex);
        return Result.failed("数字格式异常");
    }
    /** 安全异常 */
    @ExceptionHandler(SecurityException.class)
    public Result SecurityException(SecurityException ex) {
        log.error("安全异常：{} ", ex.getMessage(), ex);
        return Result.failed("安全异常");
    }
    /** sql异常 */
    @ExceptionHandler(SQLException.class)
    public Result SQLException(SQLException ex) {
        log.error("sql异常：{} ", ex.getMessage(), ex);
        return Result.failed("sql异常");
    }
    /** 类型不存在异常 */
    @ExceptionHandler(TypeNotPresentException.class)
    public Result TypeNotPresentException(TypeNotPresentException ex) {
        log.error("类型不存在异常：{} ", ex.getMessage(), ex);
        return Result.failed("类型不存在异常");
    }

    /** IO异常 */
    @ExceptionHandler(IOException.class)
    public Result iOExceptionHandler(IOException ex) {
        log.error("IO异常：{} ", ex.getMessage(), ex);
        return Result.failed("IO异常");
    }

    /** 未知方法异常 */
    @ExceptionHandler(NoSuchMethodException.class)
    public Result noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        log.error("未知方法异常：{} ", ex.getMessage(), ex);
        return Result.failed("未知方法异常");
    }

    /** 数组越界异常 */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public Result indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        log.error("数组越界异常：{} ", ex.getMessage(), ex);
        return Result.failed("数组越界异常");
    }
    /** sql语法错误异常 */
    @ExceptionHandler(BadSqlGrammarException.class)
    public Result BadSqlGrammarException(BadSqlGrammarException ex) {
        log.error("sql语法错误异常：{} ", ex.getMessage(), ex);
        return Result.failed("sql语法错误异常");
    }

    /** 无法注入bean异常 */
    @ExceptionHandler(NoSuchBeanDefinitionException.class)
    public Result NoSuchBeanDefinitionException(NoSuchBeanDefinitionException ex) {
        log.error("无法注入bean异常 ：{} ", ex.getMessage(), ex);
        return Result.failed("无法注入bean");
    }

    /** Http消息不可读异常 */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result requestNotReadable(HttpMessageNotReadableException ex) {
        log.error("400错误..requestNotReadable：{} ", ex.getMessage(), ex);
        return Result.failed("Http消息不可读");
    }

    /** 400错误 */
    @ExceptionHandler({TypeMismatchException.class})
    public Result requestTypeMismatch(TypeMismatchException ex) {
        log.error("400错误..TypeMismatchException：{} ", ex.getMessage(), ex);
        return Result.failed("服务器异常");
    }

    /** 500错误 */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public Result server500(RuntimeException ex) {
        log.error("500错误：{} ", ex.getMessage(), ex);
        return Result.failed("服务器异常");
    }

    /** 栈溢出 */
    @ExceptionHandler({StackOverflowError.class})
    public Result requestStackOverflow(StackOverflowError ex) {
        log.error("栈溢出：{} ", ex.getMessage(), ex);
        return Result.failed("栈溢出异常");
    }

    /** 除数不能为0 */
    @ExceptionHandler({ArithmeticException.class})
    public Result arithmeticException(ArithmeticException ex) {
        log.error("除数不能为0：{} ", ex.getMessage(), ex);
        return Result.failed("除数不能为0异常");
    }

    /** 对象参数校验未通过错误 */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("参数校验未通过错误：{} ", ex.getMessage(), ex);
        return Result.failed(ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    /** 嵌套与集合参数校验未通过错误 */
    @ExceptionHandler({ConstraintViolationException.class})
    public Result constraintViolationException(ConstraintViolationException ex) {
        log.error("参数校验未通过错误：{} ", ex.getMessage(), ex);
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation violation : violations) {
            builder.append(violation.getMessage());
            break;
        }
        return Result.failed((builder.toString()));
    }

    /** 其他错误 */
    @ExceptionHandler({Exception.class})
    public Result exception(Exception ex) {
        log.error("其他错误：{} ", ex.getMessage(), ex);
        return Result.failed("网络连接失败，请退出后再试");
    }

}
