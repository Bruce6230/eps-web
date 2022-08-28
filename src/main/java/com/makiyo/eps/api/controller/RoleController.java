package com.makiyo.eps.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.makiyo.eps.api.utils.Response;
import com.makiyo.eps.api.controller.form.SearchRoleByIdForm;
import com.makiyo.eps.api.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/role")
@Tag(name = "RoleController", description = "角色Web接口")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/searchAllRole")
    @Operation(summary = "查询所有角色")
    public Response searchAllRole() {
        ArrayList<HashMap> list = roleService.searchAllRole();
        return Response.ok().put("list", list);
    }

    @PostMapping("/searchById")
    @Operation(summary = "根据ID查询角色")
    @SaCheckPermission(value = {"ROOT", "ROLE:SELECT"}, mode = SaMode.OR)
    public Response searchById(@Valid @RequestBody SearchRoleByIdForm form) {
        HashMap map = roleService.searchById(form.getId());
        return Response.ok(map);
    }
}
