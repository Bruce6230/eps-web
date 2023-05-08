package com.makiyo.eps.api.service;

import java.io.IOException;

public interface VideoService {
    public String fatigueDetection(String data);

    public String microExpressions(String data) throws IOException;

    public String fireDetection(String data);
}
