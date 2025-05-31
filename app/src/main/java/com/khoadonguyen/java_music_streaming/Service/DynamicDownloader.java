package com.khoadonguyen.java_music_streaming.Service;

import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.downloader.Request;
import org.schabi.newpipe.extractor.downloader.Response;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


/***
 * cấu hình động Downloader vì newpipedextractor không tự cấu hình http được
 */
public class DynamicDownloader extends Downloader {
    @Override
    public Response execute(Request request) throws IOException, ReCaptchaException {
        OkHttpClient okHttpClient = new OkHttpClient();

        String url = request.url();


        String method = request.httpMethod();
        Map<String, List<String>> headersMap = request.headers();

        byte[] body = request.dataToSend();

        Headers.Builder builder = new Headers.Builder();
        for (Map.Entry<String, List<String>> entry : headersMap.entrySet()) {
            String name = entry.getKey();
            List<String> values = entry.getValue();
            if (name != null && values != null) {
                for (String value : values) {
                    builder.add(name, value);
                }
            }
        }
        Headers headers = builder.build();

        RequestBody requestBody = null;
        if (body != null && body.length > 0) {
            requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), body);
        }
        if (method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("HEAD")) {
            requestBody = null;
        }

        okhttp3.Request okRequest = new okhttp3.Request.Builder()
                .url(url)
                .headers(headers)
                .method(method, requestBody)
                .build();

        okhttp3.Response okHttpResponse = okHttpClient.newCall(okRequest).execute();

        return new Response(
                okHttpResponse.code(),
                okHttpResponse.message(),
                okHttpResponse.headers().toMultimap(),
                okHttpResponse.body().string(),
                request.url());
    }


}
