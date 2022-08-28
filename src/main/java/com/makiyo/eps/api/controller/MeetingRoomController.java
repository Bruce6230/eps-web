package com.makiyo.eps.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.json.JSONUtil;
import com.makiyo.eps.api.utils.Response;
import com.makiyo.eps.api.controller.form.SearchFreeMeetingRoomForm;
import com.makiyo.eps.api.controller.form.SearchMeetingRoomByIdForm;
import com.makiyo.eps.api.service.MeetingRoomService;
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
}