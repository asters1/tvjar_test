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
import android.net.Uri;

import java.util.HashMap;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import com.github.catvod.utils.okhttp.OkHttpUtil;
import com.github.catvod.utils.okhttp.OKCallBack;
import okhttp3.Call;
import okhttp3.Response;

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

      JSONObject result = new JSONObject();
      JSONArray classes = new JSONArray();

      JSONObject TX = new JSONObject();


      JSONObject IQY = new JSONObject();
      JSONObject IQYfilter = new JSONObject();

      JSONObject MG = new JSONObject();
      JSONObject MGfilter = new JSONObject();


      TX.put("type_id", "TX");
      TX.put("type_name", "TX");




      IQY.put("type_id", "IQY");
      IQY.put("type_name", "IQY");

      MG.put("type_id", "MG");
      MG.put("type_name", "MG");



      classes.put(TX);
      classes.put(IQY);
      classes.put(MG);
      result.put("class", classes);
      if (filter) {
        JSONObject filters=new JSONObject();
        filters.put("TX", GetTXFilters());
        filters.put("IQY", GetIQYFilters());
        filters.put("MG", GetMGFilters());
        result.put("filters", filters);

      }
      return result.toString();


    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
  // public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
    try {
      if (tid.equals("TX")){
      //筛选
      String SX="";
      JSONObject result=new JSONObject();
      for (String key : extend.keySet()) {
        SX=SX+"&"+key+"="+extend.get(key);
      }
      // System.out.println(SX);


        String vod_remarks="";
        String url="https://v.qq.com/x/bu/pagesheet/list?_all=1&append=1&channel=child&listpage=1&offset="+((Integer.parseInt(pg) - 1) * 21)+"&pagesize=21&sort=75"+SX;
        String content=OkHttpUtil.string(url, GetNormalHeaders());
        Elements listItems = Jsoup.parse(content).select(".list_item");
        JSONArray jSONArray = new JSONArray();


        for (int i = 0; i < listItems.size(); i++) {
          Element item = listItems.get(i);
          String v_name = item.select("a").attr("title");
          String pic= q(url, item.select("img").attr("src"));
          if (item.select(".figure_caption") == null) {
            vod_remarks = "";
          } else {
            vod_remarks = item.select(".figure_caption").text();
          }
          String vid = item.select("a").attr("data-float");
          JSONObject vod = new JSONObject();
          vod.put("vod_id", vid);
          vod.put("vod_name", v_name);
          vod.put("vod_pic", pic);
          vod.put("vod_remarks", vod_remarks);

          jSONArray.put(vod);
        }
        result.put("page", pg);
        result.put("pagecount", Integer.MAX_VALUE);
        result.put("limit", 90);
        result.put("total", Integer.MAX_VALUE);
        result.put("list", jSONArray);


        return result.toString();
      }else if (tid.equals("IQY")){
        String SX="";
        String url = "https://pcw-api.iqiyi.com/search/video/videolists?channel_id=15&is_purchase=&mode=24&pageNum=" + pg + "&pageSize=24"+SX;
          if (extend != null) {
                Set<String> keySet = extend.keySet();
                ArrayList arrayList = new ArrayList();
                for (String key : keySet) {
                  String trim = extend.get(key).trim();
                    if (key.matches("\\d+")) {
                        arrayList.add(trim + ";must");
                    } else {
                        url = url + "&" + key + "=" + trim;
                    }
                }
                url = url + "&three_category_id=" + TextUtils.join(",", arrayList);

          }

String res=OkHttpUtil.string(url, GetNormalHeaders());
System.out.println(res);
JSONObject jSONObject = new JSONObject();
            try {
                JSONArray optJSONArray = new JSONObject(res).optJSONObject("data").optJSONArray("list");
                JSONArray jSONArray = new JSONArray();
                int i = 0;
                while (i < optJSONArray.length()) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    String optString = optJSONObject.optString("name");
                    String q = q(url, optJSONObject.optString("imageUrl"));
                    String optString2 = optJSONObject.optString("score");
                    String optString3 = optJSONObject.optString("playUrl");
                    if (!optJSONObject.optString("albumId").equals("")) {
                        if (optJSONObject.optInt("sourceId") != 0) {
                            optString3 = "/video/video/baseinfo/" + optJSONObject.optString("tvId") + "?userInfo=verify&jsonpCbName=videoInfo39";
                        } else {
                            optString3 = "/albums/album/avlistinfo?aid=" + optJSONObject.optString("albumId") + "&size=5000&page=1&url=" + optString3;
                        }
                    } else if (optJSONObject.optLong("tvId") != 0) {
                        optString3 = "/video/video/baseinfo/" + optJSONObject.optString("tvId") + "?userInfo=verify&jsonpCbName=videoInfo39";
                    }
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("vod_id", optString3);
                    jSONObject2.put("vod_name", optString);
                    jSONObject2.put("vod_pic", q);
                    jSONObject2.put("vod_remarks", optString2);
                    jSONArray.put(jSONObject2);
                    i++;
                    optJSONArray = optJSONArray;
                }
                jSONObject.put("page", pg);
                jSONObject.put("pagecount", Integer.MAX_VALUE);
                jSONObject.put("limit", 90);
                jSONObject.put("total", Integer.MAX_VALUE);
                jSONObject.put("list", jSONArray);
            } catch (Exception e) {
                SpiderDebug.log(e);
            }
            return jSONObject.toString(4);
      }else if(tid.equals("MG")){
        String str3 = "https://pianku.api.mgtv.com/rider/list/msite/v2?platform=msite&channelId=50&pn=" + pg + "&chargeInfo=&sort=c2";
        if (extend != null) {
                for (String str4 : extend.keySet()) {
                    String trim = extend.get(str4).trim();
                    if (trim.length() != 0) {
                        str3 = str3 + "&" + str4 + "=" + URLEncoder.encode(trim);
                    }
                }
            }
            String content = OkHttpUtil.string(str3, GetNormalHeaders());
            JSONObject jSONObject = new JSONObject();
            try {
                JSONArray optJSONArray = new JSONObject(content).optJSONObject("data").optJSONArray("hitDocs");
                JSONArray jSONArray = new JSONArray();
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    String optString = optJSONObject.optString("title");
                    String q = q(str3, optJSONObject.optString("img"));
                    String optString2 = optJSONObject.optString("updateInfo");
                    if (optString2.equals("")) {
                        optString2 = optJSONObject.optString("subtitle");
                    }
                    String vodId = optJSONObject.optString("playPartId");
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("vod_id", vodId);
                    jSONObject2.put("vod_name", optString);
                    jSONObject2.put("vod_pic", q);
                    jSONObject2.put("vod_remarks", optString2);
                    jSONArray.put(jSONObject2);
                }
                jSONObject.put("list", jSONArray);
                jSONObject.put("page", pg);
                jSONObject.put("pagecount", Integer.MAX_VALUE);
                jSONObject.put("limit", 90);
                jSONObject.put("total", Integer.MAX_VALUE);
                jSONObject.put("list", jSONArray);
            } catch (Exception e) {
                SpiderDebug.log(e);
            }
            return jSONObject.toString(4);


      }




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
  public JSONObject GetFilter(String key,String name,JSONArray value){
    try {
      JSONObject result= new JSONObject();
      result.put("key", key);
      result.put("name", name);
      result.put("value", value);
      return result;

    } catch (Exception e) {
    }


    return null;

  }
  public JSONArray GetMGFilters(){
try {
JSONArray result=new JSONArray("[{\"name\":\"类型\",\"value\":[{\"v\":\"a1\",\"n\":\"全部\"},{\"v\":\"60\",\"n\":\"恋爱少女\"},{\"v\":\"86\",\"n\":\"热血\"},{\"v\":\"62\",\"n\":\"搞笑\"},{\"v\":\"63\",\"n\":\"青春\"},{\"v\":\"64\",\"n\":\"魔幻仙侠\"},{\"v\":\"65\",\"n\":\"激燃运动\"},{\"v\":\"66\",\"n\":\"特摄\"},{\"v\":\"67\",\"n\":\"推理\"},{\"v\":\"68\",\"n\":\"亲子幼教\"},{\"v\":\"69\",\"n\":\"芒果出品\"},{\"v\":\"70\",\"n\":\"动漫音乐\"},{\"v\":\"71\",\"n\":\"经典\"},{\"v\":\"72\",\"n\":\"其他\"}],\"key\":\"kind\"},{\"name\":\"地区\",\"value\":[{\"v\":\"a1\",\"n\":\"全部\"},{\"v\":\"52\",\"n\":\"内地\"},{\"v\":\"53\",\"n\":\"欧美\"},{\"v\":\"54\",\"n\":\"日韩\"},{\"v\":\"55\",\"n\":\"其他\"}],\"key\":\"area\"},{\"name\":\"版本\",\"value\":[{\"v\":\"a1\",\"n\":\"全部\"},{\"v\":\"165\",\"n\":\"剧场版\"},{\"v\":\"57\",\"n\":\"TV版\"},{\"v\":\"166\",\"n\":\"OVA版\"},{\"v\":\"167\",\"n\":\"真人版\"}],\"key\":\"edition\"},{\"name\":\"排序\",\"value\":[{\"v\":\"c2\",\"n\":\"最热\"},{\"v\":\"c1\",\"n\":\"最新\"}],\"key\":\"sort\"}]");
  
return result;
} catch (Exception e) {
  // TODO: handle exception
}
return null;
  }
  public JSONArray GetIQYFilters(){
try {
JSONArray result=new JSONArray("[{\"name\":\"地区\",\"value\":[{\"v\":\"1261\",\"n\":\"欧美\"},{\"v\":\"1259\",\"n\":\"大陆\"},{\"v\":\"1260\",\"n\":\"港台\"},{\"v\":\"28933\",\"n\":\"韩国\"},{\"v\":\"1263\",\"n\":\"其它\"}],\"key\":\"18057\"},{\"name\":\"年龄段\",\"value\":[{\"v\":\"4489\",\"n\":\"0-3岁\"},{\"v\":\"28929\",\"n\":\"4-6岁\"},{\"v\":\"4493\",\"n\":\"7-10岁\"},{\"v\":\"4494\",\"n\":\"11-13岁\"}],\"key\":\"20288\"},{\"name\":\"类型\",\"value\":[{\"v\":\"28988\",\"n\":\"玩具\"},{\"v\":\"28983\",\"n\":\"儿歌\"},{\"v\":\"28984\",\"n\":\"故事\"},{\"v\":\"28985\",\"n\":\"学英语\"},{\"v\":\"28986\",\"n\":\"百科\"},{\"v\":\"28987\",\"n\":\"国学\"},{\"v\":\"28989\",\"n\":\"识字\"},{\"v\":\"28990\",\"n\":\"数学\"},{\"v\":\"28991\",\"n\":\"美术\"},{\"v\":\"28993\",\"n\":\"舞蹈\"},{\"v\":\"30918\",\"n\":\"音乐\"},{\"v\":\"30919\",\"n\":\"诗词\"},{\"v\":\"28994\",\"n\":\"其他\"}],\"key\":\"28982\"}]");
  
return result;
} catch (Exception e) {
}
return null;

  }
  public JSONArray GetTXFilters(){
    try {
      JSONArray result=new JSONArray();
      // String url="https://v.qq.com/x/bu/pagesheet/list?_all=1&append=1&channel=child&listpage=1&offset=0&pagesize=21&iarea=-1&iyear=2&gender=-1&itype=5&sort=75";
      //地区
      JSONArray iarea=new JSONArray();
      iarea.put(new JSONObject("{\"n\":\"全部\",\"v\":\"-1\"}"));
      iarea.put(new JSONObject("{\"n\":\"国内\",\"v\":\"3\"}"));
      iarea.put(new JSONObject("{\"n\":\"欧美\",\"v\":\"1\"}"));
      iarea.put(new JSONObject("{\"n\":\"日韩\",\"v\":\"2\"}"));
      //年龄
      JSONArray iyear=new JSONArray();
      iyear.put(new JSONObject("{\"n\":\"全部\",\"v\":\"-1\"}"));
      iyear.put(new JSONObject("{\"n\":\"0-3岁\",\"v\":\"1\"}"));
      iyear.put(new JSONObject("{\"n\":\"4-6岁\",\"v\":\"2\"}"));
      iyear.put(new JSONObject("{\"n\":\"7-9岁\",\"v\":\"3\"}"));
      iyear.put(new JSONObject("{\"n\":\"10岁以上\",\"v\":\"4\"}"));
      //性别
      JSONArray gender=new JSONArray();
      gender.put(new JSONObject("{\"n\":\"全部\",\"v\":\"-1\"}"));
      gender.put(new JSONObject("{\"n\":\"男孩\",\"v\":\"2\"}"));
      gender.put(new JSONObject("{\"n\":\"女孩\",\"v\":\"1\"}"));
      //类型
      JSONArray itype=new JSONArray();
      itype.put(new JSONObject("{\"n\":\"全部\",\"v\":\"-1\"}"));
      itype.put(new JSONObject("{\"n\":\"玩具\",\"v\":\"4\"}"));
      itype.put(new JSONObject("{\"n\":\"交通工具\",\"v\":\"10\"}"));
      itype.put(new JSONObject("{\"n\":\"手工·绘画\",\"v\":\"3\"}"));
      itype.put(new JSONObject("{\"n\":\"儿歌\",\"v\":\"1\"}"));
      itype.put(new JSONObject("{\"n\":\"益智早教\",\"v\":\"2\"}"));
      itype.put(new JSONObject("{\"n\":\"英语\",\"v\":\"5\"}"));
      itype.put(new JSONObject("{\"n\":\"早教\",\"v\":\"6\"}"));
      itype.put(new JSONObject("{\"n\":\"数学\",\"v\":\"7\"}"));
      itype.put(new JSONObject("{\"n\":\"国学\",\"v\":\"8\"}"));
      itype.put(new JSONObject("{\"n\":\"冒险\",\"v\":\"9\"}"));
      itype.put(new JSONObject("{\"n\":\"魔幻·科幻\",\"v\":\"11\"}"));
      itype.put(new JSONObject("{\"n\":\"动物\",\"v\":\"12\"}"));
      itype.put(new JSONObject("{\"n\":\"真人·特摄\",\"v\":\"13\"}"));
      itype.put(new JSONObject("{\"n\":\"探索\",\"v\":\"14\"}"));
      itype.put(new JSONObject("{\"n\":\"其他\",\"v\":\"15\"}"));





      result.put(GetFilter("iarea", "地区", iarea));
      result.put(GetFilter("iyear", "年龄", iyear));
      result.put(GetFilter("gender", "性别", gender));
      result.put(GetFilter("itype", "类型", itype));

      return result;
    } catch (Exception e) {
    }
    return null;


  }
  private String q(String str, String str2) {
    String str3;
    try {
      if (str2.startsWith("//")) {
        Uri parse = Uri.parse(str);
        str3 = parse.getScheme() + ":" + str2;
      } else if (str2.contains("://")) {
        return str2;
      } else {
        Uri parse2 = Uri.parse(str);
        str3 = parse2.getScheme() + "://" + parse2.getHost() + str2;
      }
      return str3;
    } catch (Exception e) {
      SpiderDebug.log(e);
      return str2;
    }
  }

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
