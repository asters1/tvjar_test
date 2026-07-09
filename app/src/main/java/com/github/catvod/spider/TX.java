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

public class TX extends Spider {

  // https://v.qq.com/x/cover/mzc00200sb2g4xz/d4102rvy6uj.html
  private static final String siteUrl = "https://v.qq.com";
  //ext为外部给的字符串
  public String ext = "";

  public void init(Context context,String ext) {
    super.init(context,ext);
    this.ext=ext;
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
      JSONObject filter_obj=new JSONObject(f_str);




      result.put("class", classes);
      result.put("filters",filter_obj);
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
      String v_list_u="https://v.%71%71.com/x/bu/pagesheet/list?_all=1&append=1&channel="+tid+"&listpage=1&offset="+(page-1)*21+"&pagesize=21&iarea=-1";
      // if(extend==o)
      if (extend.isEmpty()) {
        v_list_u=v_list_u+"&gender=2";
      }else{
        v_list_u=v_list_u+"&gender="+extend.get("gender");

      }
      String res = OkHttpUtil.string(v_list_u, getHeaders());
      // System.out.println(res);
        Elements list_el = Jsoup.parse(res).select("[class=list_item]");
        for (int i = 0; i < list_el.size(); i++) {
          JSONObject vod = new JSONObject();
          String vod_id=list_el.get(i).select("a").attr("data-float");
          String vod_name=list_el.get(i).select("img").attr("alt");

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
