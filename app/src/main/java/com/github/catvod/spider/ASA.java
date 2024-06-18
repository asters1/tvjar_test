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

public class ASA extends Spider {

  private static final String siteUrl = "https://www.crvcd.cc";
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

      JSONObject RM = new JSONObject();
      JSONObject ZX = new JSONObject();
      JSONObject TJ = new JSONObject();
      JSONObject RH = new JSONObject();
      JSONObject GC = new JSONObject();
      JSONObject LL = new JSONObject();

      RM.put("type_id", "/vod/search/by/hits");
      RM.put("type_name", "热门");

      ZX.put("type_id", "/label/new");
      ZX.put("type_name", "最新");

      TJ.put("type_id", "/vodplay/7121-1-1");
      TJ.put("type_name", "推荐");

      RH.put("type_id", "/vodtype/2");
      RH.put("type_name", "日韩");

      GC.put("type_id", "/vodtype/20");
      GC.put("type_name", "国产");

      if (filter) {
        System.out.println("====");
        String filterconfig ="{\"/vodtype/2\":{\"key\": \"类型\",\"name\": \"类型\", \"value\": [{\"n\": \"全部\", \"v\": \"/vodtype/2\" },{\"n\": \"热门\", \"v\": \"/vodshow/2--hits---------\" }]}}";
        result.put("filters", new JSONObject(filterconfig));

      }

      classes.put(RM);
      classes.put(ZX);
      classes.put(TJ);
      classes.put(RH);
      classes.put(GC);

      result.put("class", classes);
      // System.out.println(result.toString());
      return result.toString();






    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
    try {

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
