package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;


import java.util.ArrayList;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class IkanBot extends Spider {

  private static final String siteUrl = "https://v.aikanbot.com";
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

      dianying.put("type_name", "电影");
      dianying.put("type_id", "movie");

      dianshiju.put("type_name", "电视剧");
      dianshiju.put("type_id", "tv");

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
      // System.out.println("====");
      // System.out.println(getToken(""));
      // System.out.println("====");
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
        vod.put("vod_pic", vod_pic);
        jSONArray.put(vod);
      }
      result.put("page", page);
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

      JSONObject result = new JSONObject();
      JSONObject info = new JSONObject();
      JSONArray list_info = new JSONArray();
      String u=siteUrl+ids.get(0);
      String res = OkHttpUtil.string(u, getHeaders());
      // String vod_id = Jsoup.parse(res).select("[id=current_id]").attr("value");
      String vod_name = Jsoup.parse(res).select("h2").text();
      String vod_pic = Jsoup.parse(res).select("[class=item-root]").select("img").attr("data-src");
      String vod_actor=Jsoup.parse(res).select("[class=meta]").get(3).text();
      String vod_area=Jsoup.parse(res).select("[class=meta]").get(2).text();
      String vod_year=Jsoup.parse(res).select("[class=meta]").get(1).text();
      String vod_content=Jsoup.parse(res).select("[class=line-tips]").text();
      info.put("vod_id", ids.get(0));
      info.put("vod_name", vod_name);
      info.put("vod_pic", vod_pic);
      info.put("type_name", "");
      info.put("vod_year", vod_year);
      info.put("vod_area", vod_area);
      info.put("vod_remarks", "");
      info.put("vod_actor", vod_actor);
      info.put("vod_director", "");
      info.put("vod_content", vod_content);
      String v_tks = getToken(res);
      int vid_index=u.lastIndexOf("/")+1;
      String uu=siteUrl+"/api/getResN?videoId=" + u.substring(vid_index)+"&mtype=2&token="+v_tks;
      // System.out.println(uu);
      String res_m3u8 = OkHttpUtil.string(uu, getHeaders());
      // System.out.println(res_m3u8);
      JSONObject json_res_m3u8=new JSONObject(res_m3u8);
      HashMap<String, String> m = new HashMap<>();
      m.put("bfzym3u8", "暴风");
      m.put("1080zyk", "优质");
      m.put("kuaikan", "快看");
      m.put("lzm3u8", "量子");
      m.put("ffm3u8", "非凡");
      m.put("haiwaikan", "海外看");
      m.put("gsm3u8", "光速");
      m.put("zuidam3u8", "最大");
      m.put("bjm3u8", "八戒");
      m.put("snm3u8", "索尼");
      m.put("wolong", "卧龙");
      m.put("xlm3u8", "新浪");
      m.put("yhm3u8", "樱花");
      m.put("tkm3u8", "天空");
      m.put("jsm3u8", "极速");
      m.put("wjm3u8", "无尽");
      m.put("sdm3u8", "闪电");
      m.put("kcm3u8", "快车");
      m.put("jinyingm3u8", "金鹰");
      m.put("fsm3u8", "飞速");
      m.put("tpm3u8", "淘片");
      m.put("lem3u8", "鱼乐");
      m.put("dbm3u8", "百度");
      m.put("tomm3u8", "番茄");
      m.put("ukm3u8", "U酷");
      m.put("ikm3u8", "爱坤");
      m.put("hnzym3u8", "红牛资源");
      m.put("hnm3u8", "红牛");
      m.put("68zy_m3u8", "68");
      m.put("kdm3u8", "酷点");
      m.put("bdxm3u8", "北斗星");
      m.put("qhm3u8", "奇虎");
      m.put("hhm3u8", "豪华");
      JSONArray res_list=json_res_m3u8.getJSONObject("data").getJSONArray("list");
      // System.out.println(res_list);
      ArrayList<String> play_from_array = new ArrayList<String>();
      ArrayList<String> play_url_array = new ArrayList<String>();
      for(int i=0;i<res_list.length();i++){
        JSONArray json_resData=new JSONArray( res_list.getJSONObject(i).getString("resData"));
        JSONObject f_and_u=json_resData.getJSONObject(0);

        try {
          String flag=f_and_u.getString("flag");
          String res_url=f_and_u.getString("url");
          // System.out.println(flag);
          if(m.containsKey(flag)){
            // System.out.println(m.get(flag));
            play_from_array.add(m.get(flag));


          }else{
            play_from_array.add(flag);

          }

          play_url_array.add(res_url.replaceAll("##", "#"));


        } catch (Exception e) {
          System.out.println("运行时解析Url出错");
        }
      }
      String vod_play_from=TextUtils.join("$$$", play_from_array);
      String vod_play_url=TextUtils.join("$$$", play_url_array);

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
        vod.put("vod_pic", vod_pic);
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
      result.put("playUrl", id);
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
