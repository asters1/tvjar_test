package com.github.catvod.spider;

import com.github.catvod.crawler.SpiderDebug;

import java.util.HashMap;

import com.github.catvod.crawler.Spider;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class TX extends Spider {

    private static final String siteUrl = "https://v.qq.com";

    protected HashMap<String, String> getHeaders(String url) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("method", "GET");
        headers.put("User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");
        return headers;
    }

    private String formatUrl(String Url) {
        if (Url.startsWith("//")) {

            return "http:" + Url;
        }

        return "";
    }

    public String homeContent(boolean filter) {
        try {

            JSONObject result = new JSONObject();
            JSONArray classes = new JSONArray();

            JSONObject dianying = new JSONObject();
            JSONObject dianshiju = new JSONObject();
            JSONObject dongman = new JSONObject();
            JSONObject shaoer = new JSONObject();
            JSONObject jilupian = new JSONObject();

            JSONObject doudou = new JSONObject();
            JSONObject yangyang = new JSONObject();

            doudou.put("type_id", "doudou");
            doudou.put("type_name", "豆豆");

            yangyang.put("type_id", "yangyang");
            yangyang.put("type_name", "洋洋");

            dianying.put("type_id", "movie");
            dianying.put("type_name", "电影");

            dianshiju.put("type_id", "tv");
            dianshiju.put("type_name", "电视剧");

            dongman.put("type_id", "cartoon");
            dongman.put("type_name", "动漫");

            shaoer.put("type_id", "child");
            shaoer.put("type_name", "少儿");

            jilupian.put("type_id", "doco");
            jilupian.put("type_name", "纪录片");

            classes.put(doudou);
            classes.put(yangyang);
            classes.put(dianshiju);
            classes.put(dianying);
            classes.put(dongman);
            classes.put(shaoer);
            classes.put(jilupian);

            result.put("class", classes);
            return result.toString();
        } catch (Exception e) {

            SpiderDebug.log(e);
        }
        return "";
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {
            if (tid.equals("doudou") || tid.equals("yangyang")) {
                String result, q_url;

                if (tid.equals("doudou")) {
                    q_url = "https://wisteria.cf/raw.githubusercontent.com/asters1/source/master/tvbox/json/doudou.json";
                } else {
                    q_url = "https://wisteria.cf/raw.githubusercontent.com/asters1/source/master/tvbox/json/doudou.json";

                }

                result = OkHttpUtil.string(q_url, getHeaders(q_url));

                return result;

            } else {
                int page = Integer.parseInt(pg);
                String cateUrl = siteUrl
                        + "/x/bu/pagesheet/list?_all=1&append=1&channel=" + tid + "&listpage=1&offset="
                        + (page - 1) * 21
                        + "&pagesize=21&sort=18";
                String content = OkHttpUtil.string(cateUrl, getHeaders(cateUrl));
                String remarks;
                JSONObject result = new JSONObject();
                try {
                    Elements listItems = Jsoup.parse(content).select(".list_item");
                    JSONArray jSONArray = new JSONArray();
                    for (int i = 0; i < listItems.size(); i++) {
                        Element item = listItems.get(i);

                        String Pd = item.select("a").attr("title");
                        String pic = formatUrl(item.select("img").attr("src"));
                        if (item.select(".figure_caption") == null) {
                            remarks = "";
                        } else {
                            remarks = item.select(".figure_caption").text();
                        }
                        String Pd2 = item.select("a").attr("data-float");
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put("vod_id", Pd2);
                        jSONObject2.put("vod_name", Pd);
                        jSONObject2.put("vod_pic", pic);
                        jSONObject2.put("vod_remarks", remarks);
                        jSONArray.put(jSONObject2);
                    }
                    result.put("page", page);
                    result.put("pagecount", Integer.MAX_VALUE);
                    result.put("limit", 90);
                    result.put("total", Integer.MAX_VALUE);
                    result.put("list", jSONArray);
                    return result.toString();
                } catch (Exception e) {

                    SpiderDebug.log(e);
                }

                return "";
            }
        } catch (Exception e) {

            SpiderDebug.log(e);
        }
        return "";
    }
}
