package com.makiyo.eps.api.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.makiyo.eps.api.service.VideoService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class VideoServiceImpl implements VideoService {
    @Value("${eps.face.fatigueDetectionUrl}")
    private String fatigueDetectionUrl;

    @Value("${eps.face.microExpressionsUrl}")
    private String microExpressionsUrl;

    @Value("${eps.face.fireDetectionUrl}")
    private String fireDetectionUrl;

    @Override
    public String fatigueDetection(String data) {
        HttpRequest request = HttpUtil.createPost(fatigueDetectionUrl);
        request.form("image",data);
        HttpResponse response = request.execute();
        String body = response.body();
        return body;
    }

    @Override
    public String microExpressions(MultipartFile videoFile) throws IOException {
        //接收均为base64格式
        byte[] fileContent = videoFile.getBytes();
        String video = Base64.encodeBase64String(fileContent);
        HttpRequest request = HttpUtil.createPost(microExpressionsUrl);
        request.form("videoFile",videoFile);
        HttpResponse response = request.execute();
        String body = response.body();
        return body;
    }

    @Override
    public String fireDetection(String data){
        HttpRequest request = HttpUtil.createPost(fireDetectionUrl);
        request.form("video",data);
        HttpResponse response = request.execute();
        String body = response.body();
        return body;
    }
}
