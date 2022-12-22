package com.github.catvod.spider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import okhttp3.Call;
import okhttp3.Response;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.catvod.utils.okhttp.OKCallBack;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;

public class Douban extends Spider {

    private static final String siteUrl = "https://movie.douban.com";

    public HashMap<String, String> headers = new HashMap<>();

    public void init(Context context) {
        super.init(context);
        HashMap<String, String> h1 = new HashMap<>();
        h1.put("User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        OkHttpUtil.get(OkHttpUtil.defaultClient(), "https://movie.douban.com", new OKCallBack<Response>() {
            @Override
            protected void onResponse(Response response) {
                String str_bid = response.header("Set-Cookie");
                String pattern_bid = "(bid=.*?);";
                Pattern r = Pattern.compile(pattern_bid);
                Matcher m = r.matcher(str_bid);
                if (m.find()) {
                    h1.put("Cookie", m.group(1));
                }

            }

            @Override
            protected void onFailure(Call call, Exception e) {

            }

            protected Response onParseResponse(Call call, Response response) {
                return response;
            }

        });
        this.headers = h1;
        this.headers.put("Referer", "https://movie.douban.com/");
    }

    public String homeContent(boolean filter) {
        try {
            // 电视剧
            // https://m.douban.com/rexxar/api/v2/tv/recommend?refresh=0&start=20&count=20&selected_categories={"地区":"华语"}&uncollect=false&tags=华语
            // 电影
            // https://m.douban.com/rexxar/api/v2/movie/recommend?refresh=0&start=0&count=20&selected_categories={}&uncollect=false&tags=
            JSONObject result = new JSONObject();
            JSONArray classes = new JSONArray();

            JSONObject dianying = new JSONObject();
            JSONObject dianshiju = new JSONObject();
            JSONObject dongman = new JSONObject();
            JSONObject zongyi = new JSONObject();

            dianying.put("type_name", "电影");
            dianying.put("type_id", "/rexxar/api/v2/movie");

            dianshiju.put("type_name", "电视剧");
            dianshiju.put("type_id", "/rexxar/api/v2/tv");

            // dongman.put("type_name", "动漫");

            // zongyi.put("type_name", "综艺");
            classes.put(dianying);
            classes.put(dianshiju);

            result.put("class", classes);
            if (filter) {
                String filterconfig = "{\"/rexxar/api/v2/movie\": [{\"key\": \"类型\",\"name\": \"类型\", \"value\": [{ \"n\": \"全部\", \"v\": \"\" }, {\"n\": \"喜剧\",\"v\": \"喜剧\"}, {\"n\": \"爱情\",\"v\": \"爱情\"}, {\"n\": \"动作\",\"v\": \"动作\"}, {\"n\": \"科幻\",\"v\": \"科幻\"}, {\"n\": \"悬疑\",\"v\": \"悬疑\"}, {\"n\": \"犯罪\",\"v\": \"犯罪\"}, {\"n\": \"历史\",\"v\": \"历史\"}, {\"n\": \"奇幻\",\"v\": \"奇幻\"}, {\"n\": \"恐怖\",\"v\": \"恐怖\"},{\"n\": \"战争\",\"v\": \"战争\"} ,{\"n\": \"武侠\",\"v\": \"武侠\"}]}],\"/rexxar/api/v2/tv\": [{\"key\": \"类型\",\"name\": \"类型\", \"value\": [{ \"n\": \"全部\", \"v\": \"\" }, {\"n\": \"喜剧\",\"v\": \"喜剧\"}, {\"n\": \"爱情\",\"v\": \"爱情\"}, {\"n\": \"动作\",\"v\": \"动作\"}, {\"n\": \"科幻\",\"v\": \"科幻\"}, {\"n\": \"悬疑\",\"v\": \"悬疑\"}, {\"n\": \"犯罪\",\"v\": \"犯罪\"}, {\"n\": \"历史\",\"v\": \"历史\"}, {\"n\": \"奇幻\",\"v\": \"奇幻\"}, {\"n\": \"恐怖\",\"v\": \"恐怖\"},{\"n\": \"战争\",\"v\": \"战争\"} ,{\"n\": \"武侠\",\"v\": \"武侠\"}]}]}";
                // https://m.douban.com/rexxar/api/v2/movie/recommend?refresh=0&start=0&count=20&selected_categories={"地区":"华语"}&uncollect=false&tags=华语"
                // https://m.douban.com/rexxar/api/v2/movie/recommend?refresh=0&start=0&count=20&selected_categories={"地区":"华语","类型":"爱情"}&uncollect=false&tags=华语,爱情
                // https://m.douban.com/rexxar/api/v2/tv/recommend?refresh=0&start=0&count=20&selected_categories={"类型":"科幻","形式":"电视剧"}&uncollect=false&tags=科幻
                // https://m.douban.com/rexxar/api/v2/tv/recommend?refresh=0&start=0&count=20&selected_categories={"类型":"科幻","形式":"电视剧","地区":"华语"}&uncollect=false&tags=科幻,华语
                result.put("filters", new JSONObject(filterconfig));

            }

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

            int page = Integer.parseInt(pg);
            String res = OkHttpUtil.string(
                    "https://m.douban.com/" + tid + "/recommend?refresh=0&start=" + (page - 1) * 20
                            + "&count=20&selected_categories={\"地区\":\"华语\"}&uncollect=false&tags=华语",
                    headers);
            JSONObject json_res = new JSONObject(res);
            JSONArray items = json_res.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject info = new JSONObject();
                info.put("vod_id", items.getJSONObject(i).getString("id"));
                info.put("vod_name", items.getJSONObject(i).getString("title"));
                info.put("vod_pic", items.getJSONObject(i).getJSONObject("pic").getString("large"));
                try {
                    info.put("vod_remarks", items.getJSONObject(i).getJSONObject("rating").get("value") + "分");

                } catch (Exception e) {
                    info.put("vod_remarks", "暂无评分");
                }

                list.put(info);
            }
            result.put("page", page);
            result.put("pagecount", Integer.MAX_VALUE);
            result.put("limit", 20);
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
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");
        return headers;
    }
}
