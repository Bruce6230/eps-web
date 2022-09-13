package com.makiyo.eps.api.service;

import com.makiyo.eps.api.utils.PageUtils;

import java.util.HashMap;

public interface MeetingService {
    public PageUtils searchOfflineMeetingByPage(HashMap param);

    public Long searchRoomIdByUUID(String uuid);
}