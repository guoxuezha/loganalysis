package com.gem.loganalysis.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Excel 工具类
 *
 * @author czw
 */
public class ExcelUtils {

    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response  响应
     * @param filename  文件名
     * @param sheetName Excel sheet 名
     * @param head      Excel head 头
     * @param data      数据列表哦
     * @param <T>       泛型，保证 head 和 data 类型的一致性
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), head)
                .autoCloseStream(false) // 不要自动关闭，交给 Servlet 自己处理
                // .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 基于 column 长度，自动适配。最大 255 宽度
                .sheet(sheetName).doWrite(data);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        //response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
        List<T> data = new LinkedList<>();
        EasyExcel.read(file.getInputStream(), head, new ReadListener<T>(data::addAll)).sheet().doRead();
        return data;
    }

/*    public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
        return EasyExcel.read(file.getInputStream(), head, null)
                .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
                .doReadAllSync();
    }*/

}

    class ReadListener<T> extends PageReadListener<T> {
        private List<T> cache = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        private final Consumer<List<T>> consumer;

        public ReadListener(Consumer<List<T>> consumer) {
            super(consumer);
            this.consumer = consumer;
        }

        @Override
        public void invoke(T data, AnalysisContext context) {
            // 如果一行Excel数据均为空值，则不装载该行数据
            if (lineNull(data)) {
                return;
            }
            cache.add(data);
            if (cache.size() >= BATCH_COUNT) {
                consumer.accept(cache);
                cache = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            if (cache == null || cache.isEmpty()) {
                return;
            }
            consumer.accept(cache);
        }

        boolean lineNull(T line) {
            if (line instanceof String) {
                return StringUtils.isEmpty((String) line);
            }
            try {
                Set<Field> fields = Arrays.stream(line.getClass().getDeclaredFields()).filter(f -> f.isAnnotationPresent(ExcelProperty.class)).collect(Collectors.toSet());
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.get(line) != null) {
                        return false;
                    }
                }
                return true;
            } catch (Exception ignored) {
            }
            return true;
        }

}
