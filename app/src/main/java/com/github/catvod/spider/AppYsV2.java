package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class AppYsV2 extends Spider {

  //===========================
  public  String siteUrl = "";

  public List<String> siteFormPasre = new ArrayList<>();

  //===========================

  public void init(Context context,String ext) {
    super.init(context,ext);
    this.siteUrl=ext;
  }

  public String homeContent(boolean filter) {
    try {
      //读取分类
      String url = getCateUrl(siteUrl);
      JSONObject result_obj=new JSONObject();
      JSONArray class_arr=new JSONArray();
      JSONObject filter_obj=new JSONObject();
      JSONArray jsonArray=null;
      if (url!=""){
        JSONObject json_obj=new JSONObject( OkHttpUtil.string(url,getHeaders(url)));
        if(json_obj.has("list")&&(isJsonArray(json_obj.get("list").toString()))){
          jsonArray=json_obj.getJSONArray("list");
        }else if (json_obj.has("data")&&(!isJsonArray(json_obj.get("data").toString()))&&json_obj.getJSONObject("data").has("list")&&isJsonArray( json_obj.getJSONObject("data").get("list").toString())){

          jsonArray=json_obj.getJSONObject("data").getJSONArray("list");
        }else if (json_obj.has("data")&&isJsonArray(  json_obj.get("data").toString() )){
          jsonArray=json_obj.getJSONArray("data");
        }
      }else{
        return "";
      }

      //结果

      if (jsonArray!=null){
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject jobj=jsonArray.getJSONObject(i);
          String type_name=jobj.getString("type_name");
          if (isBan(type_name)){
            continue;
          }
          String type_id=jobj.get("type_id").toString();
          JSONObject Cls=new JSONObject();
          Cls.put("type_name", type_name);
          Cls.put("type_id",type_id);
          // System.out.println(Cls);
          JSONObject typeExtend=null;
          if (filter){
            if(!isJsonArray( jobj.get("type_extend").toString())){
              typeExtend=jobj.getJSONObject("type_extend");
            }
            String filterStr = getFilterTypes(url, typeExtend);
            String[] filters = filterStr.split("\n");

            JSONArray class_value=new JSONArray();
            for(int k=1;k<filters.length;k++){
              JSONArray class_one_value=new JSONArray();
              String l=filters[k].trim();
              if(l.equals("")){
                continue;
              }
              String[] oneLine=l.split("\\+");
              String type=oneLine[0].trim();
              String typeN=type;
              if (type.contains("筛选")){
                type = type.replaceAll("筛选","");
                if (type.equals( "class") ){typeN = "类型";}
                else if (type.equals( "area") ){typeN = "地区";}
                else if (type.equals( "lang") ){typeN = "语言";}
                else if (type.equals( "year") ){typeN = "年份";}
              }
              for (int j = 1; j < oneLine.length; j++) {
                String kv=oneLine[j].trim();
                int sp = kv.indexOf("=");

                if(sp==-1){
                  if (isBan(kv)) {continue;}
                  JSONObject jOne_v=new JSONObject();
                  jOne_v.put("n", kv);
                  jOne_v.put("v", kv);
                  class_one_value.put( jOne_v);
                }else {
                  String n = kv.substring(0, sp);
                  if (isBan(n)) {continue;}
                  JSONObject jOne_v=new JSONObject();
                  jOne_v.put("n", n.trim());
                  jOne_v.put("v",kv.substring(sp + 1).trim());
                  class_one_value.put( jOne_v);
                }
              }
              // class_value.put(class_one_value);
              // System.out.println(class_one_value);
              JSONObject class_one_obj=new JSONObject();
              class_one_obj.put("key", type);
              class_one_obj.put("name", typeN);
              class_one_obj.put("value", class_one_value);
              class_value.put(class_one_obj);
            }
            filter_obj.put(type_id, class_value);
          }
          class_arr.put(Cls);

        }
        result_obj.put("class", class_arr);
        result_obj.put("filters",filter_obj);
      }
      return result_obj.toString();




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
  public String getCateUrl(String URL){
    if(URL.contains("api.php/app")||URL.contains("xgapp")){
      return URL + "nav?token=";
    }else if (URL.contains(".vod")){
      return URL + "/types";
    }else{
      return "";
    }
  }
  protected boolean isBan(String key) {
    return "伦理".equals(key) || "情色".equals(key) || "福利".equals(key);
  }
  protected String UA(String url){
    if(url.contains(".vod")){
      return "okhttp/4.1.0";
    }
    return "";
  }
  protected HashMap<String, String> getHeaders(String url) {
    HashMap<String, String> headers = new HashMap<>();
    headers.put("User-Agent",UA(url));
    return headers;
  }
  protected String getFilterTypes(String URL,JSONObject typeExtend){
    String str="";
    if (typeExtend!=null){
      for (String key : typeExtend.keySet()) {
        if (key.equals("class")||key.equals("area")||key.equals("lang")||key.equals("year")){
          try{
            str +=  "筛选" + key + "+全部=+" + typeExtend.get(key).toString().replaceAll(",", "+")+"\n";
          } catch (Exception e) {
          }

        }
      }

      if(URL.contains(".vod")){
        str += "\n" + "排序+全部=+最新=time+最热=hits+评分=score";

      }else if (URL.contains("api.php/app") || URL.contains("xgapp")) {
        // 什么都不做，让字符串保持原样。
      }else{
        str+="分类+全部=+电影=movie+连续剧=tvplay+综艺=tvshow+动漫=comic+4K=movie_4k+体育=tiyu\n筛选class+全部=+喜剧+爱情+恐怖+动作+科幻+剧情+战争+警匪+犯罪+动画+奇幻+武侠+冒险+枪战+恐怖+悬疑+惊悚+经典+青春+文艺+微电影+古装+历史+运动+农村+惊悚+惊悚+伦理+情色+福利+三级+儿童+网络电影\n筛选area+全部=+大陆+香港+台湾+美国+英国+法国+日本+韩国+德国+泰国+印度+西班牙+加拿大+其他\n筛选year+全部=+2023+2022+2021+2020+2019+2018+2017+2016+2015+2014+2013+2012+2011+2010+2009+2008+2007+2006+2005+2004+2003+2002+2001+2000";
      }
    }
    return str;
  }
  protected boolean isJsonArray(String str_obj){
    try{
      new JSONArray(str_obj);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  //.equls=================
  void printLog(String key, String value) {
    try {

      String str = key + "=" + value;
      String str1 = "http://localhost:8080/?" + str;
      String res = OkHttpUtil.string(str1, null);
      System.out.println(res);
    } catch (Exception e) {
    }
  }
  //.equls=================
}
