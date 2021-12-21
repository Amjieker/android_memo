package com.example.lin.Tool;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


// 用 okhttp 来访问网络， 封装一下他的东西
public class HttpTools {
    // 发送post 同步
    public synchronized static String SendPost(String _url, String _json) throws IOException {
        OkHttpClient cl = new OkHttpClient();
        System.out.println(_url);
        MediaType mediaType = MediaType.parse("application/json");
        Request req = new Request.Builder()
                .url(_url) // 构造url
                .post(RequestBody.create(mediaType,_json)) // 传输的json数据
                .addHeader("content-type", "application/json") // 头部
                .build();
        Response res = cl.newCall(req).execute();
        if (!res.isSuccessful()) throw new IOException(res.toString());
//        System.out.println(res.body());
//        System.out.println(res.body().string());
        return res.body().string();
    }
}
