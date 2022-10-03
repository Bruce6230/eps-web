package com.makiyo.eps.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.makiyo.eps.api.controller.form.*;
import com.makiyo.eps.api.pojo.TbMeeting;
import com.makiyo.eps.api.service.MeetingService;
import com.makiyo.eps.api.tencent.TrtcUtil;
import com.makiyo.eps.api.utils.PageUtils;
import com.makiyo.eps.api.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/meeting")
@Tag(name = "MeetingController", description = "会议Web接口")
@Slf4j
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

    @PostMapping("/insert")
    @Operation(summary = "添加会议")
    @SaCheckLogin
    public Response insert(@Valid @RequestBody InsertMeetingForm form) {
        DateTime start = DateUtil.parse(form.getDate() + " " + form.getStart());
        DateTime end = DateUtil.parse(form.getDate() + " " + form.getEnd());
        if (start.isAfterOrEquals(end)) {
            return Response.error("结束时间必须大于开始时间");
        } else if (new DateTime().isAfterOrEquals(start)) {
            return Response.error("会议开始时间不能早于当前时间");
        }
        TbMeeting meeting = JSONUtil.parse(form).toBean(TbMeeting.class);
        meeting.setUuid(UUID.randomUUID().toString(true));
        meeting.setCreatorId(StpUtil.getLoginIdAsInt());
        meeting.setStatus((short) 1);
        int rows = meetingService.insert(meeting);
        return Response.ok().put("rows", rows);
    }

    @PostMapping("/recieveNotify")
    @Operation(summary = "接收工作流通知")
    public Response recieveNotify(@Valid @RequestBody RecieveNotifyForm form){
        if(form.getResult().equals("同意")){
            log.debug(form.getUuid()+"的会议审批通过");
        }
        else{
            log.debug(form.getUuid()+"的会议审批不通过");
        }
        return Response.ok();
    }

    @PostMapping("/searchOfflineMeetingInWeek")
    @Operation(summary = "查询某个会议室的一周会议")
    @SaCheckLogin
    public Response searchOfflineMeetingInWeek(@Valid @RequestBody SearchOfflineMeetingInWeekForm form) {
        String date = form.getDate();
        DateTime startDate, endDate;
        if (date != null && date.length() > 0) {
            //从date开始，生成七天日期
            startDate = DateUtil.parseDate(date);
            endDate = startDate.offsetNew(DateField.DAY_OF_WEEK, 6);

        } else {
            //查询当前日期，生成本周的日期
            startDate = DateUtil.beginOfWeek(new Date());
            endDate = DateUtil.endOfWeek(new Date());
        }
        HashMap param = new HashMap() {{
            put("place", form.getName());
            put("startDate", startDate.toDateStr());
            put("endDate", endDate.toDateStr());
            put("mold", form.getMold());
            put("userId", StpUtil.getLoginIdAsLong());
        }};
        ArrayList list = meetingService.searchOfflineMeetingInWeek(param);

        //生成周日历水平表头的文字标题
        DateRange range = DateUtil.range(startDate, endDate, DateField.DAY_OF_WEEK);
        ArrayList days = new ArrayList();
        range.forEach(one -> {
            JSONObject json = new JSONObject();
            json.set("date", one.toString("MM/dd"));
            json.set("day", one.dayOfWeekEnum().toChinese("周"));
            days.add(json);
        });

        return Response.ok().put("list", list).put("days", days);
    }

    @PostMapping("/searchMeetingInfo")
    @Operation(summary = "查询会议信息")
    @SaCheckLogin
    public Response searchMeetingInfo(@Valid @RequestBody SearchMeetingInfoForm form) {
        HashMap map = meetingService.searchMeetingInfo(form.getStatus(), form.getId());
        return Response.ok(map);
    }

    @PostMapping("/deleteMeetingApplication")
    @Operation(summary = "删除会议申请")
    @SaCheckLogin
    public Response deleteMeetingApplication(@Valid @RequestBody DeleteMeetingApplicationForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("creatorId", StpUtil.getLoginIdAsLong());
        param.put("userId", StpUtil.getLoginIdAsLong());
        int rows = meetingService.deleteMeetingApplication(param);
        return Response.ok().put("rows", rows);
    }

    @PostMapping("/searchOnlineMeetingByPage")
    @Operation(summary = "查询线上会议分页数据")
    @SaCheckLogin
    public Response searchOnlineMeetingByPage(@Valid @RequestBody SearchOnlineMeetingByPageForm form) {
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
        PageUtils pageUtils = meetingService.searchOnlineMeetingByPage(param);
        return Response.ok().put("page", pageUtils);
    }

    @PostMapping("/updateMeetingPresent")
    @Operation(summary = "执行会议签到")
    @SaCheckLogin
    public Response updateMeetingPresent(@Valid @RequestBody UpdateMeetingPresentForm form) {
        HashMap param = new HashMap() {{
            put("meetingId", form.getMeetingId());
            put("userId", StpUtil.getLoginIdAsInt());
        }};
        boolean bool = meetingService.searchCanCheckinMeeting(param);
        if (bool) {
            int rows = meetingService.updateMeetingPresent(param);
            return Response.ok().put("rows", rows);
        }
        return Response.ok().put("rows", 0);
    }

    @PostMapping("/searchOnlineMeetingMembers")
    @Operation(summary = "查询线下会议成员")
    @SaCheckLogin
    public Response searchOnlineMeetingMembers(@Valid @RequestBody SearchOnlineMeetingMembersForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("userId", StpUtil.getLoginIdAsInt());
        ArrayList<HashMap> list = meetingService.searchOnlineMeetingMembers(param);
        return Response.ok().put("list", list);
    }
}