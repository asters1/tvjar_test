package com.github.catvod.demo;

import com.github.catvod.spider.Bilituys;


public class TestBilituys {
    public static void main(String[] args) {
        Bilituys bilituys = new Bilituys();
        // 首页测试，输出...
//        String s = bilituys.homeContent(true);
//        System.out.println(s);

        // 分类页面数据测试
//        HashMap<String, String> map = new HashMap<>();
//        map.put("area", "大陆");
//        String s = bilituys.categoryContent("1", "1", true, map);
//        System.out.println(s);

        // 详情页面数据测试
//        ArrayList<String> ids = new ArrayList<>();
//        ids.add("https://www.bilituys.com/bilidetail/18942.html");
//        String s = bilituys.detailContent(ids);
//        System.out.println(s);

        // 搜索测试
        String s = bilituys.searchContent("三体", true);
        System.out.println(s);


        // 播放内容数据测试

    }
}
