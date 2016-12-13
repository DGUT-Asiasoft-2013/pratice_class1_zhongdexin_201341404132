package com.example.helloworld.api;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/12/13.
 */

public class Service {
    static OkHttpClient client;

    static {
        CookieJar cookieJar = new CookieJar() {
            Map<HttpUrl, List<Cookie>> cookiemap = new HashMap<HttpUrl, List<Cookie>>();

            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                cookiemap.put(httpUrl, list);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                List<Cookie> cookies = cookiemap.get(httpUrl);
                if (cookies == null) {
                    return new ArrayList<Cookie>();
                }else {
                    return cookies;
                }
            }
        };

        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar).build();
    }

    public static OkHttpClient getShareClient() {
        return  client;
    }

    public static Request.Builder requestBuilderWithApi(String api) {
        return new Request.Builder()
                .url("http://172.27.0.51:8080/membercenter/api/" + api);
    }
}
