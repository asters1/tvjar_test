package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

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
      String vod_id = Jsoup.parse(res).select("[id=current_id]").attr("value");
      String vod_name = Jsoup.parse(res).select("h2").text();
      String vod_pic = Jsoup.parse(res).select("[class=item-root]").select("img").attr("data-src");
      String vod_actor=Jsoup.parse(res).select("[class=meta]").get(4).text();
      String vod_area=Jsoup.parse(res).select("[class=meta]").get(3).text();
      String vod_year=Jsoup.parse(res).select("[class=meta]").get(2).text();
      String vod_content=Jsoup.parse(res).select("[class=line-tips]").text();
      info.put("vod_id", ids.get(0));
      info.put("vod_name", vod_name);
      info.put("vod_pic", vod_pic);
      // info.put("type_name", type_name);
      info.put("vod_year", vod_year);
      info.put("vod_area", vod_area);
      // info.put("vod_remarks", vod_remarks);
      info.put("vod_actor", vod_actor);
      // info.put("vod_director", vod_director);
      info.put("vod_content", vod_content);


      String v_tks = getToken(res);



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

    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
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
