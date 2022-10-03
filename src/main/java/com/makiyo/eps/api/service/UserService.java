package com.makiyo.eps.api.service;

import com.makiyo.eps.api.pojo.TbUser;
import com.makiyo.eps.api.utils.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public interface UserService {
    public HashMap createQrCode();

    public boolean checkQrCode(String uuid);

    public HashMap wechatLogin(String uuid);

    public Set<String> searchUserPermissions(int userId);

    public HashMap searchUserSummary(int userId);

    public HashMap searchById(int userId);

    public ArrayList<HashMap> searchAllUser();

    public Integer login(HashMap param);

    public int updatePassword(HashMap param);

    public PageUtils searchUserByPage(HashMap param);

    public int insert(TbUser user);

    public int update(HashMap param);

    public int deleteUserById(Integer[] id);

    public ArrayList<String> searchUserRoles(int userId);

    public HashMap searchNameAndDept(int userId);
}
