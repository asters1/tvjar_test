package com.github.catvod.demo;

import com.github.catvod.spider.SixV;

public class TestSixV {
    public static void main(String[] args) {
        SixV sixV = new SixV();
        // 首页测试，输出...
//        String s = freeOK.homeContent(true);
//        System.out.println(s);

        // 分类页面数据测试
//        String s = sixV.categoryContent("/xijupian", "1", true, new HashMap<>());
//        System.out.println(s);

        // 详情页面数据测试
//        ArrayList<String> ids = new ArrayList<>();
//        ids.add("https://www.66s.cc/xijupian/20346.html");
//        ids.add("https://www.66s.cc/xijupian/20531.html");
//        String s = sixV.detailContent(ids);
//        System.out.println(s);

        // 播放内容数据测试


        // 搜索测试
        String s = sixV.searchContent("我", true);
        System.out.println(s);

    }
}
