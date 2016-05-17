package onionsss.it.onionsss.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者：张琦 on 2016/5/17 18:12
 */
public class OkUtils {
    public static Response getResponse(String url) throws IOException {
        OkHttpClient okHttpClient_get = new OkHttpClient();
        // 2.请求体
        Request request = new Request.Builder()
                .get()//get请求方式
                .url(url)//网址
                .build();

        // 3.执行okhttp
        Response response = null;
        response = okHttpClient_get.newCall(request).execute();
//        System.out.println(response.body().string());


        return response;
    }
}
