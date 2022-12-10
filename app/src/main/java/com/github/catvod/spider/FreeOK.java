package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class FreeOK extends Spider {

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

            rebopaihang.put("type_id", "/label/hot.html");
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

            if (tid.equals("/label/hot.html")) {
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
            ArrayList<String> surls = new ArrayList<String>();
            String durl = ids.get(0);
            String content = OkHttpUtil.string(durl, getHeaders());

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
}
