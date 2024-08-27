package com.github.catvod.spider;

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
import com.github.catvod.utils.okhttp.OKCallBack;
import okhttp3.Call;
import okhttp3.Response;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class Alist3 extends Spider {

  public String siteUrl = "";
  //ext为外部给的字符串
  public String ext = "";
  public String alist_path = "";
  public String alist_name = "";
  public String pic_path="";

  public void init(Context context,String ext) {
    try{
      super.init(context,ext);
      this.ext=ext;
      JSONObject extjson=new JSONObject(ext);
      this.alist_name=extjson.getString("name");
      this.siteUrl=extjson.getString("url");
      this.alist_path=extjson.getString("path");
      this.pic_path=extjson.getString("pic");

    } catch (Exception e) {
      SpiderDebug.log(e);
    }
  }

  public String homeContent(boolean filter) {
    try {
      // System.out.println(ext);
      JSONObject alist =new JSONObject();
      alist.put("type_name", this.alist_name);
      alist.put("type_id", "alist");
      JSONObject result = new JSONObject();
      JSONArray classes = new JSONArray();
      classes.put(alist);
      result.put("class", classes);
      return result.toString();


    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
    try {
      // System.out.println(alist_path);
      // System.out.println(siteUrl);
      JSONObject result_json = new JSONObject();
      JSONArray list = new JSONArray();
      OkHttpUtil.postJson(OkHttpUtil.defaultClient(), siteUrl+"/api/fs/list", "{\"path\":\""+alist_path+"\",\"password\": \"\"}", null, new OKCallBack.OKCallBackString() {
        @Override
        protected void onFailure(Call call, Exception e) {

        }

        @Override
        protected void onResponse(String response) {
          try{
            JSONObject res = new JSONObject(response);
            JSONObject data = res.getJSONObject("data");
            // System.out.println(data);
            for (int i = 0; i < data.getInt("total"); i++) {
              // System.out.println(i);
              JSONObject jsonObject = new JSONObject();
              jsonObject.put("vod_name",
                  data.getJSONArray("content").getJSONObject(i).getString("name"));
              // System.out.println(jsonObject.get("vod_name"));
              jsonObject.put("vod_id",
                  alist_path+"/"
                  + data.getJSONArray("content").getJSONObject(i).getString("name"));
              jsonObject.put("vod_pic",pic_path+data.getJSONArray("content").getJSONObject(i).getString("name")+".jpg");
              // System.out.println(pic_path+data.getJSONArray("content").getJSONObject(i).getString("name")+".jpg");

              // getraw( "/"+alist_path+"/"
              //   + data.getJSONArray("content").getJSONObject(i).getString("name")
              //
              //   + "/1.jpg"));
              list.put(jsonObject);
            }
          } catch (Exception e) {
            SpiderDebug.log(e);
          }

        }

      }
      );
      result_json.put("page", 1);
      result_json.put("pagecount", 1);
      result_json.put("limit", Integer.MAX_VALUE);
      result_json.put("total", Integer.MAX_VALUE);
      result_json.put("list", list);
      return result_json.toString();

    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String detailContent(List<String> ids) {

    try {
      // System.out.println(ids);
      String vod_name=ids.get(0).substring(ids.get(0).lastIndexOf("/")+1, ids.get(0).length());
      // System.out.println(vod_name);


      JSONObject info = new JSONObject();
      JSONArray list_info=new JSONArray();
      info.put("vod_name",vod_name);

      info.put("vod_id", ids.get(0));
      // System.out.println(ids.get(0));
      // info.put("vod_pic", getraw(ids.get(0)+"/1.jpg"));
      info.put("vod_pic", pic_path+vod_name+".jpg");

      // info.put("vod_play_from", "Alist");
      // info.put("vod_play_from", "Alist");
      ArrayList<String> vod_play_froms= new ArrayList<String>();
      JSONObject result = new JSONObject();
      OkHttpUtil.postJson(OkHttpUtil.defaultClient(),siteUrl+"/api/fs/list","{\"path\":\"" + ids.get(0) + "\",\"password\": \"\"}",null,

          new OKCallBack.OKCallBackString() {
            @Override
            protected void onFailure(Call call, Exception e) {

            }

            protected void onResponse(String response) {
              try {
                JSONObject res = new JSONObject(response);
                // System.out.println(response);
                ArrayList<String> play_froms= new ArrayList<String>();
                ArrayList<String> Alist3_play_froms= new ArrayList<String>();
                JSONObject data = res.getJSONObject("data");

                for (int i = 0; i < data.getInt("total"); i++) {
                  JSONObject content=data.getJSONArray("content").getJSONObject(i);
                  String name=
                    content.getString("name");
                  // System.out.println(data.getJSONArray("content").getJSONObject(i).toString());
                  if(content.getBoolean("is_dir")){
                    // System.out.println("{\"path\":\"" + ids.get(0) +"/"+name+ "\",\"password\": \"\"}");
                    OkHttpUtil.postJson(OkHttpUtil.defaultClient(),siteUrl+"/api/fs/list","{\"path\":\"" + ids.get(0) +"/"+name+ "\",\"password\": \"\"}",null,
                        new OKCallBack.OKCallBackString() {
                          @Override
                          protected void onFailure(Call call, Exception e) {

                          }

                          protected void onResponse(String response1) {
                            try {
                              String s_url="";
                              JSONObject res1 = new JSONObject(response1);

                              JSONObject data1 = res1.getJSONObject("data");
                              // System.out.println(data1.toString());

                              ArrayList<String> dir_play_froms= new ArrayList<String>();

                              // System.out.println("====");
                              // System.out.println(dir_play_froms.toString());
                              // System.out.println("====");
                              for (int j = 0; j < data1.getInt("total"); j++) {
                                if(!vod_play_froms.contains(name)){
                                  // vod_play_froms.add("Alist3");
                                  vod_play_froms.add(name);
                                }
                                JSONObject content=data1.getJSONArray("content").getJSONObject(j);
                                String id1=
                                  ids.get(0)+"/"+name+"/"
                                  + data1.getJSONArray("content").getJSONObject(j).getString("name");
                                String name1=
                                  content.getString("name");
                                dir_play_froms.add(name1+"$"+id1);
                                // surl=
                              }
                              String sdir=TextUtils.join("#", dir_play_froms);
                              play_froms.add(sdir);




                            } catch (Exception e) {
                              SpiderDebug.log(e);
                            }
                          }
                    }
                    );

                  }else{
                    if(!vod_play_froms.contains("Alist3")){
                      vod_play_froms.add("Alist3");
                    }
                    String id=
                      ids.get(0)+"/"
                      + data.getJSONArray("content").getJSONObject(i).getString("name");
                    Alist3_play_froms.add(name+"$"+id);

                  }

                }
                  // System.out.println(Alist3_play_froms.toString());
                  String astr=TextUtils.join("#", Alist3_play_froms);
                              if(!astr.equals("")){
                  play_froms.add(astr);
                              }
                // info.put("vod_play_url",TextUtils.join("#", play_froms));
                String vod_play_url=TextUtils.join("$$$", play_froms);
                String vod_play_from=TextUtils.join("$$$", vod_play_froms);
                // System.out.println(vod_play_from);
                info.put("vod_play_from", vod_play_from);
                // printLog("vod_play_from", vod_play_from);
                // System.out.println(vod_play_url);
                info.put("vod_play_url", vod_play_url);

                result.put("list", info);
                // System.out.println(info);
                // System.out.println(result);


              } catch (Exception e) {
                SpiderDebug.log(e);
              }
            }
      }


      );
      // System.out.println(info);
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
      // System.out.println(key);
      String search_url=siteUrl+"/api/fs/search";
      OkHttpUtil.postJson(OkHttpUtil.defaultClient(),search_url,"{\"parent\":\"/"+alist_path+"\",\"keywords\":\""+key+"\",\"scope\":1,\"page\":1,\"per_page\":100,\"password\":\"\"}",null,

          new OKCallBack.OKCallBackString() {
            @Override
            protected void onFailure(Call call, Exception e) {

            }

            protected void onResponse(String response) {
              // System.out.println(response);
              try {


                JSONObject res=new JSONObject(response);
                JSONObject data = res.getJSONObject("data");
                // System.out.println(data);
                for (int i = 0; i < data.getInt("total"); i++) {
                  JSONObject jsonObject=new JSONObject();
                  jsonObject.put("vod_id",
                      "/"+alist_path+"/"
                      + data.getJSONArray("content").getJSONObject(i).getString("name"));

                  jsonObject.put("vod_pic",pic_path+data.getJSONArray("content").getJSONObject(i).getString("name")+".jpg");
                  jsonObject.put("vod_name",
                      data.getJSONArray("content").getJSONObject(i).getString("name"));
                  // System.out.println(jsonObject);
                  list.put(jsonObject);
                }

              } catch (Exception e) {
                SpiderDebug.log(e);
              }
            }});
      result.put("list", list);
      return result.toString();
    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }

  public String playerContent(String flag, String id, List<String> vipFlags) {
    try {
      String url = getraw(id);
      JSONObject result = new JSONObject();
      result.put("header", "");
      result.put("parse", 0);
      result.put("url", url);
      result.put("playUrl", "");
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
      String str1 = "http://localhost:8080/" + str;
      String res = OkHttpUtil.string(str1, null);
      System.out.println(res);
    } catch (Exception e) {
    }
  }
  // ====================
  protected String getraw(String path) {
    try {
      // System.out.println(path);
      String get_raw_url =  siteUrl+"/api/fs/get";
      String jsonstr = "{\"path\": \"" + path + "\",\"password\": \"\"}";
      JSONArray result = new JSONArray();
      OkHttpUtil.postJson(OkHttpUtil.defaultClient(), get_raw_url, jsonstr,
          new OKCallBack.OKCallBackString() {
            @Override
            protected void onFailure(Call call, Exception e) {
            }

            @Override
            protected void onResponse(String response) {
              try {
                // System.out.println(response);
                JSONObject res = new JSONObject(response);
                JSONObject data = res.getJSONObject("data");
                result.put(data.getString("raw_url"));
              } catch (Exception e) {
                SpiderDebug.log(e);
              }
            }
          });
      return result.getString(0);

    } catch (Exception e) {
      SpiderDebug.log(e);
    }
    return "";
  }
}
