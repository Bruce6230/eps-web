package com.makiyo.eps.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.makiyo.eps.api.controller.form.SearchOfflineMeetingByPageForm;
import com.makiyo.eps.api.controller.form.SearchRoomIdByUUIDForm;
import com.makiyo.eps.api.service.MeetingService;
import com.makiyo.eps.api.tencent.TrtcUtil;
import com.makiyo.eps.api.utils.PageUtils;
import com.makiyo.eps.api.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/meeting")
@Tag(name = "MeetingController", description = "会议Web接口")
public class MeetingController {
    @Value("${tencent.trtc.appId}")
    private int appId;

    @Autowired
    private TrtcUtil trtcUtil;

    @Autowired
    private MeetingService meetingService;

    @PostMapping("/searchOfflineMeetingByPage")
    @Operation(summary = "查询线下会议分页数据")
    @SaCheckLogin
    public Response searchOfflineMeetingByPage(@Valid @RequestBody SearchOfflineMeetingByPageForm form) {
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        HashMap param = new HashMap() {{
            put("date", form.getDate());
            put("mold", form.getMold());
            put("userId", StpUtil.getLoginId());
            put("start", start);
            put("length", length);
        }};
        PageUtils pageUtils = meetingService.searchOfflineMeetingByPage(param);
        return Response.ok().put("page", pageUtils);
    }

    @GetMapping("/searchMyUserSig")
    @Operation(summary = "获取用户签名")
    @SaCheckLogin
    public Response searchMyUserSig() {
        int userId = StpUtil.getLoginIdAsInt();
        String userSig = trtcUtil.genUserSig(userId + "");
        return Response.ok().put("userSig", userSig).put("userId", userId).put("appId", appId);
    }

    @PostMapping("/searchRoomIdByUUID")
    @Operation(summary = "查询会议房间RoomID")
    @SaCheckLogin
    public Response searchRoomIdByUUID(@Valid @RequestBody SearchRoomIdByUUIDForm form) {
        Long roomId = meetingService.searchRoomIdByUUID(form.getUuid());
        return Response.ok().put("roomId", roomId);
    }
}