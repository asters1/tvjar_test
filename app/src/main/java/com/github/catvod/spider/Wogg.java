package com.github.catvod.spider;

import org.checkerframework.checker.units.qual.m;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.catvod.utils.okhttp.OkHttpUtil;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class Wogg extends Spider {

  // private static final String siteUrl = "";
  // private static final String siteUrl = "";
  //ext为外部给的字符串
  public String ali_token = "";
  public String siteUrl = "";

  public void init(Context context,String ext) {
    super.init(context,ext);
    JSONObject json_ext=new JSONObject(ext);


    this.siteUrl=json_ext.getString("wogg_url");
    this.ali_token=json_ext.getString("ali_token");
  }

  public String homeContent(boolean filter) {
    try {
      // System.out.println(ali_token);
      //
      // System.out.println(siteUrl);;
      JSONObject result = new JSONObject();
      JSONArray classes = new JSONArray();

      JSONObject dianying = new JSONObject();
      JSONObject dianshiju = new JSONObject();
      JSONObject dongman = new JSONObject();
      JSONObject zongyi = new JSONObject();
      JSONObject duanju = new JSONObject();
      JSONObject yingyue = new JSONObject();


      dianying.put("type_name", "电影");
      dianying.put("type_id", "/1");

      dianshiju.put("type_name", "电视剧");
      dianshiju.put("type_id", "/2");
      dongman.put("type_name", "动漫");
      dongman.put("type_id", "/3");

      zongyi.put("type_name", "综艺");
      zongyi.put("type_id", "/4");
      duanju.put("type_name", "短剧");
      duanju.put("type_id", "/6");
      yingyue.put("type_name", "音乐");
      yingyue.put("type_id", "/5");

      classes.put(dianying);
      classes.put(dianshiju);
      classes.put(dongman);
      classes.put(zongyi);
      classes.put(duanju);
      classes.put(yingyue);
      result.put("class", classes);


      if(filter){
        JSONObject f= new JSONObject( GetFilter());
        // System.out.println("==\n"+GetFilter()+"\n==");
        result.put("filters", f);

      }
      // System.out.println( result.toString());
      return result.toString();

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
  String GetFilter(){
    try {
      String f="";
      //类型(电影电视剧)
      String M_and_T_leixing="[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"喜剧\",\"v\":\"喜剧\"},{\"n\":\"爱情\",\"v\":\"爱情\"},{\"n\":\"恐怖\",\"v\":\"恐怖\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"科幻\",\"v\":\"科幻\"},{\"n\":\"剧情\",\"v\":\"剧情\"},{\"n\":\"战争\",\"v\":\"战争\"},{\"n\":\"警匪\",\"v\":\"警匪\"},{\"n\":\"犯罪\",\"v\":\"犯罪\"},{\"n\":\"古装\",\"v\":\"古装\"},{\"n\":\"奇幻\",\"v\":\"奇幻\"},{\"n\":\"武侠\",\"v\":\"武侠\"},{\"n\":\"冒险\",\"v\":\"冒险\"},{\"n\":\"枪战\",\"v\":\"枪战\"},{\"n\":\"恐怖\",\"v\":\"恐怖\"},{\"n\":\"悬疑\",\"v\":\"悬疑\"},{\"n\":\"惊悚\",\"v\":\"惊悚\"},{\"n\":\"经典\",\"v\":\"经典\"},{\"n\":\"青春\",\"v\":\"青春\"},{\"n\":\"文艺\",\"v\":\"文艺\"},{\"n\":\"微电影\",\"v\":\"微电影\"},{\"n\":\"历史\",\"v\":\"历史\"}]";
      //动漫的类型
      String D_leixing="[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"情感\",\"v\":\"情感\"},{\"n\":\"科幻\",\"v\":\"科幻\"},{\"n\":\"热血\",\"v\":\"热血\"},{\"n\":\"推理\",\"v\":\"推理\"},{\"n\":\"搞笑\",\"v\":\"搞笑\"},{\"n\":\"冒险\",\"v\":\"冒险\"},{\"n\":\"萝莉\",\"v\":\"萝莉\"},{\"n\":\"校园\",\"v\":\"校园\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"机战\",\"v\":\"机战\"},{\"n\":\"运动\",\"v\":\"运动\"},{\"n\":\"战争\",\"v\":\"战争\"},{\"n\":\"少年\",\"v\":\"少年\"},{\"n\":\"少女\",\"v\":\"少女\"},{\"n\":\"社会\",\"v\":\"社会\"},{\"n\":\"原创\",\"v\":\"原创\"},{\"n\":\"亲子\",\"v\":\"亲子\"},{\"n\":\"益智\",\"v\":\"益智\"},{\"n\":\"励志\",\"v\":\"励志\"},{\"n\":\"其他\",\"v\":\"其他\"}]";

      //短剧的类型
        // []";
      String DJ_leixing="[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"古装\",\"v\":\"古装\"},{\"n\":\"战争\",\"v\":\"战争\"},{\"n\":\"青春偶像\",\"v\":\"青春偶像\"},{\"n\":\"喜剧\",\"v\":\"喜剧\"},{\"n\":\"家庭\",\"v\":\"家庭\"},{\"n\":\"犯罪\",\"v\":\"犯罪\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"奇幻\",\"v\":\"奇幻\"},{\"n\":\"剧情\",\"v\":\"剧情\"},{\"n\":\"历史\",\"v\":\"历史\"},{\"n\":\"经典\",\"v\":\"经典\"},{\"n\":\"乡村\",\"v\":\"乡村\"},{\"n\":\"情景\",\"v\":\"情景\"},{\"n\":\"商战\",\"v\":\"商战\"},{\"n\":\"网剧\",\"v\":\"网剧\"},{\"n\":\"其他\",\"v\":\"其他\"}]";
      //电影和电视的地区
      String M_and_T_diqu="[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"大陆\",\"v\":\"大陆\"},{\"n\":\"香港\",\"v\":\"香港\"},{\"n\":\"台湾\",\"v\":\"台湾\"},{\"n\":\"美国\",\"v\":\"美国\"},{\"n\":\"法国\",\"v\":\"法国\"},{\"n\":\"英国\",\"v\":\"英国\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"韩国\",\"v\":\"韩国\"},{\"n\":\"德国\",\"v\":\"德国\"},{\"n\":\"泰国\",\"v\":\"泰国\"},{\"n\":\"印度\",\"v\":\"印度\"},{\"n\":\"意大利\",\"v\":\"意大利\"},{\"n\":\"西班牙\",\"v\":\"西班牙\"},{\"n\":\"加拿大\",\"v\":\"加拿大\"},{\"n\":\"其他\",\"v\":\"其他\"}]";
      //综艺的地区
      String Z_diqu="[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"大陆\",\"v\":\"大陆\"},{\"n\":\"香港\",\"v\":\"香港\"},{\"n\":\"台湾\",\"v\":\"台湾\"},{\"n\":\"美国\",\"v\":\"美国\"},{\"n\":\"法国\",\"v\":\"法国\"},{\"n\":\"英国\",\"v\":\"英国\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"韩国\",\"v\":\"韩国\"}]";


      //电影，电视，动漫语言
      String M_T_and_D_yuyan="[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"国语\",\"v\":\"国语\"},{\"n\":\"英语\",\"v\":\"英语\"},{\"n\":\"粤语\",\"v\":\"粤语\"},{\"n\":\"闽南语\",\"v\":\"闽南语\"},{\"n\":\"韩语\",\"v\":\"韩语\"},{\"n\":\"日语\",\"v\":\"日语\"},{\"n\":\"法语\",\"v\":\"法语\"},{\"n\":\"德语\",\"v\":\"德语\"},{\"n\":\"其他\",\"v\":\"其他\"}]";
      //除音乐以外的所有日期
      String A_Time="[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2024\",\"v\":\"2024\"},{\"n\":\"2023\",\"v\":\"2023\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2011\",\"v\":\"2011\"},{\"n\":\"2010\",\"v\":\"2010\"}]";
      //所有的字母
      String A_Z="[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"A\",\"v\":\"A\"},{\"n\":\"B\",\"v\":\"B\"},{\"n\":\"C\",\"v\":\"C\"},{\"n\":\"D\",\"v\":\"D\"},{\"n\":\"E\",\"v\":\"E\"},{\"n\":\"F\",\"v\":\"F\"},{\"n\":\"G\",\"v\":\"G\"},{\"n\":\"H\",\"v\":\"H\"},{\"n\":\"I\",\"v\":\"I\"},{\"n\":\"J\",\"v\":\"J\"},{\"n\":\"K\",\"v\":\"K\"},{\"n\":\"L\",\"v\":\"L\"},{\"n\":\"M\",\"v\":\"M\"},{\"n\":\"N\",\"v\":\"N\"},{\"n\":\"O\",\"v\":\"O\"},{\"n\":\"P\",\"v\":\"P\"},{\"n\":\"Q\",\"v\":\"Q\"},{\"n\":\"R\",\"v\":\"R\"},{\"n\":\"S\",\"v\":\"S\"},{\"n\":\"T\",\"v\":\"T\"},{\"n\":\"U\",\"v\":\"U\"},{\"n\":\"V\",\"v\":\"V\"},{\"n\":\"W\",\"v\":\"W\"},{\"n\":\"X\",\"v\":\"X\"},{\"n\":\"Y\",\"v\":\"Y\"},{\"n\":\"Z\",\"v\":\"Z\"},{\"n\":\"0-9\",\"v\":\"0-9\"}]";
      //排序
      String PX="[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"时间排序\",\"v\":\"time\"},{\"n\":\"人气排序\",\"v\":\"hits\"},{\"n\":\"评分排序\",\"v\":\"score\"}]";





      // f="";


      f="{\"/1\":[{\"key\":\"L\",\"name\":\"类型\",\"value\":"+M_and_T_leixing+"},{\"key\":\"D\",\"name\":\"地区\",\"value\":"+M_and_T_diqu+"},{\"key\":\"Y\",\"name\":\"语言\",\"value\":"+M_T_and_D_yuyan+"},{\"key\":\"T\",\"name\":\"时间\",\"value\":"+A_Time+"},{\"key\":\"AZ\",\"name\":\"字母\",\"value\":"+A_Z+"},{\"key\":\"PX\",\"name\":\"排序\",\"value\":"+PX+"}],\"/2\":[{\"key\":\"L\",\"name\":\"类型\",\"value\":"+M_and_T_leixing+" },{\"key\":\"D\",\"name\":\"地区\",\"value\":"+M_and_T_diqu+"},{\"key\":\"Y\",\"name\":\"语言\",\"value\":"+M_T_and_D_yuyan+"},{\"key\":\"T\",\"name\":\"时间\",\"value\":"+A_Time+"},{\"key\":\"AZ\",\"name\":\"字母\",\"value\":"+A_Z+"},{\"key\":\"PX\",\"name\":\"排序\",\"value\":"+PX+"}],\"/3\":[{\"key\":\"L\",\"name\":\"类型\",\"value\":"+D_leixing+" },{\"key\":\"D\",\"name\":\"地区\",\"value\":"+M_and_T_diqu+"},{\"key\":\"Y\",\"name\":\"语言\",\"value\":"+M_T_and_D_yuyan+"},{\"key\":\"T\",\"name\":\"时间\",\"value\":"+A_Time+"},{\"key\":\"AZ\",\"name\":\"字母\",\"value\":"+A_Z+"},{\"key\":\"PX\",\"name\":\"排序\",\"value\":"+PX+"}],\"/4\":[{\"key\":\"D\",\"name\":\"地区\",\"value\":"+Z_diqu+"},{\"key\":\"T\",\"name\":\"时间\",\"value\":"+A_Time+"},{\"key\":\"AZ\",\"name\":\"字母\",\"value\":"+A_Z+"},{\"key\":\"PX\",\"name\":\"排序\",\"value\":"+PX+"}],\"/6\":[{\"key\":\"L\",\"name\":\"类型\",\"value\":"+DJ_leixing+"},{\"key\":\"T\",\"name\":\"时间\",\"value\":"+A_Time+"},{\"key\":\"AZ\",\"name\":\"字母\",\"value\":"+A_Z+"},{\"key\":\"PX\",\"name\":\"排序\",\"value\":"+PX+"}],\"/5\":[{\"key\":\"AZ\",\"name\":\"字母\",\"value\":"+A_Z+"},{\"key\":\"PX\",\"name\":\"排序\",\"value\":"+PX+"}]}";

      return f;
    } catch (Exception e) {
      return "";
    }

  }
}
