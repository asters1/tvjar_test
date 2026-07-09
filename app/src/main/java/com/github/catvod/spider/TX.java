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
import java.util.ArrayList;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class TX extends Spider {
    //IDEA 格式化代码ctrl+alt+l
    // https://v.qq.com/x/cover/mzc00200sb2g4xz/d4102rvy6uj.html
    private static final String siteUrl = "https://v.qq.com";
    //ext为外部给的字符串
    public String ext = "";

    public void init(Context context, String ext) {
        super.init(context, ext);
        this.ext = ext;
    }

    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36");
        return headers;
    }

    public String homeContent(boolean filter) {
        try {
            // System.out.println(ext);
            JSONObject result = new JSONObject();
            JSONArray classes = new JSONArray();

            JSONObject se = new JSONObject();
            JSONObject dm = new JSONObject();


            se.put("type_name", "少儿");
            se.put("type_id", "child");


            dm.put("type_name", "动漫");
            dm.put("type_id", "cartoon");

            classes.put(se);
            classes.put(dm);
            // ======================

            String f_str = "{\"child\":[{\"key\":\"gender\",\"name\":\"性别\",\"value\":[{\"n\":\"男孩\",\"v\":\"2\"},{\"n\":\"女孩\",\"v\":\"1\"}]}]}";
            JSONObject filter_obj = new JSONObject(f_str);


            result.put("class", classes);
            result.put("filters", filter_obj);
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
            int page = Integer.parseInt(pg);
            String v_list_u = "https://v.%71%71.com/x/bu/pagesheet/list?_all=1&append=1&channel=" + tid + "&listpage=1&offset=" + (page - 1) * 21 + "&pagesize=21&iarea=-1";
            // if(extend==o)
            if (extend.isEmpty()) {
                v_list_u = v_list_u + "&gender=2";
            } else {
                v_list_u = v_list_u + "&gender=" + extend.get("gender");

            }
            String res = OkHttpUtil.string(v_list_u, getHeaders());
            // System.out.println(res);
            Elements list_el = Jsoup.parse(res).select("[class=list_item]");
            for (int i = 0; i < list_el.size(); i++) {
                JSONObject vod = new JSONObject();
                String vod_id = list_el.get(i).select("a").attr("data-float");
                String vod_name = list_el.get(i).select("img").attr("alt");

                String vod_pic = list_el.get(i).select("img").attr("src");
                String vod_remarks = list_el.get(i).select("a").get(0).text();
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
            // System.out.println(result.toString());


        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String detailContent(List<String> ids) {
        try {
            String[] flag_list = {"qq", "虾米"};
            ArrayList<String> vod_play_froms = new ArrayList<String>();

            String cid = ids.get(0);
            String sourceId = cid;
            JSONObject result = new JSONObject();
            JSONObject info = new JSONObject();
            JSONArray list_info = new JSONArray();
            ArrayList<String> urls = new ArrayList<String>();
            String vod_play_from = "";
            String vod_play_url = "";

            JSONArray video_lists = new JSONArray();
            String play_url = "";
            ArrayList<String> vod_play_urls = new ArrayList<String>();
            String detailUrl = "https://node.video.%71%71.com/x/api/float_vinfo2?cid=" + cid;
            String html = OkHttpUtil.string(detailUrl, getHeaders());
//      System.out.println(html);
            JSONObject v_obj = new JSONObject(html);

            info.put("vod_id", ids.get(0));
            info.put("vod_name", v_obj.getJSONObject("c").getString("title"));
            info.put("type_name", v_obj.getJSONArray("typ").toString());
            info.put("vod_actor", v_obj.getJSONArray("nam").toString());
            info.put("vod_year", v_obj.getJSONObject("c").getString("year"));
            info.put("vod_content", v_obj.getJSONObject("c").getString("description"));
            info.put("vod_remarks", v_obj.getString("rec"));
            info.put("vod_pic", v_obj.getJSONObject("c").getString("pic"));
            // System.out.println(info);
            video_lists = v_obj.getJSONObject("c").getJSONArray("video_ids");
            String url = "https://v.qq.com/x/cover/" + sourceId + ".html";
//            System.out.println(url);

            if (video_lists.length() == 1) {
                String vid = video_lists.getString(0);
                url = "https://v.qq.com/x/cover/" + cid + "/" + vid + ".html";
                String title = "在线播放";
                play_url = title + "$" + url;
            } else if (video_lists.length() > 1) {
                for (int i = 0; i < video_lists.length(); i++) {
                    String vid = video_lists.getString(i);
                    String title = "第" + (i + 1) + "集";
                    url = "https://v.qq.com/x/cover/" + cid + "/" + vid + ".html";
                    urls.add(title + "$" + url);
                }
                play_url = TextUtils.join("#", urls);
            }
            for (int i = 0; i < flag_list.length; i++) {
                vod_play_urls.add(play_url);
                vod_play_froms.add(flag_list[i]);
            }
            vod_play_url = TextUtils.join("$$$", vod_play_urls);
            vod_play_from = TextUtils.join("$$$", vod_play_froms);
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
            if(flag=="qq"){

                return "";

            }else if(flag=="虾米"){

                return "";
            }

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
    public static JSONArray slice(JSONArray array, int start, int end) {
        JSONArray result = new JSONArray();

        int total = array.length();
        // 边界修正
        start = Math.max(start, 0);
        end = Math.min(end, total);
        for (int i = start; i < end; i++) {
            result.put(array.get(i));
        }
        return result;
    }

    public static ArrayList<String> jsonArrayToArrayList(JSONArray jsonArray) {
        ArrayList<String> list = new ArrayList<>();
        if (jsonArray == null) {
            return list;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            String val = jsonArray.getString(i);
            list.add(val);
        }
        return list;
    }
}
