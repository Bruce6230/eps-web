package com.makiyo.eps.api.service.impl;

import com.makiyo.eps.api.dao.TbMeetingRoomDao;
import com.makiyo.eps.api.exception.EpsException;
import com.makiyo.eps.api.pojo.TbMeetingRoom;
import com.makiyo.eps.api.service.MeetingRoomService;
import com.makiyo.eps.api.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {
    @Autowired
    private TbMeetingRoomDao meetingRoomDao;

    @Override
    public ArrayList<HashMap> searchAllMeetingRoom() {
        ArrayList<HashMap> list = meetingRoomDao.searchAllMeetingRoom();
        return list;
    }

    @Override
    public HashMap searchById(int id) {
        HashMap map = meetingRoomDao.searchById(id);
        return map;
    }

    @Override
    public ArrayList<String> searchFreeMeetingRoom(HashMap param) {
        ArrayList<String> list = meetingRoomDao.searchFreeMeetingRoom(param);
        return list;
    }

    @Override
    public PageUtils searchMeetingRoomByPage(HashMap param) {
        ArrayList<HashMap> list = meetingRoomDao.searchMeetingRoomByPage(param);
        long count = meetingRoomDao.searchMeetingRoomCount(param);
        int start = (Integer) param.get("start");
        int length = (Integer) param.get("length");
        PageUtils pageUtils = new PageUtils(list, count, start, length);
        return pageUtils;
    }

    @Override
    public int insert(TbMeetingRoom meetingRoom) {
        int rows = meetingRoomDao.insert(meetingRoom);
        return rows;
    }

    @Override
    public int update(TbMeetingRoom meetingRoom) {
        int rows = meetingRoomDao.update(meetingRoom);
        return rows;
    }

    @Override
    public int deleteMeetingRoomByIds(Integer[] ids) {
        if (!meetingRoomDao.searchCanDelete(ids)) {
            throw new EpsException("无法删除关联会议的会议室");
        }
        int rows = meetingRoomDao.deleteMeetingRoomByIds(ids);
        return rows;
    }
}