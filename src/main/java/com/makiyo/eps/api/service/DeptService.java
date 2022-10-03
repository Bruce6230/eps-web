package com.makiyo.eps.api.service;

import com.makiyo.eps.api.pojo.TbDept;
import com.makiyo.eps.api.utils.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;

public interface DeptService {
    public ArrayList<HashMap> searchAllDept();

    public HashMap searchById(int id);

    public PageUtils searchDeptByPage(HashMap param);

    public int insert(TbDept dept);

    public int deleteDeptByIds(Integer[] ids);

    public int update(TbDept dept);
}
