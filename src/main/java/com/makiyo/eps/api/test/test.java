package com.makiyo.eps.api.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class test {
    public static void main(String[] args) {
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
        // 输出 JSON 字符串
        System.out.println(json);
        //test
    }
}