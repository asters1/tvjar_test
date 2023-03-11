package com.github.catvod.spider;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhixc
 */
public class Ikan extends Spider {

    private static final String siteUrl = "https://ikanys.tv";

    // 请求头部设置
    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
        return headers;
    }

    /**
     * 首页内容
     * @param filter 是否开启筛选
     * @return
     */
    public String homeContent(boolean filter) {
        try {
            JSONObject result = new JSONObject();
            JSONArray classes = new JSONArray();

            JSONObject movieType = new JSONObject();
            JSONObject teleplayType = new JSONObject();
            JSONObject varietyType = new JSONObject();
            JSONObject anime = new JSONObject();
            JSONObject teleplayJK = new JSONObject();
            JSONObject teleplayUS = new JSONObject();
            JSONObject teleplayHT = new JSONObject();

            movieType.put("type_id", "/vodshow/1");
            movieType.put("type_name", "电影");

            teleplayType.put("type_id", "/vodshow/2");
            teleplayType.put("type_name", "连续剧");

            varietyType.put("type_id", "/vodshow/3");
            varietyType.put("type_name", "综艺");

            anime.put("type_id", "/vodshow/4");
            anime.put("type_name", "动漫");

            teleplayJK.put("type_id", "/vodshow/15");
            teleplayJK.put("type_name", "日韩剧");

            teleplayUS.put("type_id", "/vodshow/16");
            teleplayUS.put("type_name", "美剧");

            teleplayHT.put("type_id", "/vodshow/14");
            teleplayHT.put("type_name", "港台剧");


            classes.put(movieType);
            classes.put(teleplayType);
            classes.put(varietyType);
            classes.put(anime);
            classes.put(teleplayJK);
            classes.put(teleplayUS);
            classes.put(teleplayHT);

            result.put("class", classes);


            // filter 二级筛选 start
            if (filter){
                JSONObject filterConfig = new JSONObject("{\"/vodshow/1\": [{\"name\": \"年份\", \"key\": \"year\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}]}, {\"name\": \"地区\", \"key\": \"area\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"大陆\", \"v\": \"大陆\"}, {\"n\": \"中国香港\", \"v\": \"香港\"}, {\"n\": \"中国台湾\", \"v\": \"台湾\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"法国\", \"v\": \"法国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"德国\", \"v\": \"德国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"印度\", \"v\": \"印度\"}, {\"n\": \"意大利\", \"v\": \"意大利\"}, {\"n\": \"西班牙\", \"v\": \"西班牙\"}, {\"n\": \"加拿大\", \"v\": \"加拿大\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"name\": \"类型\", \"key\": \"class\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"动作\", \"v\": \"动作\"}, {\"n\": \"喜剧\", \"v\": \"喜剧\"}, {\"n\": \"爱情\", \"v\": \"爱情\"}, {\"n\": \"科幻\", \"v\": \"科幻\"}, {\"n\": \"恐怖\", \"v\": \"恐怖\"}, {\"n\": \"剧情\", \"v\": \"剧情\"}, {\"n\": \"战争\", \"v\": \"战争\"}, {\"n\": \"悬疑\", \"v\": \"悬疑\"}, {\"n\": \"冒险\", \"v\": \"冒险\"}, {\"n\": \"犯罪\", \"v\": \"犯罪\"}, {\"n\": \"奇幻\", \"v\": \"奇幻\"}, {\"n\": \"惊悚\", \"v\": \"惊悚\"}, {\"n\": \"青春\", \"v\": \"青春\"}, {\"n\": \"动画\", \"v\": \"动画\"}]}, {\"name\": \"排序\", \"key\": \"by\", \"value\": [{\"n\": \"最新\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"/vodshow/2\": [{\"name\": \"年份\", \"key\": \"year\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}]}, {\"name\": \"地区\", \"key\": \"area\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"内地\", \"v\": \"内地\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"香港\", \"v\": \"香港\"}, {\"n\": \"台湾\", \"v\": \"台湾\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"新加坡\", \"v\": \"新加坡\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"name\": \"类型\", \"key\": \"class\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"古装\", \"v\": \"古装\"}, {\"n\": \"战争\", \"v\": \"战争\"}, {\"n\": \"青春偶像\", \"v\": \"青春偶像\"}, {\"n\": \"喜剧\", \"v\": \"喜剧\"}, {\"n\": \"家庭\", \"v\": \"家庭\"}, {\"n\": \"犯罪\", \"v\": \"犯罪\"}, {\"n\": \"动作\", \"v\": \"动作\"}, {\"n\": \"奇幻\", \"v\": \"奇幻\"}, {\"n\": \"剧情\", \"v\": \"剧情\"}, {\"n\": \"历史\", \"v\": \"历史\"}, {\"n\": \"经典\", \"v\": \"经典\"}, {\"n\": \"乡村\", \"v\": \"乡村\"}, {\"n\": \"情景\", \"v\": \"情景\"}, {\"n\": \"商战\", \"v\": \"商战\"}, {\"n\": \"网剧\", \"v\": \"网剧\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"name\": \"排序\", \"key\": \"by\", \"value\": [{\"n\": \"最新\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"/vodshow/3\": [{\"name\": \"年份\", \"key\": \"year\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}]}, {\"name\": \"地区\", \"key\": \"area\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"内地\", \"v\": \"内地\"}, {\"n\": \"港台\", \"v\": \"港台\"}, {\"n\": \"日韩\", \"v\": \"日韩\"}, {\"n\": \"欧美\", \"v\": \"欧美\"}]}, {\"name\": \"排序\", \"key\": \"by\", \"value\": [{\"n\": \"最新\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"/vodshow/4\": [{\"name\": \"年份\", \"key\": \"year\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}]}, {\"name\": \"类型\", \"key\": \"class\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"番剧\", \"v\": \"番剧\"}, {\"n\": \"国创\", \"v\": \"国创\"}, {\"n\": \"动画片\", \"v\": \"动画片\"}]}, {\"name\": \"排序\", \"key\": \"by\", \"value\": [{\"n\": \"最新\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"/vodshow/15\": [{\"name\": \"年份\", \"key\": \"year\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}]}, {\"name\": \"排序\", \"key\": \"by\", \"value\": [{\"n\": \"最新\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"/vodshow/16\": [{\"name\": \"年份\", \"key\": \"year\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}]}, {\"name\": \"排序\", \"key\": \"by\", \"value\": [{\"n\": \"最新\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"/vodshow/14\": [{\"name\": \"年份\", \"key\": \"year\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}]}, {\"name\": \"排序\", \"key\": \"by\", \"value\": [{\"n\": \"最新\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}]}");
                result.put("filters", filterConfig);
            }
            // filter 二级筛选 end

            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 分类页面
     * @param tid   影片分类id值
     * @param pg    第几页
     * @param filter    二级筛选
     * @param extend
     * @return
     */
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {

            JSONObject result = new JSONObject();
            JSONArray jSONArray = new JSONArray();

            if (extend.get("area") == null) extend.put("area", "");
            if (extend.get("year") == null) extend.put("year", "");
            if (extend.get("by") == null) extend.put("by", "");
            if (extend.get("class") == null) extend.put("class", "");
            String area = URLEncoder.encode(extend.get("area")); // 中文需进行编码
            String year = extend.get("year");
            String by = extend.get("by");
            String classType = URLEncoder.encode(extend.get("class"));// 中文需进行编码

            String cateUrl = siteUrl + tid + String.format("-%s-%s-%s-----%s---%s", area, by, classType, pg, year);
            // 爱看电影分类页链接拼接后应该是这样的：
            // cateUrl = "https://ikanys.tv/vodshow/1-大陆-time-喜剧-----2---2022";
            String content = OkHttpUtil.string(cateUrl, getHeaders());

            Elements list_el = Jsoup.parse(content)
                    .select("[class=module-poster-item module-item]");

            for (int i = 0; i < list_el.size(); i++) {
                JSONObject vod = new JSONObject();
                Element item = list_el.get(i);
                String vod_id = item.attr("href");
                String vod_name = item.attr("title");
                String vod_pic = item.select("img").get(0).attr("data-original");
                String vod_remarks = item.select(".module-item-note").text();
                vod.put("vod_id", siteUrl + vod_id);
                vod.put("vod_name", vod_name);
                vod.put("vod_pic", vod_pic);
                vod.put("vod_remarks", vod_remarks);
                jSONArray.put(vod);

            }
            result.put("page", Integer.parseInt(pg));
            result.put("pagecount", Integer.MAX_VALUE);
            result.put("limit", list_el.size());
            result.put("total", Integer.MAX_VALUE);
            result.put("list", jSONArray);
            return result.toString();

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 详情页
     * @param ids
     * @return
     */
    public String detailContent(List<String> ids) {
        try {
            JSONObject result = new JSONObject();
            JSONObject info = new JSONObject();
            JSONArray list_info = new JSONArray();

            String detailUrl = ids.get(0);
//            String durl = ids.get(0);
            String content = OkHttpUtil.string(detailUrl, getHeaders());
            Elements sources = Jsoup.parse(content)
                    .select("[class=module-play-list]");

            String vod_play_url = ""; // 线路/播放源 里面的各集的播放页面链接
            String vod_play_from = "";  // 线路 / 播放源标题
            for (int i = 0; i < sources.size(); i++) {
                int b = i + 1;
                vod_play_from = vod_play_from + "源" + b + "$$$";

                Elements a_elment = sources.get(i).select("a");
                for (int j = 0; j < a_elment.size(); j++) {
                    if (j < a_elment.size() - 1) {
                        // 不是最后一集的通用写法
                        vod_play_url = vod_play_url + a_elment.get(j).text() + "$"
                                + siteUrl + a_elment.get(j).attr("href") + "#";
                    } else {
                        // 最后一集要特殊处理
                        vod_play_url = vod_play_url + a_elment.get(j).text() + "$"
                                + siteUrl + a_elment.get(j).attr("href") + "$$$";
                    }
                }
            }

            // 影片标题
            String title = Jsoup.parse(content)
                    .select(".module-info-heading")
                    .get(0).getElementsByTag("h1").text();

            // 图片
            String pic = Jsoup.parse(content)
                    .select("[class=ls-is-cached lazy lazyload]")
                    .get(0)
                    .attr("data-original");

            // 影片名称、图片等赋值
            info.put("vod_id", ids.get(0));
            info.put("vod_name", title);
            info.put("vod_pic", pic);

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

    /**
     * 搜索
     * @param key   关键字/词
     * @param quick     是否允许在播放页面发起快捷搜索
     * @return
     */
    public String searchContent(String key, boolean quick) {
        try {

            String url = siteUrl + "/index.php/ajax/suggest?mid=1&wd=" + URLEncoder.encode(key) + "&limit=20";
            JSONObject searchResult = new JSONObject(OkHttpUtil.string(url, getHeaders()));
            JSONObject result = new JSONObject();
            JSONArray videoInfo = new JSONArray();
            if (searchResult.getInt("total") > 0) {
                JSONArray lists = searchResult.getJSONArray("list");
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject vod = lists.getJSONObject(i);
                    String id = siteUrl + "/voddetail/" + vod.getInt("id");
//                    String id = "" + vod.getInt("id");
                    String title = vod.getString("name");
                    String cover = vod.getString("pic");
                    JSONObject v = new JSONObject();
                    v.put("vod_id", id);
                    v.put("vod_name", title);
                    v.put("vod_pic", cover);
                    v.put("vod_remarks", "");
                    videoInfo.put(v);
                }
            }
            result.put("list", videoInfo);
            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            SpiderDebug.log(e);
        }
        return "";
    }

    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {
            JSONObject result = new JSONObject();

            HashMap<String, String> headers = getHeaders();
//            headers.put("","");
            result.put("parse", 1);
            result.put("header", headers);
            result.put("playUrl", "");
            result.put("url", id);
            return result.toString();

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }
}
