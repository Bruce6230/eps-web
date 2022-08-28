package com.makiyo.eps.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.makiyo.eps.api.utils.Response;
import com.makiyo.eps.api.pojo.TbAmectType;
import com.makiyo.eps.api.service.AmectTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/amect_type")
@Tag(name = "AmectTypeController", description = "罚款类型Web接口")
public class AmectTypeController {
    @Autowired
    private AmectTypeService amectTypeService;

    @GetMapping("/searchAllAmectType")
    @Operation(summary = "查询所有罚款类型")
    @SaCheckLogin
    public Response searchAllAmectType() {
        ArrayList<TbAmectType> list = amectTypeService.searchAllAmectType();
        return Response.ok().put("list", list);
    }
}
