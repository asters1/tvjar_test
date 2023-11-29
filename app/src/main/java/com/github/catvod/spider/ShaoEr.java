package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.security.MessageDigest;


import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.os.Build;

import java.util.HashMap;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class ShaoEr extends Spider {

  private static final String appVersionName = "1.0.9";
  private static final String appVersionCode = "9";
  private static final String deviceModel = Build.MODEL;
  private static final String deviceVersion=Build.VERSION.RELEASE;
  private static final String deviceBrand=Build.BRAND;
  //ext为外部给的字符串
  public String ext = "";

  public void init(Context context,String ext) {
    super.init(context,ext);
    this.ext=ext;
  }

  public String homeContent(boolean filter) {
    try {
      // System.out.println(ext);

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
      JSONObject result = new JSONObject();
      JSONArray list =new JSONArray();
      String StrTime=String.valueOf(System.currentTimeMillis());
      StringBuilder URLSB = new StringBuilder();
      URLSB.append("http://ys.changmengyun.com/api.php/provide/vod_detail?appVersionName="+appVersionName);
      URLSB.append("&imei=&time="+StrTime);
      URLSB.append("&id="+ids.get(0));
      URLSB.append("&deviceScreen=2340*1080&appVersionCode="+appVersionCode);
      URLSB.append("&deviceModel="+deviceModel);
      URLSB.append("&app=ylys&deviceBrand="+deviceBrand);
      URLSB.append("&devices=android&deviceVersion="+deviceVersion);
      String res=OkHttpUtil.string(URLSB.toString(), GetHeaders(StrTime));

      JSONObject res_obj=new JSONObject(res);
      JSONObject data_obj=res_obj.getJSONObject("data");
      JSONObject vod_obj = new JSONObject();
      vod_obj.put("vod_id", ids.get(0));
      vod_obj.put("vod_name", data_obj.getString("name"));
      vod_obj.put("vod_pic", data_obj.getString("img"));
      vod_obj.put("type_name", data_obj.getString("type"));
      vod_obj.put("vod_year", data_obj.getString("year"));
      vod_obj.put("vod_remarks", data_obj.getString("msg"));
      vod_obj.put("vod_content", data_obj.getString("info"));
      // vod_obj.put("", data_obj.getString(""));
JSONArray player_info_array = data_obj.getJSONArray("player_info");
 ArrayList<String> play_from_array = new ArrayList<String>();
 ArrayList<String> play_url_list = new ArrayList<String>();

for (int i=0;i<player_info_array.length();i++){
  JSONObject v_obj=player_info_array.getJSONObject(i);
  play_from_array.add(v_obj.getString("show"));

  JSONArray u_array=v_obj.getJSONArray("video_info");

 ArrayList<String> url_list = new ArrayList<String>();
  for(int j=0;j<u_array.length();j++){
    url_list.add( u_array.getJSONObject(j).getString("name")+"$"+u_array.getJSONObject(j).getJSONArray("url").getString(0));
  }

  play_url_list.add(TextUtils.join("#", url_list));
  

}
       vod_obj.put("vod_play_from", TextUtils.join("$$$",  play_from_array));
       vod_obj.put("vod_play_url", TextUtils.join("$$$",  play_url_list));
       
       list.put(vod_obj);
       result.put("list", list);

            return result.toString();



    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String searchContent(String key, boolean quick) {
    try {
      JSONArray list = new JSONArray();
      JSONObject result = new JSONObject();
      String StrTime=String.valueOf(System.currentTimeMillis());
      StringBuilder URLSB = new StringBuilder();
      URLSB.append("http://ys.changmengyun.com/api.php/provide/search_result?video_name=");
      URLSB.append(URLEncoder.encode(key));
      URLSB.append("&appVersionName="+appVersionName);
      URLSB.append("&imei=&time="+StrTime);
      URLSB.append("&deviceScreen=2340*1080&appVersionCode="+appVersionCode);
      URLSB.append("&deviceModel="+deviceModel);
      URLSB.append("&app=ylys&deviceBrand="+deviceBrand);
      URLSB.append("&devices=android&deviceVersion="+deviceVersion);
      // System.out.println(URLSB.toString());
      String res=OkHttpUtil.string(URLSB.toString(), GetHeaders(StrTime));
      JSONObject jsonres = new JSONObject(res);
      JSONArray arrayres = jsonres.getJSONArray("data").getJSONObject(0).getJSONArray("data");
      for (int i = 0; i < arrayres.length(); i++) {
        JSONObject jSONObject = arrayres.getJSONObject(i);
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("vod_id", jSONObject.get("id").toString());
        jSONObject2.put("vod_name",jSONObject.getString("video_name"));
        jSONObject2.put("vod_pic", jSONObject.getString("img"));
        jSONObject2.put("vod_remarks", jSONObject.getString("category"));
        list.put(jSONObject2);
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
      String StrTime=String.valueOf(System.currentTimeMillis());
      String res= OkHttpUtil.string(id, GetHeaders(StrTime));
      JSONObject r_obj=new JSONObject(res);
      JSONObject result=new JSONObject();
      result.put("parse", 0);
      result.put("header", r_obj.getJSONObject("data").get("header").toString());
      result.put("url", r_obj.getJSONObject("data").getString("url"));
      return result.toString();
      

    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }
  HashMap<String,String>  GetHeaders(String time){
    try {
      HashMap<String, String> headers = new HashMap<>();
      headers.put("user-agent", "okhttp/3.12.0");
      headers.put("version_name",appVersionName);
      headers.put("version_code",appVersionCode);
      headers.put("sign",getMd5("#uBFszdEM0oL0JRn@"+time));
      headers.put("timeMillis", time);
      return headers;



    } catch (Exception e) {
      return null;
    }

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
  public String getMd5(String str) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(str.getBytes());
      byte b[] = md.digest();

      int i;

      StringBuffer buf = new StringBuffer("");
      for (int offset = 0; offset < b.length; offset++) {
        i = b[offset];
        if (i < 0)
          i += 256;
        if (i < 16)
          buf.append("0");
        buf.append(Integer.toHexString(i));
      }
      // System.out.println(buf.toString());
      //
      // 32位加密
      return buf.toString();
      // 16位的加密
      // return buf.toString().substring(8, 24);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
