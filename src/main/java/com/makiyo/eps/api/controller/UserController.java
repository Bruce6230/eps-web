package com.makiyo.eps.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.makiyo.eps.api.controller.form.*;
import com.makiyo.eps.api.pojo.TbUser;
import com.makiyo.eps.api.service.UserService;
import com.makiyo.eps.api.utils.PageUtils;
import com.makiyo.eps.api.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "用户Web接口")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 生成登陆二维码的字符串
     */
    @GetMapping("/createQrCode")
    @Operation(summary = "生成二维码Base64格式的字符串")
    public Response createQrCode() {
        HashMap map = userService.createQrCode();
        return Response.ok(map);
    }

    /**
     * 检测登陆验证码
     *
     * @param form
     * @return
     */
    @PostMapping("/checkQrCode")
    @Operation(summary = "检测登陆验证码")
    public Response checkQrCode(@Valid @RequestBody CheckQrCodeForm form) {
        boolean bool = userService.checkQrCode(form.getCode(), form.getUuid());
        return Response.ok().put("result", bool);
    }

    @PostMapping("/wechatLogin")
    @Operation(summary = "微信小程序登陆")
    public Response wechatLogin(@Valid @RequestBody WechatLoginForm form) {
        HashMap map = userService.wechatLogin(form.getUuid());
        boolean result = (boolean) map.get("result");
        if (result) {
            int userId = (int) map.get("userId");
            StpUtil.setLoginId(userId);
            Set<String> permissions = userService.searchUserPermissions(userId);
            map.remove("userId");
            map.put("permissions", permissions);
        }
        return Response.ok(map);
    }

    /**
     * 登陆成功后加载用户的基本信息
     */
    @GetMapping("/loadUserInfo")
    @Operation(summary = "登陆成功后加载用户的基本信息")
    @SaCheckLogin
    public Response loadUserInfo() {
        int userId = StpUtil.getLoginIdAsInt();
        HashMap summary = userService.searchUserSummary(userId);
        return Response.ok(summary);
    }

    @PostMapping("/searchById")
    @Operation(summary = "根据ID查找用户")
    @SaCheckPermission(value = {"ROOT", "USER:SELECT"}, mode = SaMode.OR)
    public Response searchById(@Valid @RequestBody SearchUserByIdForm form) {
        HashMap map = userService.searchById(form.getUserId());
        return Response.ok(map);
    }

    @GetMapping("/searchAllUser")
    @Operation(summary = "查询所有用户")
    @SaCheckLogin
    public Response searchAllUser() {
        ArrayList<HashMap> list = userService.searchAllUser();
        return Response.ok().put("list", list);
    }

//    @PostMapping("/login")
//    @Operation(summary = "登陆系统")
//    public Response login(@Valid @RequestBody LoginForm form){
//        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
//        Integer userId = userService.login(param);
//        Response response = Response.ok().put("result",userId!=null?true:false);
//        if (userId!=null)
//        {
//            StpUtil.setLoginId(userId);
//            Set<String> permissions=userService.searchUserPermissions(userId);
//            response.put("permissions",permissions);
//        }
//        return response;
//    }
    @PostMapping("/login")
    @Operation(summary = "登陆系统")
    public Response login(@Valid @RequestBody LoginForm form){
        HashMap param= JSONUtil.parse(form).toBean(HashMap.class);
        Integer userId=userService.login(param);
        Response r=Response.ok().put("result",userId!=null?true:false);
        if(userId!=null){
            StpUtil.setLoginId(userId);
            Set<String> permissions=userService.searchUserPermissions(userId);
            r.put("permissions",permissions);
        }
        return r;
    }

    @GetMapping("/logout")
    @Operation(summary = "退出系统")
    public Response logout()
    {
        StpUtil.logout();
        return Response.ok();
    }
    @PostMapping("/updatePassword")
    @SaCheckLogin
    @Operation(summary = "修改密码")
    public Response updatePassword(@Valid @RequestBody UpdatePasswordForm form)
    {
        int userId = StpUtil.getLoginIdAsInt();
        HashMap param = new HashMap();
        param.put("userId",userId);
        param.put("password",form.getPassword());
        int rows = userService.updatePassword(param);
        return Response.ok().put("rows",rows);
    }

    @PostMapping("/searchUserByPage")
    @Operation(summary = "查询用户分页记录")
    @SaCheckPermission(value = {"ROOT", "USER:SELECT"}, mode = SaMode.OR)
    public Response searchUserByPage(@Valid @RequestBody SearchUserByPageForm form) {
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("start", start);
        PageUtils pageUtils = userService.searchUserByPage(param);
        return Response.ok().put("page", pageUtils);
    }

    @PostMapping("/insert")
    @SaCheckPermission(value = {"ROOT","USER:INSERT"},mode = SaMode.OR)
    @Operation(summary = "添加用户")
    public Response insert(@Valid @RequestBody InsertUserForm form){
        TbUser user = JSONUtil.parse(form).toBean(TbUser.class);
        user.setStatus((byte)1);
        user.setRole(JSONUtil.parseArray(form.getRole()).toString());
        user.setCreateTime(new Date());
        int rows = userService.insert(user);
        return Response.ok().put("rows",rows);
    }

    @PostMapping("/update")
    @SaCheckPermission(value = {"ROOT", "USER:UPDATE"}, mode = SaMode.OR)
    @Operation(summary = "用户修改")
    public Response update(@Valid @RequestBody UpdateUserForm form){
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.replace("role",JSONUtil.parseArray(form.getRole()).toString());
        int rows = userService.update(param);
        if(rows==1)
        {
            StpUtil.logoutByLoginId(form.getUserId());
        }
        return Response.ok().put("rows",rows);
    }
    @PostMapping("/deleteUserByIds")
    @SaCheckPermission(value = {"ROOT", "USER:DELETE"}, mode = SaMode.OR)
    @Operation(summary = "删除用户")
    public Response deleteUserById(@Valid @RequestBody DeleteUserByIdForm form)
    {
        Integer userId = StpUtil.getLoginIdAsInt();
        if(ArrayUtil.contains(form.getIds(),userId)){
            return Response.error("您不能删除自己的账户");
        }
        int rows = userService.deleteUserById(form.getIds());
        if (rows > 0) {
            //把被删除的用户踢下线
            for (Integer id : form.getIds()) {
                StpUtil.logoutByLoginId(id);
            }
        }
        return Response.ok().put("rows", rows);
    }
}
