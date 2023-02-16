package com.makiyo.eps.api.service;

import com.makiyo.eps.api.utils.PageUtils;

import java.util.HashMap;

public interface ApprovalService {
    public PageUtils searchTaskByPage(HashMap param);

    public HashMap searchApprovalContent(HashMap param);

    public void approvalTask(HashMap param);

    public void archiveTask(HashMap param);
}