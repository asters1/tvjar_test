package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Comparator;

import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

import com.github.catvod.utils.okhttp.OKCallBack;
import okhttp3.Call;

public class Xiaoya extends Spider {

    private static final String siteUrl = "http://alist.xiaoya.pro";

    protected ArrayList<Integer> s1 = new ArrayList<Integer>();

    public void init(Context context) {
        super.init(context);
        s1.add(0);
    }

    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");
        return headers;
    }

    public String homeContent(boolean filter) {
        try {

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
            JSONObject info = new JSONObject();
            JSONArray list_info = new JSONArray();

            LinkedHashMap<String, ArrayList<String>> source = new LinkedHashMap<>();
            ArrayList<String> source1 = new ArrayList<String>();
            OkHttpUtil.postJson(OkHttpUtil.defaultClient(), siteUrl + "/api/fs/list",
                    "{\"path\":\"" + ids.get(0) + "\",\"password\": \"\"}",
                    new OKCallBack.OKCallBackString() {
                        @Override
                        protected void onFailure(Call call, Exception e) {

                        }

                        @Override
                        protected void onResponse(String response) {
                            try {
                                JSONObject res = new JSONObject(response);
                                JSONObject data = res.getJSONObject("data");
                                for (int i = 0; i < data.getInt("total"); i++) {
                                    s1.set(0, i);
                                    if (data.getJSONArray("content").getJSONObject(i).getBoolean("is_dir")) {
                                        OkHttpUtil.postJson(OkHttpUtil.defaultClient(),
                                                siteUrl + "/api/fs/list",
                                                "{\"path\":\"" + ids.get(0) + "/"
                                                        + data.getJSONArray("content").getJSONObject(i)
                                                                .getString("name")
                                                        + "\",\"password\": \"\"}",
                                                new OKCallBack.OKCallBackString() {
                                                    @Override
                                                    protected void onFailure(Call call, Exception e) {

                                                    }

                                                    @Override
                                                    protected void onResponse(String response1) {
                                                        try {
                                                            ArrayList<String> sourcei = new ArrayList<String>();
                                                            JSONObject res1 = new JSONObject(response1);
                                                            JSONObject data1 = res1.getJSONObject("data");
                                                            for (int j = 0; j < data1.getInt("total"); j++) {
                                                                if (!data1.getJSONArray("content").getJSONObject(j)
                                                                        .getBoolean("is_dir")) {
                                                                    sourcei.add(
                                                                            data1.getJSONArray("content")
                                                                                    .getJSONObject(j)
                                                                                    .getString("name"));
                                                                }
                                                            }

                                                            sourcei.sort(Comparator.naturalOrder());

                                                            source.put(data.getJSONArray("content")
                                                                    .getJSONObject(s1.get(0))
                                                                    .getString("name"), sourcei);
                                                        } catch (Exception e) {
                                                            SpiderDebug.log(e);
                                                        }
                                                    }
                                                });
                                    } else {
                                        source1.add(data.getJSONArray("content").getJSONObject(i).getString("name"));

                                    }
                                }
                                source1.sort(Comparator.naturalOrder());
                                source.put("小雅", source1);
                            } catch (Exception e) {
                                SpiderDebug.log(e);
                            }
                        }
                    });

            info.put("vod_id", ids.get(0));

            if (ids.get(0).indexOf("/") != -1) {
                info.put("vod_name", ids.get(0).substring(ids.get(0).lastIndexOf("/") + 1, ids.get(0).length()));
            } else {
                info.put("vod_name", ids.get(0));

            }

            ArrayList<String> vpf = new ArrayList<String>();
            for (String k : source.keySet()) {
                vpf.add(k);
            }
            Collections.reverse(vpf);

            String vod_play_from = "";
            String vod_play_url = "";
            for (int i = 0; i < vpf.size(); i++) {

                String vname = "";
                if (!vpf.get(i).equals("小雅")) {
                    vname = vpf.get(i) + "/";
                }

                if (i == vpf.size() - 1) {

                    vod_play_from = vod_play_from + vpf.get(i);
                } else {
                    vod_play_from = vod_play_from + vpf.get(i) + "$$$";

                }

                for (int j = 0; j < source.get(vpf.get(i)).size(); j++) {
                    if (CheckName(source.get(vpf.get(i)).get(j))) {
                        if (j == source.get(vpf.get(i)).size() - 1) {

                            vod_play_url = vod_play_url + source.get(vpf.get(i)).get(j) + "$" + ids.get(0) + "/" + vname
                                    + source.get(vpf.get(i)).get(j) + "$$$";
                        } else {
                            vod_play_url = vod_play_url + source.get(vpf.get(i)).get(j) + "$" + ids.get(0) + "/" + vname
                                    + source.get(vpf.get(i)).get(j) + "#";
                        }
                    }
                }
            }

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
            JSONObject result = new JSONObject();
            JSONArray list = new JSONArray();
            String search_url = siteUrl + "/search?box=" + key + "&url=";
            String content = OkHttpUtil.string(search_url, getHeaders());
            Elements list_el = Jsoup.parse(content).getElementsByTag("div").get(0).getElementsByTag("a");
            for (int i = 0; i < list_el.size(); i++) {
                String path = list_el.get(i).attr("href");
                String name = "";
                int index = path.indexOf(key);
                if (index == -1) {

                    name = path;
                } else {
                    name = path.substring(index).replace("%20", " ");

                }

                if (path.substring(0, 5).indexOf("书") == -1) {

                    if (CheckName(name)) {
                        JSONObject info = new JSONObject();
                        info.put("vod_id", path);
                        info.put("vod_name", name);
                        list.put(info);
                    }
                }
            }
            result.put("list", list);
            System.out.println(result);
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

    protected boolean CheckName(String str) {
        String[] check_str = { ".mp3", ".ts", ".jpg", ".nfo", "flac", ".md", ".png", ".ass", ".JPG", ".PNG", ".txt",
                ".TXT" };

        for (int i = 0; i < check_str.length; i++) {
            System.out.println(str.indexOf(check_str[i]));
            if (str.indexOf(check_str[i]) != -1) {

                return false;
            }
        }
        return true;
    }

    protected String getraw(String path) {
        try {
            String get_raw_url = siteUrl + "/api/fs/get";
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
