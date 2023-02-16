package com.makiyo.eps.api.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.makiyo.eps.api.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {
    @Value("${eps.face.fatigueDetectionUrl}")
    private String fatigueDetectionUrl;

    @Override
    public String fatigueDetection(String data) {
        HttpRequest request = HttpUtil.createPost(fatigueDetectionUrl);
        request.form("image",data);
        HttpResponse response = request.execute();
        String body = response.body();
        return body;
    }
}
