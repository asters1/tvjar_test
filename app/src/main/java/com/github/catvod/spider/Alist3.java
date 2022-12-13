package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.List;

import com.github.catvod.utils.okhttp.OKCallBack;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

import okhttp3.Call;

public class Alist3 extends Spider {

    private JSONObject get_json(String str) {
        try {
            return new JSONObject(OkHttpUtil.string(str, null));

        } catch (Exception e) {
            return null;
        }

    }

    private String get_jsons(String str) {
        try {
            String result = j_ext.getString("url");
            return result;

        } catch (Exception e) {
            return "";
        }

    }

    private JSONObject j_ext = get_json("http://localhost:8080/1.json");
    private String siteUrl = get_jsons("url");

    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");
        return headers;
    }

    public String homeContent(boolean filter) {
        try {
            JSONObject result = new JSONObject();
            JSONArray classses = new JSONArray();
            OkHttpUtil.postJson(OkHttpUtil.defaultClient(), siteUrl + "/api/fs/list",
                    "{\"path\":\"" + j_ext.getString("path") + "\",\"password\": \"\"}",
                    new OKCallBack.OKCallBackString() {
                        @Override
                        protected void onFailure(Call call, Exception e) {

                        }

                        @Override
                        protected void onResponse(String response) {
                            try {
                                JSONObject res = new JSONObject(response);
                                JSONObject fenlei = res.getJSONObject("data");
                                for (int i = 0; i < fenlei.getInt("total"); i++) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("type_name",
                                            fenlei.getJSONArray("content").getJSONObject(i).getString("name"));
                                    jsonObject.put("type_id",
                                            j_ext.getString("path") + "/"
                                                    + fenlei.getJSONArray("content").getJSONObject(i)
                                                            .getString("name"));
                                    classses.put(jsonObject);
                                }
                                result.put("class", classses);

                            } catch (Exception e) {
                                SpiderDebug.log(e);
                            }
                        }
                    });
            return result.toString();

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {
            JSONObject result = new JSONObject();
            JSONArray list = new JSONArray();
            OkHttpUtil.postJson(OkHttpUtil.defaultClient(), siteUrl + "/api/fs/list",
                    "{\"path\":\"" + tid + "\",\"password\": \"\"}",
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
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("vod_name",
                                            data.getJSONArray("content").getJSONObject(i).getString("name"));
                                    jsonObject.put("vod_id",
                                            tid + "/"
                                                    + data.getJSONArray("content").getJSONObject(i).getString("name"));
                                    jsonObject.put("vod_pic",

                                            getraw(tid + "/"
                                                    + data.getJSONArray("content").getJSONObject(i).getString("name")

                                                    + "/1.jpg"));
                                    list.put(jsonObject);

                                }
                            } catch (Exception e) {
                                SpiderDebug.log(e);
                            }
                        }
                    });
            result.put("page", 1);
            result.put("pagecount", 1);
            result.put("limit", Integer.MAX_VALUE);
            result.put("total", Integer.MAX_VALUE);
            result.put("list", list);
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
            JSONArray json_i = new JSONArray();
            JSONArray json_vod_play_url = new JSONArray();
            json_vod_play_url.put(0, "");
            JSONArray json_vod_play_from = new JSONArray();
            json_vod_play_from.put(0, "");

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
                                System.out.println(response);
                                info.put("vod_id", ids.get(0));
                                for (int i = 0; i < data.getInt("total"); i++) {
                                    if (data.getJSONArray("content").getJSONObject(i).getBoolean("is_dir")) {

                                        if (i < data.getInt("total") - 2) {

                                            json_vod_play_from.put(0, json_vod_play_from.get(0)
                                                    + data.getJSONArray("content").getJSONObject(i).getString("name")
                                                    + "$$$");
                                        } else {
                                            json_vod_play_from.put(0, json_vod_play_from.get(0)
                                                    + data.getJSONArray("content").getJSONObject(i).getString("name"));
                                        }
                                        json_i.put(0, i);

                                        OkHttpUtil.postJson(OkHttpUtil.defaultClient(), siteUrl + "/api/fs/list",
                                                "{\"path\":\"" + ids.get(0) + "/"
                                                        + data.getJSONArray("content").getJSONObject(i)
                                                                .getString("name")
                                                        + "\",\"password\": \"\"}",
                                                new OKCallBack.OKCallBackString() {
                                                    @Override
                                                    protected void onFailure(Call call, Exception e) {

                                                    }

                                                    @Override
                                                    protected void onResponse(String response) {
                                                        try {
                                                            JSONObject res1 = new JSONObject(response);
                                                            JSONObject data1 = res1.getJSONObject("data");
                                                            String list_url = "";
                                                            for (int j = 0; j < data1.getInt("total"); j++) {
                                                                if (j < data1.getInt("total") - 1) {
                                                                    json_vod_play_url.put(0, json_vod_play_url.get(0) +

                                                                            data1.getJSONArray("content")
                                                                                    .getJSONObject(j)
                                                                                    .getString("name")
                                                                            + "$" + ids.get(0) + "/"
                                                                            + data.getJSONArray("content")
                                                                                    .getJSONObject(json_i.getInt(0))
                                                                                    .getString("name")
                                                                            + "/"
                                                                            + data1.getJSONArray("content")
                                                                                    .getJSONObject(j)
                                                                                    .getString("name")
                                                                            + "#");
                                                                } else {
                                                                    json_vod_play_url.put(0, json_vod_play_url.get(0) +

                                                                            data1.getJSONArray("content")
                                                                                    .getJSONObject(j)
                                                                                    .getString("name")
                                                                            + "$" + ids.get(0) + "/"
                                                                            + data.getJSONArray("content")
                                                                                    .getJSONObject(json_i.getInt(0))
                                                                                    .getString("name")
                                                                            + "/"
                                                                            + data1.getJSONArray("content")
                                                                                    .getJSONObject(j)
                                                                                    .getString("name")
                                                                            + "$$$");
                                                                }
                                                            }
                                                            json_vod_play_url.put(list_url);
                                                        } catch (Exception e) {
                                                            SpiderDebug.log(e);
                                                        }

                                                    }
                                                });

                                    }

                                }
                            } catch (Exception e) {
                                SpiderDebug.log(e);
                            }

                        }
                    });
            info.put("vod_play_from", json_vod_play_from.getString(0));
            info.put("vod_play_url", json_vod_play_url.getString(0));
            list_info.put(info);
            result.put("list", list_info);
            System.out.println(result);
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
