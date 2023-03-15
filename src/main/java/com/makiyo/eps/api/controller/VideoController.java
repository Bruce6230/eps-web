package com.makiyo.eps.api.controller;

import com.makiyo.eps.api.service.VideoService;
import com.makiyo.eps.api.utils.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/video")
@Tag(name = "VideoController", description = "疲劳检测")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @PostMapping("/fatigueDetection")
    public Response fatigueDetection(@RequestBody Map<String,String> map) {
        String image = videoService.fatigueDetection(map.get("photo"));
        Response response = new Response();
        response.put("image",image);
        return response;
    }
}
