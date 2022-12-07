package com.github.catvod.spider;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import com.github.catvod.crawler.Spider;

public class Base extends Spider {

    public String homeContent(boolean filter) {
        return "";
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        return "";
    }

    public String detailContent(List<String> ids) {
        return "";
    }

    public String searchContent(String key, boolean quick) {
        return "";
    }

    public String playerContent(String flag, String id, List<String> vipFlags) {
        return "";
    }
}
