package com.github.catvod.spider;


import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import java.net.URLEncoder;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class CaiHong extends Spider {

  private static final String siteUrl = "http://ys.qtw.asia/lvdou_api.php/v1.vod";
  public HashMap<String,ArrayList<String>> parseUrlMap =new HashMap<>();
  //ext为外部给的字符串
  public String ext = "";

  public void init(Context context,String ext) {
    super.init(context,ext);
    this.ext=ext;
  }

  public String homeContent(boolean filter) {
    try {
      // System.out.println(ext);
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
              // System.out.println(typeExtend);
            }
            String filterStr = getFilterTypes(url, typeExtend);
            String[] filters = filterStr.split("\n");

            JSONArray class_value=new JSONArray();
            for(int k=0;k<filters.length;k++){
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
      String apiUrl=siteUrl;
      String url=getCateFilterUrlPrefix(apiUrl) + tid + getCateFilterUrlSuffix(apiUrl);

      url = url.replace("#PN#", pg);
      if (extend.containsKey("class")){
        url = url.replace("筛选class", extend.get("class"));
      }else{
        url = url.replace("筛选class", "");
      }
      if (extend.containsKey("area")){
        url = url.replace("筛选area", extend.get("area"));
      }else{
        url = url.replace("筛选area", "");
      }
      if (extend.containsKey("lang")){
        url = url.replace("筛选lang", extend.get("lang"));
      }else{
        url = url.replace("筛选lang", "");
      }
      if (extend.containsKey("year")){
        url = url.replace("筛选year", extend.get("year"));
      }else{
        url = url.replace("筛选year", "");
      }
      if (extend.containsKey("排序")){
        url = url.replace("排序", extend.get("排序"));
      }else{
        url = url.replace("排序", "");
      }

      JSONObject json_obj=new JSONObject( OkHttpUtil.string(url,getHeaders(url)));
      int totalPg=Integer.MAX_VALUE;
      if(json_obj.has("totalpage")&&isJsonInt(json_obj, "totalpage")){
        totalPg = json_obj.getInt("totalpage");

      }else if(json_obj.has("pagecount")&&isJsonInt(json_obj, "pagecount")){
        totalPg = json_obj.getInt("pagecount");
      }else if (json_obj.has("data")&&!isJsonArray(json_obj.get("data").toString())&&json_obj.getJSONObject("data").has("total")&&isJsonInt(json_obj.getJSONObject("data"), "total")&&json_obj.getJSONObject("data").has("limit")&&isJsonInt(json_obj.getJSONObject("data"), "limit")){
        int limit=json_obj.getJSONObject("data").getInt("limit");
        int total=json_obj.getJSONObject("data").getInt("total");
        if(total%limit==0){
          totalPg=total/limit;
        }else{
          totalPg=total/limit+1;
        }
      }
      JSONArray jsonArray=null;
      if(json_obj.has("list")){
        jsonArray=json_obj.getJSONArray("list");
      }else if (json_obj.has("data")&&!isJsonArray(json_obj.get("data").toString())&&json_obj.getJSONObject("data").has("list")){
        jsonArray=json_obj.getJSONObject("data").getJSONArray("list");

      }else if (json_obj.has("data")&& isJsonArray(json_obj.get("data").toString())){
        jsonArray=json_obj.getJSONArray("data");

      }

      JSONArray videos=new JSONArray();
      if (jsonArray!=null){
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject vObj=jsonArray.getJSONObject(i);
          JSONObject v=new JSONObject() ;
          if (vObj.has("vod_id")){
            v.put("vod_id", vObj.get("vod_id").toString());
          }else if(vObj.has("nextlink")){
            v.put("vod_id", vObj.get("nextlink").toString());
          }
          if (vObj.has("vod_name")){
            v.put("vod_name", vObj.get("vod_name").toString());
          }else if(vObj.has("title")){
            v.put("vod_name", vObj.get("title").toString());
          }
          if (vObj.has("vod_pic")){
            v.put("vod_pic", vObj.get("vod_pic").toString());
          }else if(vObj.has("pic")){
            v.put("vod_pic", vObj.get("piv").toString());
          }
          if (vObj.has("vod_remarks")){
            v.put("vod_remarks", vObj.get("vod_remarks").toString());
          }else if(vObj.has("state")){
            v.put("vod_remarks", vObj.get("state").toString());
          }
          videos.put(v);
        }
      }
      JSONObject result=new JSONObject();
      result.put("page", Integer.parseInt(pg));
      result.put("pagecount",totalPg);
      result.put("limit", 90);
      result.put("total", Integer.MAX_VALUE);
      result.put("list", videos);
      // System.out.println(jsonArray);
      return result.toString();

    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String detailContent(List<String> ids) {
    try {
      String apiUrl=siteUrl;
      String url=getPlayUrlPrefix(apiUrl) + ids.get(0);
      // String url=getPlayUrlPrefix(apiUrl) + "157198";

      JSONObject json_obj=new JSONObject( OkHttpUtil.string(url,getHeaders(url)));
      JSONObject pl_obj= genPlayList(apiUrl, json_obj,ids.get(0));
      if (pl_obj!=null){
        JSONObject result=new JSONObject();
        JSONArray lis=new JSONArray();
        lis.put(pl_obj);
        result.put("list", lis);
        return result.toString();

      }
    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String searchContent(String key, boolean quick) {
    try {

      String apiUrl=siteUrl;
      String url= getSearchUrl(apiUrl, encodeURIComponent(key));
      JSONObject json_obj=new JSONObject( OkHttpUtil.string(url,getHeaders(url)));
      JSONArray jsonArray=null;
      JSONArray videos=new JSONArray();

      if ( json_obj.has("list")&&isJsonArray( json_obj.get("list").toString())){
        jsonArray=json_obj.getJSONArray("list");
      }else if(json_obj.has("data") &&(!isJsonArray(json_obj.get("data").toString()))&&json_obj.getJSONObject("data").has("list")&&isJsonArray(json_obj.getJSONObject("data").get("list").toString())){

        jsonArray=json_obj.getJSONObject("data").getJSONArray("list");
      }else if(json_obj.has("data")&&isJsonArray(json_obj.get("data").toString())){

        jsonArray=json_obj.getJSONArray("data");
      }
      if(jsonArray!=null){
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject v=new JSONObject();

          if (jsonArray.getJSONObject(i).has("vod_id")){
            v.put("vod_id",jsonArray.getJSONObject(i).get("vod_id").toString() );
            v.put("vod_name",jsonArray.getJSONObject(i).get("vod_name").toString() );
            v.put("vod_remarks",jsonArray.getJSONObject(i).get("vod_remarks").toString() );
            v.put("vod_pic",jsonArray.getJSONObject(i).get("vod_pic").toString() );
            videos.put(v);
          }
        }
      }

      JSONObject result =new JSONObject();
      result.put("list", videos);
      return result.toString();

    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String playerContent(String flag, String id, List<String> vipFlags) {
    try {
      JSONObject result =new JSONObject();
      id=id.trim();
      String parse =GetparseUrlMapValue(parseUrlMap);
      if(id.startsWith("http")&&isVideo(id) ) {
        result.put("header", getHeaders(id));
        result.put("parse", 0);
        result.put("url", id);
        return result.toString();
      }else if(!parse.equals("")){
        String url=parse+id;
        String purl=getPlayUrl(url);
        result.put("header", getHeaders(purl));
        result.put("parse", 0);
        result.put("url", purl);
        return result.toString();
      }else{
        result.put("header", getHeaders(id));
        result.put("parse", 2);
        result.put("url", id);
        return result.toString();

      }

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
  public String getCateUrl(String URL){
    if(URL.contains("api.php/app")||URL.contains("xgapp")){
      return URL + "nav?token=";
    }else if (URL.contains(".vod")){
      return URL + "/types";
    }else{
      return "";
    }
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
  protected boolean isJsonInt(JSONObject json,String key){
    try{
      json.getInt(key);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  protected boolean isJsonArray(String str_obj){
    try{
      new JSONArray(str_obj);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  protected boolean isBan(String key) {
    return "伦理".equals(key) || "情色".equals(key) || "福利".equals(key);
  }
  protected String getFilterTypes(String URL,JSONObject typeExtend){
    Iterator<String> it=typeExtend.keys();
    String str="";
    if (typeExtend!=null){
      while(it.hasNext()){
        String key = (String) it.next().toString();
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
  protected String getCateFilterUrlSuffix(String URL){
    if (URL.contains("api.php/app") || URL.contains("xgapp")) {
      return "&class=筛选class&area=筛选area&lang=筛选lang&year=筛选year&limit=18&pg=#PN#";
    } else if (URL.contains(".vod")) {
      return "&class=筛选class&area=筛选area&lang=筛选lang&year=筛选year&by=排序&limit=18&page=#PN#";
    } else {
      return "&page=#PN#&area=筛选area&type=筛选class&start=筛选year";
    }
  }
  protected String getCateFilterUrlPrefix(String URL){
    if (URL.contains("api.php/app")||URL.contains("xgapp")){
      return URL + "video?tid=";
    }else if (URL.contains(".vod")){
      return URL + "?type=";
    }else{
      return URL + "?ac=list&class=";

    }
  }

  protected String getPlayUrlPrefix(String URL){
    if (URL.contains("api.php/app") || URL.contains("xgapp")) {
      return URL + "video_detail?id=";
    } else if (URL.contains(".vod")) {
      return URL + "/detail?vod_id=";
    } else {
      return "";
    }
  }
  protected JSONObject genPlayList(String URL,JSONObject object,String vid) {
try{
    JSONObject vod =new JSONObject();
    ArrayList<String> playUrls= new ArrayList<String>();
    ArrayList<String> playFlags= new ArrayList<String>();
    JSONObject data=object.getJSONObject("data");
    if (data.has("vod_id")){
      vod.put("vod_id", data.get("vod_id").toString());
      vod.put("vod_id", data.get("vod_id").toString());

    }else{
      vod.put("vod_id", "");

    }
    if (data.has("vod_name")){
      vod.put("vod_name", data.get("vod_name").toString());
      vod.put("vod_name", data.get("vod_name").toString());

    }else{
      vod.put("vod_name", "");

    }
    if (data.has("vod_pic")){
      vod.put("vod_pic", data.get("vod_pic").toString());
      vod.put("vod_pic", data.get("vod_pic").toString());

    }else{
      vod.put("vod_pic", "");

    }
    if (data.has("vod_class")){
      vod.put("type_name", data.get("vod_class").toString());
      vod.put("vod_class", data.get("vod_class").toString());

    }else{
      vod.put("vod_class", "");

    }
    if (data.has("vod_year")){
      vod.put("vod_year", data.get("vod_year").toString());
      vod.put("vod_year", data.get("vod_year").toString());

    }else{
      vod.put("vod_year", "");

    }
    if (data.has("vod_area")){
      vod.put("vod_area", data.get("vod_area").toString());
      vod.put("vod_area", data.get("vod_area").toString());

    }else{
      vod.put("vod_area", "");

    }
    if (data.has("vod_remarks")){
      vod.put("vod_remarks", data.get("vod_remarks").toString());
      vod.put("vod_remarks", data.get("vod_remarks").toString());

    }else{
      vod.put("vod_remarks", "");

    }
    if (data.has("vod_actor")){
      vod.put("vod_actor", data.get("vod_actor").toString());
      vod.put("vod_actor", data.get("vod_actor").toString());

    }else{
      vod.put("vod_actor", "");

    }
    if (data.has("vod_director")){
      vod.put("vod_director", data.get("vod_director").toString());
      vod.put("vod_director", data.get("vod_director").toString());

    }else{
      vod.put("vod_director", "");

    }
    if (data.has("vod_content")){
      vod.put("vod_content", data.get("vod_content").toString());
      vod.put("vod_content", data.get("vod_content").toString());

    }else{
      vod.put("vod_content", "");

    }
    if (data.has("vod_play_from")){
      vod.put("vod_play_from",data.get("vod_play_from").toString());
    }else{
      vod.put("vod_play_from", "");
    }
    if (data.has("vod_play_url")){
      vod.put("vod_play_url",data.get("vod_play_url").toString());

    }else{
      vod.put("vod_play_url", "");

    }
    // System.out.println(vod);
    // JSONArray vodUrlWithPlayer=data.getJSONArray("vod_play_url");
    // System.out.println(vodUrlWithPlayer);

    return vod;
    } catch (Exception e) {
      SpiderDebug.log(e);
    }
return null;
  }
  protected boolean isVideo(String URL){
    String[] video_type_list={".m3u8",".mp4",".mkv"};
    for (int i=0;i<video_type_list.length;i++){
      if(URL.contains(video_type_list[i])){
        return true;
      }
    }
    return false;
  }
  public String GetparseUrlMapValue(HashMap<String,ArrayList<String>> m){
    for(String key:m.keySet()){
      for (int i = 0; i < m.get(key).size(); i++) {
        if (!m.get(key).get(i).equals("")){
          return m.get(key).get(i);
        }
      }
    }


    return "";
  }
  public String getPlayUrl(String URL){
    try{
      System.out.println(URL);
      JSONObject json_obj=new JSONObject( OkHttpUtil.string(URL,getHeaders(URL)));
      return json_obj.get("url").toString();
    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";


  }
  public String encodeURIComponent(String key){
    try{
      String encodedKey = URLEncoder.encode(key);
      return encodedKey;
    } catch (Exception e) {
    }
    return "";

  }
  public String getSearchUrl(String URL,String KEY){
    if (URL.contains(".vod")) {
      return URL + "?wd=" + KEY + "&page=";
    } else if (URL.contains("api.php/app") || URL.contains("xgapp")) {
      return URL + "search?text=" + KEY + "&pg=";
    }
    return "";
  }
}
