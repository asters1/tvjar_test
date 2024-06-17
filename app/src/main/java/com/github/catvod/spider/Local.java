package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.catvod.utils.okhttp.OkHttpUtil;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class Local extends Spider {



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

      JSONObject sp = new JSONObject();
      sp.put("type_id","/storage/emulated/0/视频");
      sp.put("type_name", "视频");
      classes.put(sp);
      result.put("class", classes);
      return result.toString();



    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
    try {
      // System.out.println(ext);
      // File VideosDir=Environment.getExternalStorageDirectory();
      JSONObject result = new JSONObject();
      JSONArray jSONArray = new JSONArray();
      File VideosDir=new File(tid);
      if (VideosDir.exists()){
        File[] files=VideosDir.listFiles();
        if (files!=null){
          for(int i=0;i<files.length;i++){
            File f=files[i];
            System.out.println(files.length);

            // System.out.println(f);
            if (f.isDirectory()){
              if(f.getName().equals("PIC")){
                continue;
              }
              JSONObject vod = new JSONObject();
              String vod_name=f.getName().toString();
              vod.put("vod_name", vod_name);
              vod.put("vod_id", f.toString());
              String vod_pic=tid+"/"+vod_name+".jpg";
              vod.put("vod_pic", vod_pic);
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

    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String detailContent(List<String> ids) {
    try {
      JSONObject result = new JSONObject();
      JSONObject info = new JSONObject();
      JSONArray list_info = new JSONArray();

      String url = ids.get(0);
      File VideosDir=new File(url);
      ArrayList<String> arr = new ArrayList<String>();
      if (VideosDir.exists()){
        File[] files=VideosDir.listFiles();
        if (files!=null){
          for(int i=0;i<files.length;i++){
            File f=files[i];
            String vname=f.getName();
            String vurl=f.toString();
            // System.out.println(vname+"$"+vurl);
            arr.add(vname+"$"+vurl);
          }
        }

      }
      String vod_name=VideosDir.getName();

      String vod_play_from="Local";
      String vod_play_url=TextUtils.join("$$$", arr);

      info.put("vod_id", ids.get(0));
      info.put("vod_name", vod_name);

      info.put("vod_play_from", vod_play_from);
      info.put("vod_play_url", vod_play_url);

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
      JSONObject result = new JSONObject();
            result.put("parse", 0);
            result.put("header", "");
            result.put("playUrl", id);
            result.put("url", "");
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
}
