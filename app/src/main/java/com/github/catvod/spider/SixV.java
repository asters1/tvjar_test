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

public class SixV extends Spider {

    private static final String siteUrl = "https://www.66s.cc";

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

            JSONObject comedy = new JSONObject();
            JSONObject actionMovie = new JSONObject();
            JSONObject romanticMovie = new JSONObject();
            JSONObject scientificMovie = new JSONObject();
            JSONObject horrorMovie = new JSONObject();
            JSONObject featureMovie = new JSONObject();
            JSONObject warMovie = new JSONObject();
            JSONObject documentary = new JSONObject();
            JSONObject anime = new JSONObject();
            JSONObject domesticDrama = new JSONObject();
            JSONObject japaneseAndKoreanDramas = new JSONObject();
            JSONObject europeanAndAmericanDramas = new JSONObject();

            comedy.put("type_id", "/xijupian");
            comedy.put("type_name", "喜剧片");

            actionMovie.put("type_id", "/dongzuopian");
            actionMovie.put("type_name", "动作片");

            romanticMovie.put("type_id", "/aiqingpian");
            romanticMovie.put("type_name", "爱情片");

            scientificMovie.put("type_id", "/kehuanpian");
            scientificMovie.put("type_name", "科幻片");

            horrorMovie.put("type_id", "/kongbupian");
            horrorMovie.put("type_name", "恐怖片");

            featureMovie.put("type_id", "/juqingpian");
            featureMovie.put("type_name", "剧情片");

            warMovie.put("type_id", "/zhanzhengpian");
            warMovie.put("type_name", "战争片");

            documentary.put("type_id", "/jilupian");
            documentary.put("type_name", "纪录片");

            anime.put("type_id", "/donghuapian");
            anime.put("type_name", "动画片");

            domesticDrama.put("type_id", "/dianshiju/guoju");
            domesticDrama.put("type_name", "国产剧");

            japaneseAndKoreanDramas.put("type_id", "/dianshiju/rihanju");
            japaneseAndKoreanDramas.put("type_name", "日韩剧");

            europeanAndAmericanDramas.put("type_id", "/dianshiju/oumeiju");
            europeanAndAmericanDramas.put("type_name", "欧美剧");


            classes.put(comedy);
            classes.put(actionMovie);
            classes.put(romanticMovie);
            classes.put(scientificMovie);
            classes.put(horrorMovie);
            classes.put(featureMovie);
            classes.put(warMovie);
            classes.put(documentary);
            classes.put(anime);
            classes.put(domesticDrama);
            classes.put(japaneseAndKoreanDramas);
            classes.put(europeanAndAmericanDramas);

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

            // 第一页
            // https://www.66s.cc/xijupian
            // 第二页
            // https://www.66s.cc/xijupian/index_2.html
            String cateUrl = siteUrl + tid;
            if (!pg.equals("1")){
                cateUrl += "/index_" + pg + ".html";
            }
            String content = OkHttpUtil.string(cateUrl, getHeaders());

            Elements list_el = Jsoup.parse(content)
                                .select("#post_container")
                                .select("[class=zoom]");

            for (int i = 0; i < list_el.size(); i++) {
                JSONObject vod = new JSONObject();
                Element item = list_el.get(i);
                String vod_id = item.attr("href");
                String vod_name = item.attr("title");
                String vod_pic = item.select("img").attr("src");
//                String vod_remarks = item.select(".pic-text").text();
                vod.put("vod_id", siteUrl + vod_id);
                vod.put("vod_name", vod_name);
                vod.put("vod_pic", vod_pic);
//                vod.put("vod_remarks", vod_remarks);
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
            String content = OkHttpUtil.string(detailUrl, getHeaders());
            Elements sources = Jsoup.parse(content)
                    .select("#post_content");

            // 磁力链接只能选择一条，多了，TVBox就无法识别播放了。
            // 另外磁力链接的播放，即使返回到首页，不在播放页面了，磁力依旧在后台继续下载
            // 以上这些问题估计只能考 TVBox 的作者去解决了。

            String vod_play_url = ""; // 线路/播放源 里面的各集的播放页面链接
            String vod_play_from = "";  // 线路 / 播放源标题
            for (int i = 0; i < sources.size(); i++) {
                int b = i + 1;
//                vod_play_from = vod_play_from + "源" + b + "$$$";
                vod_play_from = vod_play_from + "magnet" + "$$$";

                Elements aElemntArray = sources.get(i).select("table").select("a");
                for (int j = 0; j < aElemntArray.size(); j++) {
                    if (!vod_play_url.equals("")){
                        // 如果已经有一条磁力链接了，那么退出for循环
                        // 因为多条磁力链接，TVBox 似乎不会识别播放
                        break;
                    }
                    String href = aElemntArray.get(j).attr("href");
                    if (!href.startsWith("magnet")){
                        // 不是磁力链接，略过
                        continue;
                    }
                    if (j < aElemntArray.size() - 1) {
                        // 磁力链接不需要拼接 siteUrl，因为已经是直链了
                        // 不是最后一集的通用写法
                        vod_play_url = vod_play_url + aElemntArray.get(j).text() + "$"
                               + href + "#";
                    } else {
                        // 最后一集要特殊处理
                        vod_play_url = vod_play_url + aElemntArray.get(j).text() + "$"
                                + href + "$$$";
                    }
                }
            }

            // 影片标题
            String title = Jsoup.parse(content)
                    .select(".article_container")
                    .get(0).getElementsByTag("h1").text();

            // 图片
            String pic = Jsoup.parse(content)
                    .select("#post_content")
                    .select("img")
                    .attr("src");

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

            /*String url = siteUrl + "/e/search/index.php";
            JSONObject searchResult = new JSONObject(OkHttpUtil.string(url, getHeaders()));
            JSONObject result = new JSONObject();
            JSONArray videoInfo = new JSONArray();
            if (searchResult.getInt("total") > 0) {
                JSONArray lists = searchResult.getJSONArray("list");
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject vod = lists.getJSONObject(i);
                    String id = siteUrl + "/voddetail/" + vod.getInt("id");
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
            return result.toString();*/

        } catch (Exception e) {
            e.printStackTrace();
            SpiderDebug.log(e);
        }
        return "";
    }

    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {
            JSONObject result = new JSONObject();

//            result.put("parse", 0);
            result.put("parse", 1);
            result.put("header", "");
            result.put("playUrl", "");
            result.put("url", id);
            return result.toString();

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }
}
