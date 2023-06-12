package com.gem.loganalysis.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.gem.loganalysis.model.bo.LogAnalysisRuleBo;
import com.gem.loganalysis.model.bo.MergeLog;
import com.gem.utils.file.BlockData;
import com.gem.utils.file.BlockFile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/16 16:02
 */
public class BlockFileUtil {

    public static void main(String[] args) throws UnsupportedEncodingException {
        BlockFile blockFile = new BlockFile(getBlockFileRootPath(), "0420230526.DAT", "0420230526.IDX", true, 1, 64);
        System.out.println(blockFile.getBlockCount());

        BlockData fetch = blockFile.fetch();
        System.out.println(Arrays.toString(fetch.getData()));
        blockFile.destroy();
    }

    /**
     * 将日志写入文件
     *
     * @param mergeLog       日志记录
     * @param analysisRuleBo 解析规则
     */
    public static void writeLog(MergeLog mergeLog, LogAnalysisRuleBo analysisRuleBo) {
        // 执行写入前先判断文件是否应切换到新的一天
        if (!DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN).equals(analysisRuleBo.getBlockFileDay())) {
            analysisRuleBo.getBlockFile().destroy();
            MinioUtil minioUtil = SpringContextUtil.getBean(MinioUtil.class);
            File datFile = new File(getBlockFileRootPath() + analysisRuleBo.getRuleRelaId() + analysisRuleBo.getBlockFileDay() + ".DAT");
            File idxFile = new File(getBlockFileRootPath() + analysisRuleBo.getRuleRelaId() + analysisRuleBo.getBlockFileDay() + ".IDX");
            minioUtil.upload(getMultipartFile(datFile));
            minioUtil.upload(getMultipartFile(idxFile));
            analysisRuleBo.setBlockFileDay(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN));
            analysisRuleBo.setBlockFile(new BlockFile(getBlockFileRootPath(), analysisRuleBo.getRuleRelaId() + analysisRuleBo.getBlockFileDay() + ".DAT", analysisRuleBo.getRuleRelaId() + analysisRuleBo.getBlockFileDay() + ".IDX", true, 3, 64));
        }
        analysisRuleBo.getBlockFile().writeData(mergeLog.getLogId(), JSONUtil.toJsonStr(mergeLog));
    }

    /**
     * 把File转为MultipartFile对象
     *
     * @param file file
     * @return
     */
    public static MultipartFile getMultipartFile(File file) {
        FileInputStream fileInputStream;
        MultipartFile multipartFile = null;
        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(), file.getName(), ContentType.TEXT_PLAIN.getValue(), fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }

    public static String getBlockFileRootPath() {
        return System.getProperty("user.dir") + File.separator + "blockFile" + File.separator;
    }

}
