package com.makiyo.eps.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {
    public String fatigueDetection(String data);

    public String microExpressions(MultipartFile videoFile) throws IOException;

    public String fireDetection(String data);
}
