package com.gem.loganalysis.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.ContentType;
import com.gem.loganalysis.model.bo.LogAnalysisRuleBo;
import com.gem.loganalysis.model.bo.MergeLog;
import com.gem.utils.file.BlockData;
import com.gem.utils.file.BlockFile;
import com.gem.utils.file.BlockIndex;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/16 16:02
 */
public class BlockFileUtil {

    public static void main(String[] args) {
        BlockFile blockFile = new BlockFile("./", "00019743cc8340eea0b640a7f8d24d7120230516.DAT", "00019743cc8340eea0b640a7f8d24d7120230516.IDX", true, 1, 64);
        blockFile.writeData("00019743cc8340eea0b640a7f8d24d71", "{\"module\":\"fw\",\"src_addr\":\"89.248.163.145\",\"src_port\":\"58376\",\"proto\":\"tcp\",\"dst_addr\":\"222.95.84.218\",\"dst_port\":\"9101\",\"action\":\"accept\",\"time\":\":2023-05-12 11:15:29\",\"src_intf\":\"T2/1\"}");
        blockFile.writeData("00019743cc8340eea0b640a7f8d24d71", "{\"module\":\"fw\",\"src_addr\":\"222.184.52.156\",\"src_port\":\"57860\",\"proto\":\"tcp\",\"dst_addr\":\"222.95.84.127\",\"dst_port\":\"80\",\"action\":\"accept\",\"time\":\":2023-05-12 11:20:40\",\"src_intf\":\"T2/1\"}");

        List<BlockIndex> blockIndexList = blockFile.getBlockIndex("00019743cc8340eea0b640a7f8d24d71");
        List<BlockData> result = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            BlockData blockData = blockFile.readData(blockIndexList.get(i).getKey(), blockIndexList.get(i).getDataOffset(), blockIndexList.get(i).getDataLength());
            result.add(blockData);
            System.out.println(blockData.getKey() + "\n" + blockData.getDataOffset() + "\n" + blockData.getDataLength() + "\n" + new String(blockData.getData()));
        }
    }

    /**
     * 将日志写入文件
     *
     * @param mergeLog       日志记录
     * @param analysisRuleBo 解析规则
     */
    public static void writeLog(MergeLog mergeLog, LogAnalysisRuleBo analysisRuleBo) {
        if (!DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN).equals(analysisRuleBo.getBlockFileDay())) {
            // 旧日文件关闭上传
            analysisRuleBo.getBlockFile().close();
            MinioUtil minioUtil = SpringContextUtil.getBean(MinioUtil.class);
            File datFile = new File("./" + analysisRuleBo.getRuleRelaId() + analysisRuleBo.getBlockFileDay() + ".DAT");
            File idxFile = new File("./" + analysisRuleBo.getRuleRelaId() + analysisRuleBo.getBlockFileDay() + ".IDX");
            minioUtil.upload(getMultipartFile(datFile));
            minioUtil.upload(getMultipartFile(idxFile));
            analysisRuleBo.setBlockFileDay(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN));
            analysisRuleBo.setBlockFile(new BlockFile("./", analysisRuleBo.getRuleRelaId() + analysisRuleBo.getBlockFileDay() + ".DAT", analysisRuleBo.getRuleRelaId() + analysisRuleBo.getBlockFileDay() + ".IDX", true, 3, 64));
        }
        analysisRuleBo.getBlockFile().writeData(mergeLog.getLogId(), mergeLog.toString());
    }

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


}
