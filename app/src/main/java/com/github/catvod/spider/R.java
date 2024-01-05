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

public class R extends Spider {

  private static final String siteUrl = "https://91gaoqingheiliao.com";
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
      JSONObject A = new JSONObject();
      JSONObject B = new JSONObject();
      JSONObject C = new JSONObject();
      JSONObject D = new JSONObject();
      JSONObject E = new JSONObject();

      A.put("type_id", "/t/111");
      A.put("type_name", "国产1区");

      B.put("type_id", "/t/120");
      B.put("type_name", "国产2区");

      C.put("type_id", "/t/86");
      C.put("type_name", "视频区");

      D.put("type_id", "/t/89");
      D.put("type_name", "女优区");

      E.put("type_id", "/t/225");
      E.put("type_name", "番号区");

      classes.put(A);
      classes.put(B);
      classes.put(C);
      classes.put(D);
      classes.put(E);

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


      String   cateUrl="";
      if (extend.size()==0){
        cateUrl=siteUrl+tid+"-"+pg+"/";

      }
      // System.out.println(cateUrl);
      String res = OkHttpUtil.string(cateUrl, getHeaders());
      Elements list_el = Jsoup.parse(res).select("[class=row row-space8 row-m-space8]").get(0).select("[class=col-25 col-m-12 mb20]");
      for (int i = 0; i < list_el.size(); i++) {
        JSONObject vod = new JSONObject();
        JSONObject res_detail=new JSONObject();
        String vod_name = list_el.get(i).select("img").attr("alt");
        String vod_id = list_el.get(i).select("a").attr("href").replace("/voddetail/", "");
        String vod_pic = CompletionUrl( list_el.get(i).select("img").attr("src"));
        String vod_remarks =  list_el.get(i).select("small").text();
        res_detail.put("vod_id", vod_id);
        res_detail.put("vod_name", vod_name);
        res_detail.put("vod_pic", vod_pic);
        res_detail.put("vod_remarks", vod_remarks);

        vod.put("vod_id", res_detail.toString() );
        vod.put("vod_name", vod_name);
        vod.put("vod_pic", vod_pic);
        vod.put("vod_remarks", vod_remarks);
        System.out.println(vod);
        jSONArray.put(vod);

      }
      result.put("page", Integer.parseInt(pg));
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
      JSONObject result=new JSONObject();
JSONArray list=new JSONArray();


      JSONObject vod=new JSONObject(ids.get(0));
      vod.put("vod_play_from", vod.getString("vod_name"));
      vod.put("vod_play_url", "第一集$"+siteUrl+"/v/"+vod.getString("vod_id"));
      list.put(vod);
      result.put("list", list);
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
  protected HashMap<String, String> getHeaders() {
    HashMap<String, String> headers = new HashMap<>();
    headers.put("User-Agent",
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");
    return headers;
  }
  protected String CompletionUrl(String u){
    if (u.startsWith("http")){
      return u;
    }else if (u.startsWith("/")){
      return siteUrl+u;
    }

    return "";

  }

}
