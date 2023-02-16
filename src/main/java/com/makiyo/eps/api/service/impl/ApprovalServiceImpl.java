package com.makiyo.eps.api.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.makiyo.eps.api.exception.EpsException;
import com.makiyo.eps.api.service.ApprovalService;
import com.makiyo.eps.api.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class ApprovalServiceImpl implements ApprovalService {
    @Value("${workflow.url}")
    private String workflow;

    @Override
    public PageUtils searchTaskByPage(HashMap param) {
        String url = workflow + "/workflow/searchTaskByPage";
        HttpResponse resp = HttpRequest.post(url).header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(param)).execute();
        if(resp.getStatus()==200){
            JSONObject json = JSONUtil.parseObj(resp.body());
            JSONObject page = json.getJSONObject("page");
            ArrayList list = page.get("list", ArrayList.class);
            Long totalCount = page.getLong("totalCount");
            Integer pageIndex = page.getInt("pageIndex");
            Integer pageSize = page.getInt("pageSize");
            PageUtils pageUtils = new PageUtils(list, totalCount, pageIndex, pageSize);
            return pageUtils;
        }else{
            log.error(resp.body());
            throw new EpsException("获取工作流数据异常");
        }
    }

    @Override
    public HashMap searchApprovalContent(HashMap param) {
        String url=workflow+"/workflow/searchApprovalContent";
        HttpResponse resp=HttpRequest.post(url).header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(param)).execute();
        if(resp.getStatus()==200){
            JSONObject json=JSONUtil.parseObj(resp.body());
            HashMap content=json.get("content",HashMap.class);
            return content;
        }else{
            log.error(resp.body());
            throw new EpsException("获取工作流数据异常");
        }
    }

    @Override
    public void approvalTask(HashMap param) {
        String url = workflow + "/workflow/approvalTask";
        HttpResponse resp=HttpRequest.post(url).header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(param)).execute();
        if(resp.getStatus()!=200){
            log.error(resp.body());
            throw new EpsException("调用工作流审批异常");
        }
    }

    @Override
    public void archiveTask(HashMap param) {
        String url = workflow + "/workflow/archiveTask";
        HttpResponse resp=HttpRequest.post(url).header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(param)).execute();
        if(resp.getStatus()!=200){
            log.error(resp.body());
            throw new EpsException("调用工作流审批异常");
        }
    }

}
