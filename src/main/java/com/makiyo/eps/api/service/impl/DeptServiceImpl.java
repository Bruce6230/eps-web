package com.makiyo.eps.api.service.impl;

import com.makiyo.eps.api.dao.TbDeptDao;
import com.makiyo.eps.api.exception.EpsException;
import com.makiyo.eps.api.pojo.TbDept;
import com.makiyo.eps.api.service.DeptService;
import com.makiyo.eps.api.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private TbDeptDao deptDao;

    @Override
    public ArrayList<HashMap> searchAllDept() {
        ArrayList<HashMap> list = deptDao.searchAllDept();
        return list;
    }

    @Override
    public HashMap searchById(int id) {
        HashMap map = deptDao.searchById(id);
        return map;
    }

    @Override
    public PageUtils searchDeptByPage(HashMap param) {
        ArrayList<HashMap> list = deptDao.searchDeptByPage(param);
        long count = deptDao.searchDeptCount(param);
        int start = (Integer) param.get("start");
        int length = (Integer) param.get("length");
        PageUtils pageUtils = new PageUtils(list, count, start, length);

        return pageUtils;
    }

    @Override
    public int insert(TbDept dept) {
        int rows = deptDao.insert(dept);
        return rows;
    }

    @Override
    public int deleteDeptByIds(Integer[] ids) {
        if (!deptDao.searchCanDelete(ids)) {
            throw new EpsException("无法删除关联用户的部门");
        }
        int rows = deptDao.deleteDeptByIds(ids);
        return rows;
    }

    @Override
    public int update(TbDept dept) {
        int rows=deptDao.update(dept);
        return rows;
    }
}
