package com.makiyo.eps.api.service;

import com.makiyo.eps.api.pojo.TbRole;
import com.makiyo.eps.api.utils.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;

public interface RoleService {
    public ArrayList<HashMap> searchAllRole();

    public HashMap searchById(int id);

    public PageUtils searchRoleByPage(HashMap param);

    public int insert(TbRole role);

    public ArrayList<Integer> searchUserIdByRoleId(int roleId);

    public int update(TbRole role);

    public int deleteRoleByIds(Integer[] ids);
}
