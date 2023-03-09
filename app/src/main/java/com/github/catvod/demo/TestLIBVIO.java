package com.github.catvod.demo;

import com.github.catvod.spider.LIBVIO;
import com.github.catvod.spider.SixV;

import java.util.ArrayList;
import java.util.HashMap;

public class TestLIBVIO {
    public static void main(String[] args) {
        LIBVIO libvio = new LIBVIO();
        // 首页测试，输出...
//        String s = libvio.homeContent(true);
//        System.out.println(s);

        // 分类页面数据测试
//        String s = libvio.categoryContent("/show/1", "1", true, new HashMap<>());
//        System.out.println(s);

        // 详情页面数据测试
//        ArrayList<String> ids = new ArrayList<>();
//        ids.add("https://tv.libvio.cc/detail/714889391.html");
//        String s = libvio.detailContent(ids);
//        System.out.println(s);

        // 播放内容数据测试


        // 搜索测试
        String s = libvio.searchContent("我", true);
        System.out.println(s);

    }
}
