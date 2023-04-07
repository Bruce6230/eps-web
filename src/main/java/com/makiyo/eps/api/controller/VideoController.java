package com.makiyo.eps.api.controller;

import com.makiyo.eps.api.controller.form.ImageForm;
import com.makiyo.eps.api.service.VideoService;
import com.makiyo.eps.api.utils.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/video")
@Tag(name = "VideoController", description = "实时监控")
public class VideoController {
    @Autowired
    private VideoService videoService;

    //疲劳检测测
    @PostMapping("/fatigueDetection")
    public Response fatigueDetection(@RequestBody Map<String,String> map) {
        String image = videoService.fatigueDetection(map.get("photo"));
        Response response = new Response();
        response.put("image",image);
        return response;
    }

    @PostMapping("/microExpressions")
    public Response microExpressions(@RequestParam("videoFile") MultipartFile videoFile) throws IOException {
        Response response = new Response();
        if (videoFile.isEmpty()) {
            return response;
        }
        String result = videoService.microExpressions(videoFile);
        response.put("video",result);
        return response;
    }

    @PostMapping("/fireDetection")
    public Response fireDetection(@RequestBody Map<String,String> map) {
        String result = videoService.fireDetection(map.get("video"));
        Response response = new Response();
        response.put("video",result);
        return response;
    }
    @PostMapping("/test")
    public Response test(@RequestBody ImageForm imageForm)
    {
        String temp = imageForm.getPhoto();
        String base64Data = temp.split(",")[1];
        System.out.println(base64Data);
        Response response = new Response();
        return response;
    }
}
