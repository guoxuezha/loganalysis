package com.gem.loganalysis.util;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/7 22:54
 */
@Slf4j
public class IPUtil {

    private Searcher searcher;

    private static IPUtil INSTANCE = null;

    private IPUtil() {
        String dbPath;
        try {
            dbPath = ResourceUtils.getURL("classpath:").getPath() + "ip2region.xdb";
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            byte[] cBuff = Searcher.loadContentFromFile(dbPath);
            this.searcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            log.error("failed to load content from `{}`: ", dbPath, e);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(IPUtil.getInstance().getCityInfoByMemorySearch("123.249.94.31"));
    }

    /**
     * 获取ip属地(缓存整个 xdb 数据)
     *
     * @param ip IP地址
     * @return 返回地址
     */
    public String getCityInfoByMemorySearch(String ip) {
        try {
            long sTime = System.nanoTime();
            String region = searcher.search(ip);
            long cost = TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - sTime);
            log.info("{region: {}, ioCount: {}, took: {} μs}\n", region, searcher.getIOCount(), cost);
            return region;
        } catch (Exception e) {
            log.error("failed to search({}): ", ip, e);
        }
        return null;
    }

    public static IPUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IPUtil();
        }
        return INSTANCE;
    }


}
