package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import java.security.MessageDigest;

import com.github.catvod.utils.Misc;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class Kunyu77 extends Spider {
    private static final String siteUrl = "https://api.kunyu77.com";

    protected HashMap<String, String> getHeaders1(String t, String str) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 11; M2012K11AC Build/RKQ1.200826.002)");
        headers.put("t", t);
        headers.put("TK", getMd5(str));
        return headers;
    }

    protected HashMap<String, String> getHeaders(String t, String str) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "okhttp/3.12.0");
        headers.put("t", t);
        headers.put("TK", getMd5(str));
        return headers;
    }

    public String homeContent(boolean filter) {
        try {
            JSONObject result = new JSONObject();
            JSONArray classes = new JSONArray();

            JSONObject dianying = new JSONObject();
            JSONObject dianshiju = new JSONObject();
            JSONObject dongman = new JSONObject();
            JSONObject zongyi = new JSONObject();
            JSONObject quanbu = new JSONObject();

            quanbu.put("type_id", "0");
            quanbu.put("type_name", "全部");

            dianying.put("type_id", "1");
            dianying.put("type_name", "电影");

            dianshiju.put("type_id", "2");
            dianshiju.put("type_name", "电视剧");

            zongyi.put("type_id", "3");
            zongyi.put("type_name", "综艺");

            dongman.put("type_id", "4");
            dongman.put("type_name", "动漫");

            classes.put(quanbu);
            classes.put(dianying);
            classes.put(dianshiju);
            classes.put(zongyi);
            classes.put(dongman);

            result.put("class", classes);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {
            String url = "http://api.kunyu77.com/api.php/provide/searchFilter?type_id=" + tid
                    + "&pagesize=24&pagenum=1&year=&category=&area=";
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String detailContent(List<String> ids) {
        try {

            JSONObject info = new JSONObject();
            JSONArray list = new JSONArray();
            JSONObject result = new JSONObject();

            String t = String.valueOf(System.currentTimeMillis() / 1000);
            String str3 = "XSpeUFjJ";
            String str4 = "android7.1.22.0.4";
            String str5 = "RMX1931com.sevenVideo.app.android010110002";
            String str6 = "&pcode=010110002&version=2.0.4&devid=4ac3fe96a6133de96904b8d3c8cfe16d&package=com.sevenVideo.app.android&sys=android&sysver=7.1.2&brand=realme&model=RMX1931&sj=";
            String str7 = "?ids=";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.kunyu77.com/api.php/provide/videoDetail");
            stringBuilder.append(str7);
            stringBuilder.append((String) ids.get(0));
            stringBuilder.append(str6);
            stringBuilder.append(t);
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("/api.php/provide/videoDetailrealme4ac3fe96a6133de96904b8d3c8cfe16d");
            stringBuilder3.append((String) ids.get(0));
            stringBuilder3.append(str5);
            stringBuilder3.append(t);
            stringBuilder3.append(str4);
            stringBuilder3.append(t);
            stringBuilder3.append(str3);
            JSONObject res_JsonObject = new JSONObject(
                    OkHttpUtil.string("http://api.kunyu77.com/api.php/provide/videoDetail?ids=" + ids.get(0)
                            + "&pcode=010110002&version=2.0.4&devid=4ac3fe96a6133de96904b8d3c8cfe16d&package=com.sevenVideo.app.android&sys=android&sysver=7.1.2&brand=realme&model=RMX1931&sj="
                            + t, getHeaders1(t, stringBuilder3.toString())));

            String vod_name = res_JsonObject.getJSONObject("data").getString("videoName");
            String vod_id = ids.get(0);
            String vod_year = res_JsonObject.getJSONObject("data").getString("year");
            String vod_pic = res_JsonObject.getJSONObject("data").getString("videoCover");
            String vod_area = res_JsonObject.getJSONObject("data").getString("area");
            String vod_actor = res_JsonObject.getJSONObject("data").getString("actor");
            String vod_remarks = res_JsonObject.getJSONObject("data").getString("msg");
            String vod_director = res_JsonObject.getJSONObject("data").getString("director");
            String vod_content = res_JsonObject.getJSONObject("data").getString("brief").trim();
            String type_name = res_JsonObject.getJSONObject("data").getString("subCategory");

            info.put("vod_id", vod_id);
            info.put("vod_name", vod_name);
            info.put("vod_pic", vod_pic);
            info.put("type_name", type_name);
            info.put("vod_year", vod_year);
            info.put("vod_area", vod_area);
            info.put("vod_remarks", vod_remarks);
            info.put("vod_actor", vod_actor);
            info.put("vod_director", vod_director);
            info.put("vod_content", vod_content);

            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append("/api.php/provide/videoPlaylistrealme4ac3fe96a6133de96904b8d3c8cfe16d");
            stringBuilder5.append((String) ids.get(0));
            stringBuilder5.append("RMX1931com.sevenVideo.app.android010110002");
            stringBuilder5.append(t);
            stringBuilder5.append("android7.1.22.0.4");
            stringBuilder5.append(t);
            stringBuilder5.append("XSpeUFjJ");

            JSONObject play_url_JsonObject = new JSONObject(
                    OkHttpUtil.string("http://api.kunyu77.com/api.php/provide/videoPlaylist?ids=" + ids.get(0)
                            + "&pcode=010110002&version=2.0.4&devid=4ac3fe96a6133de96904b8d3c8cfe16d&package=com.sevenVideo.app.android&sys=android&sysver=7.1.2&brand=realme&model=RMX1931&sj="
                            + t, getHeaders1(t, stringBuilder5.toString())));
            JSONArray play_url_JsonArray = play_url_JsonObject.getJSONObject("data").getJSONArray("episodes");
            String play_url_name = "";

            LinkedHashMap<String, ArrayList<String>> playfrom_Map = new LinkedHashMap<String, ArrayList<String>>();
            for (int i = 0; i < play_url_JsonArray.length(); i++) {
                JSONArray jSONArray3 = play_url_JsonArray.getJSONObject(i)
                        .getJSONArray("playurls");
                for (int i2 = 0; i2 < jSONArray3.length(); i2++) {
                    JSONObject jSONObject5 = jSONArray3.getJSONObject(i2);
                    play_url_name = jSONObject5.getString("playfrom");
                    ArrayList<String> arrayList = playfrom_Map.get(play_url_name);
                    if (arrayList == null) {
                        arrayList = new ArrayList<String>();
                        playfrom_Map.put(play_url_name, arrayList);

                    }
                    String name = jSONObject5.getString("title").replace(vod_name, "").trim();
                    String playurl = jSONObject5.getString("playurl");
                    arrayList.add(name + "$" + playurl);

                }
            }
            String vod_play_url = "";
            String vod_play_from = "";

            int i = 0;
            for (String str : playfrom_Map.keySet()) {
                if (i < playfrom_Map.size() - 1) {
                    vod_play_from = vod_play_from + str + "$$$";
                } else {
                    vod_play_from = vod_play_from + str;

                }
                i++;

            }
            int j = 0;
            for (ArrayList<String> array : playfrom_Map.values()) {
                if (j < playfrom_Map.size() - 1) {
                    for (int k = 0; k < array.size(); k++) {
                        if (k < array.size() - 1) {
                            vod_play_url = vod_play_url + array.get(k) + "#";
                        } else {
                            vod_play_url = vod_play_url + array.get(k) + "$$$";

                        }
                    }
                } else {
                    for (int k = 0; k < array.size(); k++) {
                        if (k < array.size() - 1) {
                            vod_play_url = vod_play_url + array.get(k) + "#";
                        } else {
                            vod_play_url = vod_play_url + array.get(k);

                        }
                    }

                }
                j++;

            }

            info.put("vod_play_from", vod_play_from);
            info.put("vod_play_url", vod_play_url);

            list.put(info);
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
            String t = String.valueOf(System.currentTimeMillis() / 1000);
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("http://api.kunyu77.com/api.php/provide/searchVideo");
            stringBuilder.append(
                    "?pcode=010110002&version=2.0.4&devid=4ac3fe96a6133de96904b8d3c8cfe16d&package=com.sevenVideo.app.android&sys=android&sysver=7.1.2&brand=realme&model=RMX1931&sj=");
            stringBuilder.append(t);
            stringBuilder.append("&searchName=");
            stringBuilder.append(key);
            stringBuilder.append("&pg=1");
            String str1 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(
                    "/api.php/provide/searchVideorealme4ac3fe96a6133de96904b8d3c8cfe16dRMX1931com.sevenVideo.app.android0101100021");
            stringBuilder.append(key);
            stringBuilder.append(t);
            stringBuilder.append("android7.1.202.0.4");
            stringBuilder.append(t);
            stringBuilder.append("XSpeUFjJ");
            String res = OkHttpUtil.string(str1, getHeaders(t, stringBuilder.toString()));
            JSONObject jsonres = new JSONObject(res);
            JSONArray arrayres = jsonres.getJSONArray("data");
            for (int i = 0; i < arrayres.length(); i++) {
                JSONObject jSONObject = arrayres.getJSONObject(i);
                JSONObject jSONObject2 = new JSONObject();
                String string = jSONObject.getString("videoName");
                if (string.contains(key)) {
                    jSONObject2.put("vod_id", jSONObject.getInt("id") + "");
                    jSONObject2.put("vod_name", string);
                    jSONObject2.put("vod_pic", jSONObject.getString("videoCover"));
                    jSONObject2.put("vod_remarks", jSONObject.getString("msg"));
                    list.put(jSONObject2);
                }

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
            if (Misc.isVip(id)) {
                JSONObject result = new JSONObject();
                result.put("parse", 1);
                result.put("jx", "1");
                result.put("url", id);
                return result.toString();
            }
            JSONObject result = new JSONObject();
            result.put("parse", 0);
            result.put("playUrl", "");
            result.put("url", id);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

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
