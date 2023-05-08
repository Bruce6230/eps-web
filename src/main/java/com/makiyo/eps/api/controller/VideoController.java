package com.makiyo.eps.api.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.makiyo.eps.api.controller.form.ImageForm;
import com.makiyo.eps.api.service.VideoService;
import com.makiyo.eps.api.utils.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/video")
@Tag(name = "VideoController", description = "实时监控")
public class VideoController {
    @Autowired
    private VideoService videoService;

    //疲劳检测测
    @PostMapping("/fatigueDetection")
    public Response fatigueDetection(@RequestBody ImageForm imageForm) {
        String temp = imageForm.getPhoto();
        String image = videoService.fatigueDetection(temp);
        Response response = new Response();
        response.put("photo",image);
        return response;
    }

    @PostMapping("/microExpressions")
    public Response microExpressions(@RequestBody ImageForm imageForm) throws IOException {
        String temp = imageForm.getPhoto();
        String image = videoService.microExpressions(temp);
        Response response = new Response();
        response.put("photo",image);
        return response;
    }

    @PostMapping("/fireDetection")
    public Response fireDetection(@RequestBody ImageForm imageForm) {
        String temp = imageForm.getPhoto();
        String image = videoService.fireDetection(temp);
        Response response = new Response();
        response.put("photo",image);
        return response;
    }
    @PostMapping("/test")
    public Response test(@RequestBody ImageForm imageForm)
    {
        String temp = imageForm.getPhoto();
        String base64Data = temp.split(",")[1];
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
        HttpResponse response = HttpRequest.post("http://192.168.8.128:5000/flask/postTask")
                .header("Content-Type", "application/json")
                .body(json)
                .execute();

        // 获取响应结果
        String result = response.body();
        Response response1 = new Response();
        response1.put("key",result);
        JSONObject jsonObject1 = JSON.parseObject(result);
        String id = jsonObject1.getString("id");
//        HttpResponse httpResponse = HttpUtil.createPost("http://192.168.8.128:5000/flask/postTask")
//            .header("Content-Type", "application/json")
//            .body(json)
//            .execute();
        JSONObject jsonobject = new JSONObject();
//        String id = jsonobject.getString("id");
//        //用来请求处理后图片
//        JSONObject object = new JSONObject();
        jsonobject.put("id",id);
//        //截取图片
        jsonobject.put("frame",base64Data);
//        //发送请求
//        System.out.println(jsonobject);
        HttpResponse httpResponse = HttpUtil.createPost("http://192.168.8.128:5000/flask/postFrame")
                .header("Content-Type", "application/json")
                .body(jsonobject.toJSONString())
                .execute();
        JSONObject parseObject = JSON.parseObject(httpResponse.body());
        response1.put("image",parseObject.get("frame"));
//        return result.getString("frame");
        return response1;
    }
}
