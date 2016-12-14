package com.example.helloworld.api;


import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/12/13.
 */

public class Service {
    static OkHttpClient client;
    public static String serverAddress = "http://172.27.0.51:8080/membercenter/";

    static {
//        CookieJar cookieJar = new CookieJar() {
//            Map<HttpUrl, List<Cookie>> cookiemap = new HashMap<HttpUrl, List<Cookie>>();
//
//            @Override
//            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
//                cookiemap.put(httpUrl, list);
//            }
//
//            @Override
//            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
//                List<Cookie> cookies = cookiemap.get(httpUrl);
//                if (cookies == null) {
//                    return new ArrayList<Cookie>();
//                }else {
//                    return cookies;
//                }
//            }
//        };

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        client = new OkHttpClient.Builder()
//                .cookieJar(cookieJar).build();
        .cookieJar(new JavaNetCookieJar(cookieManager)).build();
    }

    public static OkHttpClient getShareClient() {
        return  client;
    }

    public static Request.Builder requestBuilderWithApi(String api) {
        return new Request.Builder()
                .url(serverAddress + "api/" + api);
    }
}
