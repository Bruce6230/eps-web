package com.makiyo.eps.api.service.impl;

import com.makiyo.eps.api.dao.TbRoleDao;
import com.makiyo.eps.api.exception.EpsException;
import com.makiyo.eps.api.pojo.TbRole;
import com.makiyo.eps.api.service.RoleService;
import com.makiyo.eps.api.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private TbRoleDao roleDao;

    @Override
    public ArrayList<HashMap> searchAllRole() {
        ArrayList<HashMap> list = roleDao.searchAllRole();
        return list;
    }

    @Override
    public HashMap searchById(int id) {
        HashMap map = roleDao.searchById(id);
        return map;
    }

    @Override
    public PageUtils searchRoleByPage(HashMap param) {
        ArrayList<HashMap> list = roleDao.searchRoleByPage(param);
        long count = roleDao.searchRoleCount(param);
        int start = (Integer) param.get("start");
        int length = (Integer) param.get("length");
        PageUtils pageUtils = new PageUtils(list, count, start, length);
        return pageUtils;
    }

    @Override
    public int insert(TbRole role) {
        int rows = roleDao.insert(role);
        return rows;
    }

    @Override
    public ArrayList<Integer> searchUserIdByRoleId(int roleId) {
        ArrayList<Integer> list = roleDao.searchUserIdByRoleId(roleId);
        return list;
    }

    @Override
    public int update(TbRole role) {
        int rows = roleDao.update(role);
        return rows;
    }

    @Override
    public int deleteRoleByIds(Integer[] ids) {
        if (!roleDao.searchCanDelete(ids)) {
            throw new EpsException("无法删除关联用户的角色");
        }
        int rows = roleDao.deleteRoleByIds(ids);
        return rows;
    }
}
