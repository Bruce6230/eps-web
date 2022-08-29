package com.makiyo.eps.api.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbMeetingDao {
    public boolean searchMeetingMembersInSameDept(String uuid);

    public HashMap searchMeetingById(HashMap param);

    public ArrayList<HashMap> searchOfflineMeetingByPage(HashMap param);

    public long searchOfflineMeetingCount(HashMap param);
}