package com.makiyo.eps.api.service;

import com.makiyo.eps.api.pojo.TbMeeting;
import com.makiyo.eps.api.utils.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;

public interface MeetingService {
    public PageUtils searchOfflineMeetingByPage(HashMap param);

    public Long searchRoomIdByUUID(String uuid);

    public int insert(TbMeeting meeting);

    public int deleteMeetingApplication(HashMap param);

    public ArrayList<HashMap> searchOfflineMeetingInWeek(HashMap param);

    public HashMap searchMeetingInfo(short status, long id);

    public PageUtils searchOnlineMeetingByPage(HashMap param);

    public ArrayList<HashMap> searchOnlineMeetingMembers(HashMap param);

    public boolean searchCanCheckinMeeting(HashMap param);

    public int updateMeetingPresent(HashMap param);
}