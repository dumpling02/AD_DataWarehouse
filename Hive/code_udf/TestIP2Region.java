package com.atguigu.ad.hive.udf;

import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;

public class TestIP2Region {
    public static void main(String[] args) throws Exception {

        //1.ip2region.xdb是其ip地址库文件,下载地址如为: https://github.com/lionsoul2014/ip2region/raw/master/data/ip2region.xdb
        byte[] bytes;
        Searcher searcher =null;
        try {

            // 读取本地磁盘的ip解析库进入到内存当中
            bytes = Searcher.loadContentFromFile("src\\main\\resources\\ip2region.xdb");
            // 创建解析对象
            searcher = Searcher.newWithBuffer(bytes);
            // 解析ip
            String search = searcher.search("223.223.182.174");
            // 打印结果
            System.out.println(search);
            searcher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (searcher!=null){
                try {
                    searcher.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
