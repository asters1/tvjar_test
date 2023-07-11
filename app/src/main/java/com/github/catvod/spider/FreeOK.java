package com.github.catvod.spider;

import android.content.Context;

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

public class FreeOK extends Spider {
    @Override
    public void init(Context context) {
        super.init(context);
    }

    private static final String siteUrl = "https://www.freeok.vip";

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

            JSONObject rebopaihang = new JSONObject();
            JSONObject dianying = new JSONObject();
            JSONObject dianshiju = new JSONObject();
            JSONObject dongman = new JSONObject();
            JSONObject zongyi = new JSONObject();

            rebopaihang.put("type_id", "/label/new.html");
            rebopaihang.put("type_name", "热播排行");
            dianying.put("type_id", "/vodshow/1--------");
            dianying.put("type_name", "电影");

            dianshiju.put("type_id", "/vodshow/2--------");
            dianshiju.put("type_name", "电视剧");

            dongman.put("type_id", "/vodshow/3--------");
            dongman.put("type_name", "动漫");

            zongyi.put("type_id", "/vodshow/4--------");
            zongyi.put("type_name", "综艺");

            classes.put(rebopaihang);
            classes.put(dianying);
            classes.put(dianshiju);
            classes.put(dongman);
            classes.put(zongyi);

            result.put("class", classes);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {

            if (tid.equals("/label/new.html")) {
                JSONObject result = new JSONObject();
                JSONArray jSONArray = new JSONArray();

                String cateUrl = siteUrl + tid;
                String content = OkHttpUtil.string(cateUrl, getHeaders());
                Elements list_el = Jsoup.parse(content)

                        .select("[class=module-items module-card-items]").get(0)
                        .select("[class=module-card-item-poster]");

                for (int i = 0; i < list_el.size(); i++) {
                    JSONObject vod = new JSONObject();
                    String vod_pic = list_el.get(i).select("img").attr("data-original");
                    String vod_name = list_el.get(i).select("img").attr("alt");
                    String vod_id = siteUrl + list_el.get(i).attr("href");
                    int b = i + 1;
                    String vod_remarks = "第" + b + "名";
                    vod.put("vod_id", vod_id);
                    vod.put("vod_name", vod_name);
                    vod.put("vod_pic", vod_pic);
                    vod.put("vod_remarks", vod_remarks);
                    jSONArray.put(vod);
                }
                result.put("page", Integer.parseInt(pg));
                result.put("pagecount", 1);
                result.put("limit", 100);
                result.put("total", Integer.MAX_VALUE);
                result.put("list", jSONArray);
                return result.toString();
            } else {
                JSONObject result = new JSONObject();
                JSONArray jSONArray = new JSONArray();

                String cateUrl = siteUrl + tid + pg + "---.html";
                // cateUrl = "http://httpbin.org/get";
                String content = OkHttpUtil.string(cateUrl, getHeaders());

                Elements list_el = Jsoup.parse(content)
                        .select("[class=module-items module-poster-items-base]").get(0).select("a");

                for (int i = 0; i < list_el.size(); i++) {
                    JSONObject vod = new JSONObject();
                    Element item = list_el.get(i);
                    String vod_id = item.attr("href");
                    String vod_name = item.attr("title");
                    String vod_pic = item.select("img").attr("data-original");
                    String vod_remarks = item.select(".module-item-note").text();
                    vod.put("vod_id", siteUrl + vod_id);
                    vod.put("vod_name", vod_name);
                    vod.put("vod_pic", vod_pic);
                    vod.put("vod_remarks", vod_remarks);
                    jSONArray.put(vod);

                }
                result.put("page", Integer.parseInt(pg));
                result.put("pagecount", Integer.MAX_VALUE);
                result.put("limit", 40);
                result.put("total", Integer.MAX_VALUE);
                result.put("list", jSONArray);
                return result.toString();
            }

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

            String durl = ids.get(0);
            String content = OkHttpUtil.string(durl, getHeaders());
            Elements sources = Jsoup.parse(content).select("[class=module-list sort-list tab-list his-tab-list]");
            String vod_play_url = "";
            String vod_play_from = "";
            for (int i = 0; i < sources.size(); i++) {
                int b = i + 1;
                vod_play_from = vod_play_from + "源" + b + "$$$";

                for (int j = 0; j < sources.get(i).select("a").size(); j++) {
                    if (j < sources.get(i).select("a").size() - 1) {

                        vod_play_url = vod_play_url + sources.get(i).select("a").get(j).select("span").text() + "$"
                                + siteUrl + sources.get(i).select("a").get(j).attr("href") + "#";
                    } else {
                        vod_play_url = vod_play_url + sources.get(i).select("a").get(j).select("span").text() + "$"
                                + siteUrl + sources.get(i).select("a").get(j).attr("href") + "$$$";

                    }
                }
            }
            String title = Jsoup.parse(content).select(".module-info-heading").get(0).getElementsByTag("h1").text();
            String pic = Jsoup.parse(content)
                    .select(".module-main>.module-info-poster>.module-item-cover>.module-item-pic>[class=ls-is-cached lazy lazyload]")
                    .get(0)
                    .attr("data-original");
            info.put("vod_id", ids.get(0));
            info.put("vod_name", title);
            info.put("vod_pic", pic);

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

            String search_url = "https://www.freeok.vip/vodsearch/-------------.html?wd=" + key;
            String content = OkHttpUtil.string(search_url, getHeaders());
            Elements list_el = Jsoup.parse(content).select("[class=module-card-item module-item]");
            JSONObject result = new JSONObject();
            JSONArray list = new JSONArray();
            for (int i = 0; i < list_el.size(); i++) {
                JSONObject info = new JSONObject();
                String vod_id = siteUrl + list_el.get(i).select(".module-card-item-poster").attr("href");
                String vod_name = list_el.get(i).select("strong").text();
                String vod_pic = list_el.get(i).select("[class=lazy lazyload]").attr("data-original");
                String vod_remarks = list_el.get(i).select("[class=module-item-note]").text();
                info.put("vod_id", vod_id);
                info.put("vod_name", vod_name);
                info.put("vod_pic", vod_pic);
                info.put("vod_remarks", vod_remarks);
                list.put(info);

            }
            result.put("list", list);
            return result.toString();

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
