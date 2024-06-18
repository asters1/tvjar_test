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
        String filterconfig ="{\"/vodtype/2\":[{\"key\": \"类型\",\"name\": \"类型\", \"value\": [{\"n\": \"全部\", \"v\": \"/vodtype/2\" },{\"n\": \"热门\", \"v\": \"/vodshow/2--hits------\" }]}],\"/vodtype/20\":[{\"key\": \"类型\",\"name\": \"类型\", \"value\": [{\"n\": \"全部\", \"v\": \"/vodtype/20\" },{\"n\": \"热门\", \"v\": \"/vodshow/20--hits------\" }]}]}";
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
      JSONObject result = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            JSONObject res_detail=new JSONObject();

      String url="";
      if (tid.equals("/vod/search/by/hits")||tid.equals("/label/new")){
      url=siteUrl+tid+"/page/"+pg;
      }else{
      url=siteUrl+tid+"-"+pg;

      }


      for (String key : extend.keySet()) {
        //https://www.crvcd.cc/vodshow/2--hits---------/
        //https://www.crvcd.cc/vodshow/2--hits------2---/
        //https://www.crvcd.cc/vodshow/20--hits------2---/
     url=siteUrl+extend.get(key).trim()+pg+"---";
      }
      System.out.println(url);
      String res=OkHttpUtil.string(url, GetNormalHeaders());
      Elements list_el = Jsoup.parse(res).select("[class=margin-fix]").select("[class=item]");
      System.out.println(list_el.size());
            for (int i = 0; i < list_el.size(); i++) {
                JSONObject vod = new JSONObject();
                String vod_pic = siteUrl+ list_el.get(i).select("img").attr("data-original");
                String vod_id = list_el.get(i).select("a").attr("href");
                String vod_name = list_el.get(i).select("a").attr("title");
                // System.out.println(vod_pic);
                //
                res_detail.put("vod_id", vod_id);
        res_detail.put("vod_name", vod_name);
        res_detail.put("vod_pic", vod_pic);


                vod.put("vod_id", res_detail.toString());
                vod.put("vod_name", vod_name);
                vod.put("vod_pic", vod_pic);
                jSONArray.put(vod);
            }

            result.put("page", pg);
            result.put("pagecount", Integer.MAX_VALUE);
            result.put("limit", 24);
            result.put("total", Integer.MAX_VALUE);
            result.put("list", jSONArray);
            return result.toString();

      // System.out.println(content);


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
      vod.put("vod_play_url", "第一集$"+siteUrl+vod.getString("vod_id"));
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
  HashMap<String,String>  GetNormalHeaders(){
    try {
      HashMap<String, String> headers = new HashMap<>();
      headers.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");

    } catch (Exception e) {
      // TODO: handle exception
    }
    return null;


  }
}
