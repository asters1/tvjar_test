package com.github.catvod.spider;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.select.Elements;

import com.github.catvod.crawler.Spider;

public class Cb extends Spider {

    private static final String siteUrl = "https://cb9527.com";

    protected HashMap<String, String> getHeaders(String url) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("method", "GET");
        headers.put("User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");
        return headers;
    }

    public String homeContent(boolean filter) {
        JSONObject result = new JSONObject();
        JSONArray classes = new JSONArray();

        JSONObject chunv = new JSONObject();

        chunv.put("type_id", "/wd/处女.html");
        chunv.put("type_name", "处女");

        classes.put(chunv);
        result.put("class", classes);
        return result.toString();
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        JSONObject result = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        String cateUrl = siteUrl + "/index.php/vod/search/page/" + pg + tid;
        String content = OkHttpUtil.string(cateUrl, getHeaders(cateUrl));
        Document document = Jsoup.parse(content);
        Elements li_videos = document.select(".videos>li");
        for (int i = 0; i < li_videos.size(); i++) {
            JSONObject info = new JSONObject();
            String href = siteUrl + li_videos.get(i).getElementsByTag("a").attr("href");
            String img = li_videos.get(i).getElementsByTag("img").attr("src");
            String title = li_videos.get(i).getElementsByTag("img").attr("alt");
            info.put("vod_id", href);
            info.put("vod_name", title);
            info.put("vod_pic", img);
            jSONArray.put(info);
        }
        result.put("page", Integer.parseInt(pg));
        result.put("pagecount", Integer.MAX_VALUE);
        result.put("limit", 16);
        result.put("total", Integer.MAX_VALUE);
        result.put("list", jSONArray);
        return result.toString();

    }

    public String detailContent(List<String> ids) {
        // System.out.println(ids.get(0));
        String content = OkHttpUtil.string(ids.get(0), getHeaders(ids.get(0)));
        Document document = Jsoup.parse(content);
        Elements e = document.select(".web_list_detail>.play");
        Elements p = document.select(".web_list_detail>.panel-body>.pic");
        String href = siteUrl + e.get(0).getElementsByTag("a").attr("href");
        String pic = p.get(0).getElementsByTag("img").attr("src");
        String title = p.get(0).getElementsByTag("img").attr("alt");

        JSONObject result = new JSONObject();
        JSONArray list_info = new JSONArray();
        JSONObject info = new JSONObject();

        info.put("vod_name", title);
        info.put("vod_id", ids.get(0));
        info.put("vod_pic", pic);
        info.put("vod_play_from", "1");
        info.put("vod_play_url", "第1集$" + href);

        list_info.put(info);
        result.put("list", list_info);

        return result.toString();
    }

    public String playerContent(String flag, String id, List<String> vipFlags) {
        JSONObject result = new JSONObject();
        result.put("header", "");
        result.put("parse", 0);
        result.put("playUrl", "");
        String content = OkHttpUtil.string(id, getHeaders(id));
        String pattern = "url\":\"(http.*?m3u8)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(content);
        if (m.find()) {
            String url_format = m.group(1);
            String url = url_format.replaceAll("\\\\", "");
            result.put("url", url);

        } else {
            result.put("url", "");
        }

        return result.toString();
    }
}
