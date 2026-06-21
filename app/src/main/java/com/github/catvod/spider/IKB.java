package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Collections;


import java.io.File;

import java.util.ArrayList;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class IKB extends Spider {

  private static final String siteUrl = "https://v.aikanbot.com";

  public String BasePath = "/storage/emulated/0/视频";

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
      JSONObject local_video = new JSONObject();


      local_video.put("type_name","本地视频");
      local_video.put("type_id",BasePath);

      dianying.put("type_name", "电影");
      dianying.put("type_id", "movie");

      dianshiju.put("type_name", "电视剧");
      dianshiju.put("type_id", "tv");

      classes.put(local_video);
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
      if(tid.equals(BasePath)){
        JSONObject result = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        File VideosDir=new File(tid);
        if (VideosDir.exists()){
          File[] files=VideosDir.listFiles();
          if (files!=null){
            for(int i=0;i<files.length;i++){
              File f=files[i];
              // System.out.println(files.length);

              // System.out.println(f);
              if (f.isDirectory()){
                if(f.getName().equals("PIC")){
                  continue;
                }
                JSONObject vod = new JSONObject();
                String vod_name=f.getName().toString();
                vod.put("vod_name", vod_name+"vs");
                vod.put("vod_id", f.toString());
          vod.put("ext", "quickSearch="+vod_name);
                String vod_pic=tid+"/PIC/"+vod_name+".jpg";
                vod.put("vod_pic", vod_pic);
                // System.out.println(vod_pic);
                jSONArray.put(vod);
              }

            }
          }

        }
        result.put("page", "1");
        result.put("pagecount", "1");
        result.put("limit", Integer.MAX_VALUE);
        result.put("total", Integer.MAX_VALUE);
        result.put("list", jSONArray);
        return result.toString();



      }else{

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
          vod.put("vod_pic", vod_pic+"@Referer=https://v.ikanbot.com/@User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
          vod.put("quickSearchUrl", vod_name);
          jSONArray.put(vod);
        }
        result.put("page", page);
        result.put("pagecount", Integer.MAX_VALUE);
        result.put("limit", 40);
        result.put("total", Integer.MAX_VALUE);
        result.put("list", jSONArray);
        return result.toString();



      }

    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String detailContent(List<String> ids) {
    System.out.println(ids.get(0));
    com.github.catvod.crawler.SpiderEnv.get().search("斗罗大陆");
    return "";
  }

  public String searchContent(String key, boolean quick) {
    try {
      JSONObject result=new JSONObject();
      JSONArray list=new JSONArray();
      String u=siteUrl+"/search?q="+key;
      String res = OkHttpUtil.string(u, getHeaders());
      // System.out.println(res);
      Elements  list_el = Jsoup.parse(res).select("[class=ol-xs-12 col-md-8]").select("[class=media]");
      // System.out.println("===");
      // System.out.println(list_el.toString());
      // System.out.println("===");
      for (int i = 0; i < list_el.size(); i++) {
        JSONObject vod = new JSONObject();
        String vod_id=list_el.get(i).select("[class=title-text]").attr("href");
        String vod_name=list_el.get(i).select("[class=title-text]").text();
        String vod_remarks=list_el.get(i).select("[class=label]").text();
        String vod_pic=list_el.get(i).select("img").attr("data-src");

        vod.put("vod_id", vod_id);
        vod.put("vod_name", vod_name);
        // vod.put("vod_pic", vod_pic);
        vod.put("vod_pic", vod_pic+"@Referer=https://v.ikanbot.com/@User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
        vod.put("vod_remarks", vod_remarks);
        list.put(vod);

      }
      result.put("list", list);
      return result.toString();




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
  protected HashMap<String, String> getM3u8Headers(String Url) {
    HashMap<String, String> headers = new HashMap<>();
    headers.put("User-Agent",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
    headers.put("Referer",Url);
    return headers;
  }
  protected HashMap<String, String> getHeaders() {
    HashMap<String, String> headers = new HashMap<>();
    headers.put("User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36");
    return headers;
  }
  String getToken(String html){
    String current_id = Jsoup.parse(html).select("[id=current_id]").attr("value");
    String e_token = Jsoup.parse(html).select("[id=e_token]").attr("value");
    // current_id="873527";
    // e_token="v0fd7e5f5cmr16b72f46nly1288677cdd55b4c077";
    // System.out.println(current_id);
    // System.out.println(e_token);
    if(current_id.equals("")||e_token.equals("")){
      return "";
    }

    int idLength = current_id.length();
    String subId = current_id.substring(idLength - 4, idLength);
    ArrayList<String> keys= new ArrayList<String>();
    for(int i=0;i<subId.length();i++){
      keys.add("");
    }

    for (int i = 0; i < subId.length(); i++) {

      int curInt = Integer.parseInt(subId.charAt(i)+"");
      // System.out.println(curInt);
      int splitPos = (curInt % 3) + 1;
      keys.set(i, e_token.substring(splitPos, splitPos + 8));
      // System.out.println(keys.toString());
      e_token = e_token.substring(splitPos + 8, e_token.length());

    }
    return TextUtils.join("", keys);
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
