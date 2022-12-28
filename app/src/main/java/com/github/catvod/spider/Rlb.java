package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class Rlb extends Spider {

    private static final String siteUrl = "rlubasp.cc";

    public void init(Context context) {
        super.init(context);
    }

    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");
        return headers;
    }

    public String homeContent(boolean filter) {
        try {
            JSONObject result = new JSONObject();
            JSONArray classes = new JSONArray();

            JSONObject madou = new JSONObject();
            JSONObject jyzp = new JSONObject();
            JSONObject tm = new JSONObject();
            JSONObject mt = new JSONObject();

            madou.put("type_id", "/index.php/vodtype/161");
            madou.put("type_name", "麻豆");
            jyzp.put("type_id", "/index.php/vodtype/162");
            jyzp.put("type_name", "酒衣");
            tm.put("type_id", "/index.php/vodtype/163");
            tm.put("type_name", "天美");
            mt.put("type_id", "/index.php/vodtype/164");
            mt.put("type_name", "蜜桃");



            classes.put(madou);
            classes.put(jyzp);
            classes.put(tm);
            classes.put(mt);
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

            String categoryContent_url = "https://" + siteUrl + tid + "-" + pg + ".html";
            String res = OkHttpUtil.string(categoryContent_url, getHeaders());

            Elements list_el = Jsoup.parse(res).select("[class=row gutter-20]")
                    .select("[class=col-6 col-sm-4 col-lg-3]");
            for (int i = 0; i < list_el.size(); i++) {
                JSONObject vod = new JSONObject();

                String vod_name = list_el.get(i).select(".title").text();
                String vod_id = list_el.get(i).select("[class=img-box cover-md]").get(0).getElementsByTag("a")
                        .attr("href");
                String vod_pic = list_el.get(i).select("[class=img-box cover-md]").get(0).getElementsByTag("img")
                        .attr("data-src");

                vod.put("vod_id", vod_id);

                vod.put("vod_name", vod_name);
                vod.put("vod_pic", vod_pic);
                jSONArray.put(vod);
            }
            result.put("page", Integer.parseInt(pg));
            result.put("pagecount", Integer.MAX_VALUE);
            result.put("limit", 28);
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

            String durl = "https://" + siteUrl + ids.get(0);
            String content = OkHttpUtil.string(durl, getHeaders());
            Elements sources = Jsoup.parse(content).select("[class=header-left]");
            String vod_name = sources.get(0).getElementsByTag("h4").text();
            String vod_play_url = "1$" + durl;
            String vod_play_from = "1";
            info.put("vod_name", vod_name);
            info.put("vod_play_url", vod_play_url);
            info.put("vod_play_from", vod_play_from);

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

            JSONObject result = new JSONObject();

            result.put("parse", 1);
            result.put("header", "");
            result.put("playUrl", "");
            result.put("url", id);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }
}
