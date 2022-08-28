package com.makiyo.eps.api.service;

import java.util.ArrayList;
import java.util.HashMap;

public interface RoleService {
    public ArrayList<HashMap> searchAllRole();
    public HashMap searchById(int id);
}
