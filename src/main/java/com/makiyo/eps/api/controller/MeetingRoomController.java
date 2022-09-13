package com.makiyo.eps.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.json.JSONUtil;
import com.makiyo.eps.api.controller.form.*;
import com.makiyo.eps.api.pojo.TbMeetingRoom;
import com.makiyo.eps.api.service.MeetingRoomService;
import com.makiyo.eps.api.utils.PageUtils;
import com.makiyo.eps.api.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/meeting_room")
@Tag(name = "MeetingRoomController", description = "会议管理Web接口")
public class MeetingRoomController {
    @Autowired
    private MeetingRoomService meetingRoomService;

    @GetMapping("/searchAllMeetingRoom")
    @Operation(summary = "查询所有会议室")
    @SaCheckLogin
    public Response searchAllMeetingRoom() {
        ArrayList<HashMap> list = meetingRoomService.searchAllMeetingRoom();
        return Response.ok().put("list", list);
    }

    @PostMapping("/searchById")
    @Operation(summary = "根据ID查找会议室")
    @SaCheckPermission(value = {"ROOT", "MEETING_ROOM:SELECT"}, mode = SaMode.OR)
    public Response searchById(@Valid @RequestBody SearchMeetingRoomByIdForm form) {
        HashMap map = meetingRoomService.searchById(form.getId());
        return Response.ok(map);
    }

    @PostMapping("/searchFreeMeetingRoom")
    @Operation(summary = "查询空闲会议室")
    @SaCheckLogin
    public Response searchFreeMeetingRoom(@Valid @RequestBody SearchFreeMeetingRoomForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        ArrayList<String> list = meetingRoomService.searchFreeMeetingRoom(param);
        return Response.ok().put("list", list);
    }

    @PostMapping("/searchMeetingRoomByPage")
    @Operation(summary = "查询会议室分页数据")
    @SaCheckLogin
    public Response searchMeetingRoomByPage(@Valid @RequestBody SearchMeetingRoomByPageForm form) {
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("start", start);
        PageUtils pageUtils = meetingRoomService.searchMeetingRoomByPage(param);
        return Response.ok().put("page", pageUtils);
    }

    @PostMapping("/insert")
    @Operation(summary = "添加会议室")
    @SaCheckPermission(value = {"ROOT", "MEETING_ROOM:INSERT"}, mode = SaMode.OR)
    public Response insert(@Valid @RequestBody InsertMeetingRoomForm form) {
        TbMeetingRoom meetingRoom = JSONUtil.parse(form).toBean(TbMeetingRoom.class);
        int rows = meetingRoomService.insert(meetingRoom);
        return Response.ok().put("rows", rows);
    }

    @PostMapping("/update")
    @Operation(summary = "修改会议室")
    @SaCheckPermission(value = {"ROOT", "MEETING_ROOM:UPDATE"}, mode = SaMode.OR)
    public Response update(@Valid @RequestBody UpdateMeetingRoomForm form) {
        TbMeetingRoom meetingRoom = JSONUtil.parse(form).toBean(TbMeetingRoom.class);
        int rows = meetingRoomService.update(meetingRoom);
        return Response.ok().put("rows", rows);
    }

    @PostMapping("/deleteMeetingRoomByIds")
    @Operation(summary = "删除会议室记录")
    @SaCheckPermission(value = {"ROOT", "MEETING_ROOM:DELETE"}, mode = SaMode.OR)
    public Response deleteMeetingRoomByIds(@Valid @RequestBody DeleteMeetingRoomByIdsForm form) {
        int rows = meetingRoomService.deleteMeetingRoomByIds(form.getIds());
        return Response.ok().put("rows", rows);
    }
}