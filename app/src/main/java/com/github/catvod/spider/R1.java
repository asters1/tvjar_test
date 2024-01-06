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

public class R1 extends Spider {

  private static final String siteUrl = "https://hd.hdys2.com";
  //ext为外部给的字符串
  public String ext = "";

  public void init(Context context,String ext) {
    super.init(context,ext);
    this.ext=ext;
  }

  public String homeContent(boolean filter) {
    try {
      // https://theporndude.com/zh

      // https://hd.hdys2.com/vodshow/2-----------.html
      // https://hd.hdys2.com/vodshow/8--------2---.html

      JSONObject result = new JSONObject();
      JSONArray classes = new JSONArray();

      JSONObject A = new JSONObject();
      JSONObject B = new JSONObject();
      JSONObject C = new JSONObject();
      JSONObject D = new JSONObject();

      A.put("type_id", "/vodshow/1--------");
      A.put("type_name", "有码");

      B.put("type_id", "/vodshow/2--------");
      B.put("type_name", "无码");

      C.put("type_id", "/vodshow/3--------");
      C.put("type_name", "国产");
      
      D.put("type_id", "/vodshow/4--------");
      D.put("type_name", "欧美");

      classes.put(A);
      classes.put(B);
      classes.put(C);
      classes.put(D);

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

      String cateUrl=siteUrl+tid+pg+"---.html";
      String res=OkHttpUtil.string(cateUrl, GetHeaders());
      Elements list_el = Jsoup.parse(res).select("[class=stui-vodlist clearfix]").get(0).select("[class=col-md-4 col-sm-4 col-xs-2]");
      for (int i = 0; i < list_el.size(); i++) {

        JSONObject vod = new JSONObject();
        JSONObject res_detail=new JSONObject();

        String vod_id=list_el.get(i).select("a").attr("href").replace("/voddetail/", "").replace(".html", "");
        // System.out.println(vod_id);
        String vod_name=list_el.get(i).select("a").attr("title");
        String vod_pic=list_el.get(i).select("img").attr("data-original");
        String vod_remarks=list_el.get(i).select("[class=pic-tag pic-tag-b]").text();


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
      vod.put("vod_play_url", "第一集$"+siteUrl+"/vodplay/"+vod.getString("vod_id")+"-1-1.html");
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
      JSONObject result = new JSONObject();
      JSONArray jSONArray = new JSONArray();
      String searchUrl= "https://hd.hdys2.com/vodsearch/-------------.html?wd="+key;
      String res=OkHttpUtil.string(searchUrl, GetHeaders());
      Elements list_el = Jsoup.parse(res).select("[class=stui-vodlist clearfix]").get(0).select("[class=col-md-4 col-sm-2 col-xs-1]");
      for (int i = 0; i < list_el.size(); i++) {

        JSONObject vod = new JSONObject();
        JSONObject res_detail=new JSONObject();

        String vod_id=list_el.get(i).select("a").attr("href").replace("/voddetail/", "").replace(".html", "");
        // System.out.println(vod_id);
        String vod_name=list_el.get(i).select("a").attr("title");
        String vod_pic=list_el.get(i).select("img").attr("data-original");
        String vod_remarks=list_el.get(i).select("[class=pic-tag pic-tag-b]").text();


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
      result.put("list", jSONArray);

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
  private  HashMap<String,String>  GetHeaders(){
    try {
      HashMap<String, String> headers = new HashMap<>();
      headers.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
      
    } catch (Exception e) {
      // TODO: handle exception
    }
    return null;


  }
}
