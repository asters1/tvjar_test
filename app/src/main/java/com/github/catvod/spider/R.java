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
  //https://theporndude.com/zh

  private static final String siteUrl = "https://91gaoqingheiliao.com";
  //ext为外部给的字符串
  public String ext = "";

  public void init(Context context,String ext) {
    super.init(context,ext);
    this.ext=ext;
  }

  public String homeContent(boolean filter) {
    try {
      JSONObject result=new JSONObject();
      JSONArray classes=new JSONArray();
      JSONObject filters=new JSONObject();

      String res= OkHttpUtil.string(siteUrl, getHeaders());
      Elements list_el = Jsoup.parse(res).select("[id=youmu]").select("[class=area]");
      for (int i = 1; i < list_el.size(); i++) {
        String type_name=list_el.get(i).select("dt").text();
        String type_id=Integer.toString(i);
        JSONObject t_obj=new JSONObject();
        t_obj.put("type_id", type_id);
        t_obj.put("type_name", type_name);
        classes.put(t_obj);

        JSONArray f_array=new JSONArray();
        JSONObject f_obj=new JSONObject();


        Elements list_f=list_el.get(i).select("dd");
        for(int j=0;j<list_f.size();j++){
          JSONObject f_array_obj=new JSONObject();
          String n= list_f.get(j).select("a").text();
          String v1= list_f.get(j).select("a").attr("href");
          String v=v1.substring(0,v1.length()-1);
          // System.out.println(v);
          f_array_obj.put("n", n);
          f_array_obj.put("v", v);
          f_array.put(f_array_obj);

        }
        f_obj.put("key", type_name);
        f_obj.put("name", "分类");
        f_obj.put("value", f_array);
        // System.out.println(f_obj);
        filters.put(Integer.toString(i), new JSONArray().put(f_obj));


      }
      // System.out.println(filters);
      result.put("class", classes);
      result.put("filters", filters);

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

        switch (tid) {
          case "1":
            //天美
            cateUrl=siteUrl+"/t/111-"+pg+"/";
            break;
          case "2":
            //蜜桃
            cateUrl=siteUrl+"/t/113-"+pg+"/";
            break;
          case "3":
            //日本无码
            cateUrl=siteUrl+"/t/5-"+pg+"/";
            break;
          case "4":
            cateUrl=siteUrl+"/t/103-"+pg+"/";
            break;
          case "5":
            cateUrl=siteUrl+"/t/225-"+pg+"/";
            break;

          default:
            break;
        }

      }else {
        String tyid="";
        for (String key : extend.keySet()) {
          tyid=extend.get(key);
        }
        cateUrl=siteUrl+tyid+"-"+pg+"/";
      }

      String res = OkHttpUtil.string(cateUrl, getHeaders());
      // System.out.println(res);
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
      JSONObject result = new JSONObject();
      JSONArray jSONArray = new JSONArray();
      String searchUrl= "https://91gaoqingheiliao.com/s/?wd="+key;
      String res=OkHttpUtil.string(searchUrl, getHeaders());
      Elements list_el = Jsoup.parse(res).select("[class=row row-space8 row-m-space8]").get(0).select("[class=col-25 col-m-12 mb20]");

      System.out.println(list_el.size());
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
        // System.out.println(vod);
        jSONArray.put(vod);

      }
      result.put("list", jSONArray);
      // System.out.println("==");
      // System.out.println(result.toString());
      // System.out.println("==");
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
