package com.makiyo.eps.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.makiyo.eps.api.controller.form.ApprovalTaskForm;
import com.makiyo.eps.api.controller.form.ArchiveTaskForm;
import com.makiyo.eps.api.controller.form.SearchApprovalContentForm;
import com.makiyo.eps.api.controller.form.SearchTaskByPageForm;
import com.makiyo.eps.api.exception.EpsException;
import com.makiyo.eps.api.service.ApprovalService;
import com.makiyo.eps.api.service.UserService;
import com.makiyo.eps.api.utils.PageUtils;
import com.makiyo.eps.api.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

@RestController
@RequestMapping("/approval")
@Tag(name = "ApprovalController", description = "任务审批Web接口")
@Slf4j
public class ApprovalController {
    @Value("${workflow.url}")
    private String workflow;

    @Autowired
    private UserService userService;

    @Autowired
    private ApprovalService approvalService;

    @PostMapping("/searchTaskByPage")
    @Operation(summary = "查询分页任务列表")
    @SaCheckPermission(value = {"WORKFLOW:APPROVAL", "FILE:ARCHIVE"}, mode = SaMode.OR)
    public Response searchTaskByPage(@Valid @RequestBody SearchTaskByPageForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        int userId = StpUtil.getLoginIdAsInt();
        param.put("userId", userId);
        param.put("role", userService.searchUserRoles(userId));
        PageUtils pageUtils = approvalService.searchTaskByPage(param);
        return Response.ok().put("page", pageUtils);
    }

    @PostMapping("/searchApprovalContent")
    @Operation(summary = "查询任务详情")
    @SaCheckPermission(value = {"WORKFLOW:APPROVAL", "FILE:ARCHIVE"}, mode = SaMode.OR)
    public Response searchApprovalContent(@Valid @RequestBody SearchApprovalContentForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        int userId = StpUtil.getLoginIdAsInt();
        param.put("userId", userId);
        param.put("role", userService.searchUserRoles(userId));
        HashMap content = approvalService.searchApprovalContent(param);
        return Response.ok().put("content", content);
    }

    @GetMapping("/searchApprovalBpmn")
    @Operation(summary = "获取BPMN图形")
    @SaCheckPermission(value = {"WORKFLOW:APPROVAL", "FILE:ARCHIVE"}, mode = SaMode.OR)
    public void searchApprovalBpmn(String instanceId, HttpServletResponse response) {
        if (StrUtil.isBlankIfStr(instanceId)) {
            throw new EpsException("instanceId不能为空");
        }
        HashMap param = new HashMap() {{
            put("instanceId", instanceId);
        }};
        String url = workflow + "/workflow/searchApprovalBpmn";
        HttpResponse resp = HttpRequest.post(url).header("content-type", "application/json")
                .body(JSONUtil.toJsonStr(param)).execute();
        if (resp.getStatus() == 200) {
            try (
                    InputStream in = resp.bodyStream();
                    BufferedInputStream bin = new BufferedInputStream(in);
                    OutputStream out = response.getOutputStream();
                    BufferedOutputStream bout = new BufferedOutputStream(out);
            ) {
                IoUtil.copy(bin,bout);
            } catch (Exception e) {
                log.error("执行异常", e);
            }
        } else {
            log.error("获取工作流BPMN失败");
            throw new EpsException("获取工作流BPMN失败");
        }
    }

    @PostMapping("/approvalTask")
    @Operation(summary = "审批任务")
    @SaCheckPermission(value = {"WORKFLOW:APPROVAL"}, mode = SaMode.OR)
    public Response approvalTask(@Valid @RequestBody ApprovalTaskForm form){
        HashMap param=JSONUtil.parse(form).toBean(HashMap.class);
        approvalService.approvalTask(param);
        return Response.ok();
    }

    @PostMapping("/archiveTask")
    @Operation(summary = "归档任务")
    @SaCheckPermission(value = {"FILE:ARCHIVE"})
    public Response archiveTask(@Valid @RequestBody ArchiveTaskForm form){
        if(!JSONUtil.isJsonArray(form.getFiles())){
            return Response.error("files不是JSON数组");
        }
        HashMap param=new HashMap(){{
            put("taskId",form.getTaskId());
            put("files",form.getFiles());
            put("userId",StpUtil.getLoginIdAsInt());
        }};
        approvalService.archiveTask(param);
        return Response.ok();
    }
}
