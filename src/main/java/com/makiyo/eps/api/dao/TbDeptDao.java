package com.makiyo.eps.api.dao;

import com.makiyo.eps.api.pojo.TbDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbDeptDao {
    public ArrayList<HashMap> searchAllDept();

    public HashMap searchById(int id);

    public ArrayList<HashMap> searchDeptByPage(HashMap param);

    public long searchDeptCount(HashMap param);

    public int insert(TbDept dept);

    public boolean searchCanDelete(Integer[] ids);

    public int deleteDeptByIds(Integer[] ids);

    public int update(TbDept dept);
}