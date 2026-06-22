package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import java.util.HashMap;
import java.util.List;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class Test extends Spider {

    private static final String siteUrl = "";
    //ext为外部给的字符串
    public String ext = "";

    public void init(Context context,String ext) {
        super.init(context,ext);
        this.ext=ext;
    }

    public String homeContent(boolean filter) {
        try {
            JSONObject result = new JSONObject();
            JSONArray classes = new JSONArray();

            JSONObject dianying = new JSONObject();
            JSONObject dianshiju = new JSONObject();

            dianying.put("type_name", "电影");
            dianying.put("type_id", "movie");

            dianshiju.put("type_name", "电视剧");
            dianshiju.put("type_id", "tv");
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
            JSONObject result = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < 6; i++) {
                JSONObject vod = new JSONObject();

                vod.put("vod_name", "斗破苍穹");
                vod.put("vod_id", i + "");


                // System.out.println(vod_pic);

                jSONArray.put(vod);
            }

            result.put("page", "1");
            result.put("pagecount", "1");
            result.put("limit", Integer.MAX_VALUE);
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
            String vid = ids.get(0);

            JSONObject item = new JSONObject();
            item.put("vod_id", vid);
            item.put("vod_name", "测试影片");
            item.put("quickSearch", "流浪地球");

            JSONObject result = new JSONObject();
            result.put("list", new JSONArray().put(item));

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

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
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
