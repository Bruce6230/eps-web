package com.makiyo.eps.api.task;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.makiyo.eps.api.dao.TbMeetingDao;
import com.makiyo.eps.api.dao.TbUserDao;
import com.makiyo.eps.api.exception.EpsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class MeetingWorkflowTask {

    @Autowired
    private TbUserDao userDao;

    @Autowired
    private TbMeetingDao meetingDao;

    @Value("${eps.recieveNotify}")
    private String recieveNotify;

    @Value("${workflow.url}")
    private String workflow;

    @Async("AsyncTaskExecutor")
    public void startMeetingWorkflow(String uuid, int creatorId,String title, String date, String start,String meetingType) {
        //查询申请人基本信息
        HashMap info = userDao.searchUserInfo(creatorId);

        JSONObject json = new JSONObject();
        json.set("url", recieveNotify);
        json.set("uuid", uuid);
        json.set("creatorId",creatorId);
        json.set("creatorName",info.get("name").toString());
        json.set("title",title);
        json.set("date", date);
        json.set("start", start);
        json.set("meetingType",meetingType);

        String[] roles = info.get("roles").toString().split("，");
        //判断用户角色是不是总经理，总经理创建的会议不需要审批，所以不需要查询总经理userId和部门经理userId
        if (!ArrayUtil.contains(roles, "总经理")) {
            //查询部门经理userId
            Integer managerId = userDao.searchDeptManagerId(creatorId);
            json.set("managerId", managerId);

            //查询总经理userId
            Integer gmId = userDao.searchGmId();
            json.set("gmId", gmId);

            //查询参会人是否为同一个部门
            boolean bool = meetingDao.searchMeetingMembersInSameDept(uuid);
            json.set("sameDept", bool);
        }

        String url = workflow + "/workflow/startMeetingProcess";
        HttpResponse resp = HttpRequest.post(url).header("Content-Type", "application/json")
                .body(json.toString()).execute();
        if (resp.getStatus() == 200) {
            json = JSONUtil.parseObj(resp.body());
            String instanceId = json.getStr("instanceId");
            HashMap param = new HashMap();
            param.put("uuid", uuid);
            param.put("instanceId", instanceId);
            //更新会议记录的instance_id字段
            int row = meetingDao.updateMeetingInstanceId(param);
            if (row != 1) {
                throw new EpsException("保存会议工作流实例ID失败");
            }
        } else {
            log.error(resp.body());
        }
    }

    @Async("AsyncTaskExecutor")
    public void deleteMeetingApplication(String uuid, String instanceId, String reason) {
        JSONObject json = new JSONObject();
        json.set("uuid", uuid);
        json.set("instanceId", instanceId);
        json.set("type", "会议申请");
        json.set("reason", reason);
        String url = workflow + "/workflow/deleteProcessById";
        HttpResponse resp = HttpRequest.post(url).header("Content-Type", "application/json")
                .body(json.toString()).execute();
        if (resp.getStatus() == 200) {
            log.debug("删除了会议申请");
        }
        else{
            log.error(resp.body());
        }
    }
}