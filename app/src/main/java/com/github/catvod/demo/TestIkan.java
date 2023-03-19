package com.github.catvod.demo;

import com.github.catvod.spider.Ikan;

public class TestIkan {
    public static void main(String[] args) {
        Ikan ikan = new Ikan();
        // 首页测试，输出...
//        String s = ikan.homeContent(true);
//        System.out.println(s);

        // 分类页面数据测试
//        HashMap<String, String> map = new HashMap<>();
//        map.put("area", "大陆");
//        String s = ikan.categoryContent("/vodshow/1", "1", true, map);
//        System.out.println(s);

        // 详情页面数据测试
//        ArrayList<String> ids = new ArrayList<>();
//        ids.add("https://ikanys.tv/voddetail/28393");
//        String s = ikan.detailContent(ids);
//        System.out.println(s);

        // 搜索测试
        String s = ikan.searchContent("我", true);
        System.out.println(s);


        // 播放内容数据测试

    }
}
