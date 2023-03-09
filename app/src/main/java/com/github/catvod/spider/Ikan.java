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

public class Ikan extends Spider {

    private static final String siteUrl = "http://ikanys.tv";

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

            movieType.put("type_id", "/vodshow/1--------");
            movieType.put("type_name", "电影");

            teleplayType.put("type_id", "/vodshow/2--------");
            teleplayType.put("type_name", "电视剧");

            varietyType.put("type_id", "/vodshow/3--------");
            varietyType.put("type_name", "综艺");

            anime.put("type_id", "/vodshow/4--------");
            anime.put("type_name", "动漫");


            classes.put(movieType);
            classes.put(teleplayType);
            classes.put(varietyType);
            classes.put(anime);

            result.put("class", classes);
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

            String cateUrl = siteUrl + tid + pg + "---";
            // 爱看电影分类页链接拼接后应该是这样的：
            // cateUrl = "http://ikanys.tv/vodshow/1--------2---";
            String content = OkHttpUtil.string(cateUrl, getHeaders());

            Elements list_el = Jsoup.parse(content)
                    .select("[class=myui-vodlist clearfix]")
                    .select("[class=myui-vodlist__thumb lazyload]");

            for (int i = 0; i < list_el.size(); i++) {
                JSONObject vod = new JSONObject();
                Element item = list_el.get(i);
                String vod_id = item.attr("href");
                String vod_name = item.attr("title");
                String vod_pic = item.attr("data-original");
                String vod_remarks = item.select(".pic-text").text();
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
                    .select("[class=tab-content myui-panel_bd]")
                    .get(0)
                    .select(".tab-pane");

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
                    .select(".myui-content__detail")
                    .get(0).getElementsByTag("h1").text();

            // 图片
            String pic = Jsoup.parse(content)
                    .select(".myui-content__thumb")
                    .get(0)
                    .select("img")
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
