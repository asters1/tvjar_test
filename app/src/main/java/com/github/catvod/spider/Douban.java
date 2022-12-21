package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class Douban extends Spider {

    private static final String siteUrl = "https://movie.douban.com";

    public String homeContent(boolean filter) {
        try {
            // 电视剧
            // https://m.douban.com/rexxar/api/v2/tv/recommend?refresh=0&start=20&count=20&selected_categories={"地区":"华语"}&uncollect=false&tags=华语
            // 电影
            // https://m.douban.com/rexxar/api/v2/movie/recommend?refresh=0&start=0&count=20&selected_categories={}&uncollect=false&tags=
            JSONObject result = new JSONObject();
            JSONArray classes = new JSONArray();

            JSONObject dianying = new JSONObject();
            JSONObject dianshiju = new JSONObject();
            JSONObject dongman = new JSONObject();
            JSONObject zongyi = new JSONObject();

            dianying.put("type_name", "电影");
            dianying.put("type_id", "/rexxar/api/v2/movie");

            dianshiju.put("type_name", "电视剧");
            dianshiju.put("type_id", "/rexxar/api/v2/tv");

            // dongman.put("type_name", "动漫");

            // zongyi.put("type_name", "综艺");
            classes.put(dianying);
            classes.put(dianshiju);

            result.put("class", classes);
            return result.toString();

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String detailContent(List<String> ids) {
        try {

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String searchContent(String key, boolean quick) {
        try {

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");
        return headers;
    }
}
