package onionsss.it.onionsss.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 作者：张琦 on 2016/5/17 18:12
 */
public class OkUtils {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static Response getResponse(String url) throws IOException {
        /**
         * 设置三秒连接延迟
         */
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        // 2.请求体
        Request request = new Request.Builder()
                .get()//get请求方式
                .url(url)//网址
                .build();

        // 3.执行okhttp
        Response response = null;
        response = okHttpClient.newCall(request).execute();
//        System.out.println(response.body().string());


        return response;
    }

    /**
     * 网络请求验证帐号密码
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static Response postResponse(String url, String json) throws IOException {
        /**
         * 设置三秒连接延迟
         */
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        // 2.请求体
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .post(body)//post请求方式
                .url(url)//网址
                .build();

        // 3.执行okhttp
        Response response = null;
        response = okHttpClient.newCall(request).execute();
//        System.out.println(response.body().string());

        return response;
    }
}
