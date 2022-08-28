package com.makiyo.eps.api.dao;

import com.makiyo.eps.api.pojo.TbAmectType;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface TbAmectTypeDao {
    public ArrayList<TbAmectType> searchAllAmectType();
}
