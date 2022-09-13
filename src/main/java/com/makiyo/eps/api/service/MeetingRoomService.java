package com.makiyo.eps.api.service;

import com.makiyo.eps.api.pojo.TbMeetingRoom;
import com.makiyo.eps.api.utils.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;

public interface MeetingRoomService {
    public ArrayList<HashMap> searchAllMeetingRoom();

    public HashMap searchById(int id);

    public ArrayList<String> searchFreeMeetingRoom(HashMap param);

    public PageUtils searchMeetingRoomByPage(HashMap param);

    public int insert(TbMeetingRoom meetingRoom);

    public int update(TbMeetingRoom meetingRoom);

    public int deleteMeetingRoomByIds(Integer[] ids);
}
