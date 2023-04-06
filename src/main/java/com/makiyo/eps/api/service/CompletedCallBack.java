package com.makiyo.eps.api.service;

import com.makiyo.eps.api.service.dto.Message;

public interface CompletedCallBack {

    /**
     * 完成回掉
     *
     * @param questions
     * @param sessionId
     * @param response
     */
    void completed(Message questions, String sessionId, String response);

    void fail(String sessionId);

}
