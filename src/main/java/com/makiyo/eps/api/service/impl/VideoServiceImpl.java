package com.makiyo.eps.api.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.makiyo.eps.api.service.VideoService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class VideoServiceImpl implements VideoService {
    @Value("${eps.face.FlaskTaskUrl}")
    private String flaskTaskUrl;

    @Value("${eps.face.FlaskFrameUrl}")
    private String flaskFrameUrl;

    @Override
    public String fatigueDetection(String data) {
        HttpRequest request = HttpUtil.createPost(flaskTaskUrl);
        // 创建一个 JSON 对象
        JSONObject jsonObject = new JSONObject();
        // 创建并添加 model 列表
        JSONObject modelObject = new JSONObject();
        modelObject.put("type", "jailbreakDetect");
        modelObject.put("confidenceLevel", 0.2);
        // 创建并添加 polygons 数组
        double[][][] polygons = new double[][][]{{{0, 100}, {0, 600}, {500, 500}, {200, 0}}, {{1000, 500}, {700, 200}, {500, 300}, {200, 700}}};        JSONArray polygonsArray = new JSONArray();
        for (double[][] polygon : polygons) {
            JSONArray polygonArray = new JSONArray();
            for (double[] point : polygon) {
                JSONArray pointArray = new JSONArray();
                pointArray.add(point[0]);
                pointArray.add(point[1]);
                polygonArray.add(pointArray);
            }
            polygonsArray.add(polygonArray);
        }
        modelObject.put("polygons", polygonsArray);
        JSONArray modelsArray = new JSONArray();
        modelsArray.add(modelObject);
        // 添加 models 数组到 JSON 对象
        jsonObject.put("models", modelsArray);
        // 将 JSON 对象转换为 JSON 字符串
        String json = jsonObject.toJSONString();
        request.body(json);
        HttpResponse response = request.execute();
        JSONObject jsonobject = JSONObject.parseObject(response.body());
        String id = jsonobject.getString("id");
        //用来请求处理后图片
        HttpRequest httpRequest = HttpUtil.createPost(flaskFrameUrl);
        JSONObject object = new JSONObject();
        object.put("id",id);
        //获取图片
        object.put("frame",data);

    }

    @Override
    public String microExpressions(MultipartFile videoFile) throws IOException {
        //接收均为base64格式
        byte[] fileContent = videoFile.getBytes();
        String video = Base64.encodeBase64String(fileContent);
        HttpRequest request = HttpUtil.createPost(flaskTaskUrl);
        request.form("videoFile",videoFile);
        HttpResponse response = request.execute();
        String body = response.body();
        return body;
    }

    @Override
    public String fireDetection(String data){
        HttpRequest request = HttpUtil.createPost(flaskTaskUrl);
        request.form("video",data);
        HttpResponse response = request.execute();
        String body = response.body();
        return body;
    }
}
