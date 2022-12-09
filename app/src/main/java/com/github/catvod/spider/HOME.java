package com.github.catvod.spider;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HOME extends Spider {

    private static final String siteUrl = "http://localhost:8080";

    public String homeContent(boolean filter) {
        try {
            JSONObject result = new JSONObject();
            JSONArray classes = new JSONArray();
            String fenlei_html = OkHttpUtil.string(siteUrl, null);
            Document fenlei_doc = Jsoup.parse(fenlei_html, "UTF-8");
            Elements links = fenlei_doc.getElementsByTag("a");
            for (int i = 1; i < links.size(); i++) {
                if (links.get(i).text().indexOf("/") != -1) {
                    JSONObject jsonObject = new JSONObject();
                    // 获取超链接路径
                    jsonObject.put("type_id", links.get(i).attr("href"));
                    // 获取文本信息
                    jsonObject.put("type_name", links.get(i).text().split("/")[0]);
                    classes.put(jsonObject);
                    result.put("class", classes);
                }
            }
            // System.out.println("=============："+classes);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {
            JSONObject result = new JSONObject();
            JSONArray list = new JSONArray();
            result.put("page", "1");
            result.put("pagecount", 1);
            String curl = siteUrl + "/" + tid;
            String con_html = OkHttpUtil.string(curl, null);
            Document con_doc = Jsoup.parse(con_html, "UTF-8");
            Elements links = con_doc.getElementsByTag("a");
            result.put("limit", links.size() - 1);
            result.put("total", links.size() - 1);
            for (int i = 1; i < links.size(); i++) {
                if (links.get(i).text().indexOf("/") != -1) {
                    JSONObject jsonObject = new JSONObject();
                    // 获取超链接路径
                    jsonObject.put("vod_id", curl + links.get(i).attr("href"));
                    jsonObject.put("vod_pic", curl + links.get(i).attr("href") + "1.jpg");
                    // 获取文本信息
                    jsonObject.put("vod_name", links.get(i).text().split("/")[0]);
                    list.put(jsonObject);
                    result.put("list", list);
                }
            }

            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String detailContent(List<String> ids) {
        try {
            JSONObject result3 = new JSONObject();
            JSONObject info = new JSONObject();

            JSONArray list_info = new JSONArray();

            ArrayList<String> surls = new ArrayList<String>();
            String durl = ids.get(0);

            String deta_html = OkHttpUtil.string(durl, null);
            Document suon_doc = Jsoup.parse(deta_html, "UTF-8");
            Elements links3 = suon_doc.getElementsByTag("a");

            String play_from = "";
            for (int i = 1; i < links3.size(); i++) {
                if (links3.get(i).text().indexOf("/") != -1) {
                    if (i == links3.size() - 1) {
                        play_from = play_from + links3.get(i).text().split("/")[0];
                    } else {
                        play_from = play_from + links3.get(i).text().split("/")[0] + "$$$";
                    }
                }
                String surl = durl + links3.get(i).attr("href");
                String ddeta_html = OkHttpUtil.string(surl, null);
                Document res3_doc = Jsoup.parse(ddeta_html, "UTF-8");
                Elements links4 = res3_doc.getElementsByTag("a");

                String c = "";
                for (int j = 1; j < links4.size(); j++) {
                    String a = "";
                    String b = "";
                    a = links4.get(j).text() + "$";
                    if (j == links4.size() - 1) {
                        b = surl + links4.get(j).attr("href");
                    } else {
                        b = surl + links4.get(j).attr("href") + "#";

                    }

                    c = c + a + b;

                }
                surls.add(c);

            }
            String d = "";
            for (int k = 0; k < surls.size(); k++) {
                if (k == surls.size() - 1) {
                    d = d + surls.get(k);
                } else {
                    d = d + surls.get(k) + "$$$";

                }
            }

            info.put("vod_id", ids.get(0));
            info.put("vod_name", "");
            info.put("vod_pic", durl + "1.jpg");

            info.put("vod_play_from", play_from);
            info.put("vod_play_url", d);
            list_info.put(info);
            result3.put("list", list_info);

            return result3.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }

        return "";
    }

    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {
            JSONObject result = new JSONObject();
            result.put("parse", 0);
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
