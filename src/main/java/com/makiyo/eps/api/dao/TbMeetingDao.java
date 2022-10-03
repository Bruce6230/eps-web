package com.makiyo.eps.api.dao;

import com.makiyo.eps.api.pojo.TbMeeting;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbMeetingDao {
    public boolean searchMeetingMembersInSameDept(String uuid);

    public HashMap searchMeetingById(HashMap param);

    public ArrayList<HashMap> searchOfflineMeetingByPage(HashMap param);

    public long searchOfflineMeetingCount(HashMap param);

    public int updateMeetingInstanceId(HashMap param);

    public int insert(TbMeeting meeting);

    public int deleteMeetingApplication(HashMap param);

    public ArrayList<HashMap> searchOfflineMeetingInWeek(HashMap param);

    public HashMap searchMeetingInfo(long id);

    public HashMap searchCurrentMeetingInfo(long id);

    public ArrayList<HashMap> searchOnlineMeetingByPage(HashMap param);

    public long searchOnlineMeetingCount(HashMap param);

    public ArrayList<HashMap> searchOnlineMeetingMembers(HashMap param);

    public long searchCanCheckinMeeting(HashMap param);

    public int updateMeetingPresent(HashMap param);
}