package com.makiyo.eps.api.service.impl;

import com.makiyo.eps.api.dao.TbAmectTypeDao;
import com.makiyo.eps.api.pojo.TbAmectType;
import com.makiyo.eps.api.service.AmectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AmectTypeServiceImpl implements AmectTypeService {
    @Autowired
    private TbAmectTypeDao amectTypeDao;

    @Override
    public ArrayList<TbAmectType> searchAllAmectType() {
        ArrayList<TbAmectType> list = amectTypeDao.searchAllAmectType();
        return list;
    }
}