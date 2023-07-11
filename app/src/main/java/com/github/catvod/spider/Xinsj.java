package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class Xinsj extends Spider {

    private static final String siteUrl = "https://www.6080dy3.com";

    public void init(Context context) {
        super.init(context);
    }

    public String homeContent(boolean filter) {
        try {
            JSONObject result = new JSONObject();
            JSONArray classes = new JSONArray();

            JSONObject dianying = new JSONObject();
            JSONObject dianshiju = new JSONObject();
            JSONObject dongman = new JSONObject();
            JSONObject zongyi = new JSONObject();

            dianying.put("type_id", "/vodshow/1--------");
            dianying.put("type_name", "电影");

            dianshiju.put("type_id", "/vodshow/2--------");
            dianshiju.put("type_name", "电视剧");

            zongyi.put("type_id", "/vodshow/3--------");
            zongyi.put("type_name", "综艺");

            dongman.put("type_id", "/vodshow/4--------");
            dongman.put("type_name", "动漫");
            classes.put(dianying);
            classes.put(dianshiju);
            classes.put(zongyi);
            classes.put(dongman);

            result.put("class", classes);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {

            JSONObject result = new JSONObject();
            JSONArray jSONArray = new JSONArray();

            String url = siteUrl + tid + pg + "---.html";

            int page = Integer.parseInt(pg);
            String res = OkHttpUtil.string(url, getHeaders(url));
            // System.out.println(res);
            Elements list_el = Jsoup.parse(res).select("[class=module-items]").select("[class=module-item]");
            // System.out.println(list_el);

            for (int i = 0; i < list_el.size(); i++) {
                JSONObject vod = new JSONObject();
                String vod_pic = list_el.get(i).select("[class=module-item-pic]").select("img").attr("data-src");
                String vod_id = list_el.get(i).select("[class=module-item-pic]").select("a").attr("href");
                String vod_name = list_el.get(i).select("[class=module-item-pic]").select("a").attr("title");
                String vod_remarks = list_el.get(i).select("[class=module-item-text]").text();
                // System.out.println(vod_pic);
                vod.put("vod_id", vod_id);
                vod.put("vod_name", vod_name);
                vod.put("vod_pic", vod_pic);
                vod.put("vod_remarks", vod_remarks);
                jSONArray.put(vod);
            }
            result.put("page", page);
            result.put("pagecount", Integer.MAX_VALUE);
            result.put("limit", 40);
            result.put("total", Integer.MAX_VALUE);
            result.put("list", jSONArray);
            return result.toString();

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String detailContent(List<String> ids) {
        try {

            JSONObject result = new JSONObject();
            JSONObject info = new JSONObject();
            JSONArray list_info = new JSONArray();

            String url = siteUrl + ids.get(0);

            String content = OkHttpUtil.string(url, getHeaders(url));

            Elements sourcesName_el = Jsoup.parse(content).select("[class=module-tab-content]")
                    .select("[class=module-tab-item tab-item]");
            ArrayList<String> playFroms = new ArrayList<String>();
            Elements sourcesUrl_el = Jsoup.parse(content).select("[class=sort-item]");
            // System.out.println(sourcesUrl_el);

            ArrayList<String> play_from_array = new ArrayList<String>();
            for (int i = 0; i < sourcesUrl_el.size(); i++) {
                Elements url_a = sourcesUrl_el.get(i).select("a");

                ArrayList<String> arr = new ArrayList<String>();
                for (int j = 0; j < url_a.size(); j++) {
                    arr.add(url_a.get(j).select("span").text() + "$" + url_a.get(j).attr("href"));
                }
                String sources = TextUtils.join("#", arr);
                play_from_array.add(sources);
            }

            String vod_play_url = TextUtils.join("$$$", play_from_array);
            // System.out.println(vod_play_url);
            for (int i = 0; i < sourcesName_el.size(); i++) {

                String playfrom = sourcesName_el.get(i).attr("data-dropdown-value");
                playFroms.add(playfrom);

            }

            String vod_play_from = TextUtils.join("$$$", playFroms);

            Element v_info_el = Jsoup.parse(content).select("[class=video-info]").get(0);
            // System.out.println(v_info_el);
            String vod_name = v_info_el.select("[class=page-title]").text();
            String vod_pic = Jsoup.parse(content).select("[class=lazyload]").get(0).attr("data-src");
            String type_name = v_info_el.select("[class=tag-link]").text();
            String vod_year = "年份";
            String vod_area = "地区";
            String vod_remarks = "提示信息";
            String vod_actor = "主演";
            String vod_director = "导演";
            String vod_content = "简介";

            info.put("vod_id", ids.get(0));
            info.put("vod_name", vod_name);
            info.put("vod_pic", vod_pic);
            info.put("type_name", type_name);
            info.put("vod_year", vod_year);
            info.put("vod_area", vod_area);
            info.put("vod_remarks", vod_remarks);
            info.put("vod_actor", vod_actor);
            info.put("vod_director", vod_director);
            info.put("vod_content", vod_content);

            info.put("vod_play_from", vod_play_from);
            info.put("vod_play_url", vod_play_url);

            list_info.put(info);
            result.put("list", list_info);

            return result.toString();
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
            String url = siteUrl + id;
            String content = OkHttpUtil.string(url, getHeaders(url));
            // System.out.println(content);
            Element v_info_el = Jsoup.parse(content).select("[id=bfurl]").get(0);
            String play_url = v_info_el.attr("href");
            JSONObject result = new JSONObject();
            result.put("parse", 0);
            result.put("header", "");
            result.put("playUrl", play_url);
            result.put("url", "");
            return result.toString();

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public static HashMap<String, String> Myq(String str, String str2, String str3, String str4) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(str, str2);
        hashMap.put(str3, str4);
        return hashMap;
    }

    protected final HashMap<String, String> getHeaders(String str) {
        str = "method";
        String d = "GET";
        String d2 = "Upgrade-Insecure-Requests";
        String d3 = "1";
        HashMap<String, String> yq = Myq(str, d, d2, d3);
        yq.put("DNT", d3);
        yq.put("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");
        yq.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        yq.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        return yq;
    }

    // ====================
    void printLog(String key, String value) {
        try {

            String str = key + "=" + value;
            String str1 = "http://localhost:8080/?" + str;
            String res = OkHttpUtil.string(str1, null);
            System.out.println(res);
        } catch (Exception e) {
        }
    }
    // ====================
}
