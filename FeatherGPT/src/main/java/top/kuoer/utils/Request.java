package top.kuoer.utils;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import top.kuoer.parameter.GPTSetting;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    private static Request request;
    private OkHttpClient client;
    private MediaType mediaType;


    private Request() {
        this.client = new OkHttpClient().newBuilder().build();
        this.mediaType = MediaType.parse("application/json");
    }

    public static Request getInstance() {
        if(null == request) {
            request = new Request();
        }
        return request;
    }

    public String simpleRequest(GPTSetting setting) throws IOException {
        okhttp3.Response response = this.request(setting);
        if(null == response.body()) {
            return "";
        }
        return response.body().string();
    }

    public InputStream streamRequest(GPTSetting setting) throws IOException {
        okhttp3.Response response = this.request(setting);
        if(null == response.body()) {
            return null;
        }
        return response.body().byteStream();
    }


    private okhttp3.Response request(GPTSetting setting) throws IOException {
        Gson gson = new Gson();
        RequestBody body = RequestBody.create(this.mediaType, gson.toJson(setting));
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(setting.getApiUrl())
                .method("POST", body)
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", setting.getKey())
                .build();

        return client.newCall(request).execute();
    }


}
