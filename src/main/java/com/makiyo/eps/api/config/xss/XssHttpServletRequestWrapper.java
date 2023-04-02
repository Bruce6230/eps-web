package com.makiyo.eps.api.config.xss;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private ByteArrayInputStream byteArrayInputStream;

    public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        prepareInputStream(request.getInputStream());
    }

    private void prepareInputStream(ServletInputStream inputStream) throws IOException {
        InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader buffer = new BufferedReader(reader);
        StringBuilder body = new StringBuilder();
        String line = buffer.readLine();
        while (line != null) {
            body.append(line);
            line = buffer.readLine();
        }
        buffer.close();
        reader.close();

        Map<String, Object> map = JSONUtil.parseObj(body.toString());
        Map<String, Object> result = new LinkedHashMap<>();
        for (String key : map.keySet()) {
            Object val = map.get(key);
            if (val instanceof String) {
                if (!StrUtil.hasEmpty(val.toString())) {
                    result.put(key, HtmlUtil.cleanHtmlTag(val.toString()));
                }
            } else {
                result.put(key, val);
            }
        }
        String json = JSONUtil.toJsonStr(result);
        byteArrayInputStream = new ByteArrayInputStream(json.getBytes());
    }

    // ... 其他代码

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new DelegatingServletInputStream(byteArrayInputStream);
    }

    private static class DelegatingServletInputStream extends ServletInputStream {

        private final InputStream inputStream;

        public DelegatingServletInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public boolean isFinished() {
            try {
                return inputStream.available() == 0;
            } catch (IOException e) {
                return true;
            }
        }

        @Override
        public boolean isReady() {
            try {
                return inputStream.available() > 0;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            // 异步支持
            try {
                while (!isFinished()) {
                    readListener.onDataAvailable();
                }
                readListener.onAllDataRead();
            } catch (IOException e) {
                readListener.onError(e);
            }
        }
    }
}