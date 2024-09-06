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

public class IkanBot extends Spider {

  private static final String siteUrl = "https://v.aikanbot.com";
  //ext为外部给的字符串
  public String ext = "";


  public void init(Context context,String ext) {
    super.init(context,ext);
    this.ext=ext;
  }

  public String homeContent(boolean filter) {
    try {
      // System.out.println(ext);
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
      String u="";
      JSONObject result = new JSONObject();
      JSONArray jSONArray = new JSONArray();
      int page = Integer.parseInt(pg);
      if (pg.equals("1")){

        u= siteUrl+ "/hot/index-"+tid+"-热门.html";
      }else{

        u= siteUrl+ "/hot/index-"+tid+"-热门-p-"+pg+".html";
      }
      String res = OkHttpUtil.string(u, getHeaders());
      Elements list_el = Jsoup.parse(res).select("[class=v-list]").select("[class=item]");
      for (int i = 0; i < list_el.size(); i++) {
        JSONObject vod = new JSONObject();
        String vod_id=list_el.get(i).select("a").attr("href");
        String vod_name=list_el.get(i).select("p").text();

        String vod_pic = list_el.get(i).select("img").attr("data-src");
        vod.put("vod_id", vod_id);
        vod.put("vod_name", vod_name);
        vod.put("vod_pic", vod_pic);
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
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36");
    return headers;
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
