package com.makiyo.eps.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "检验登陆验证码表单")
public class CheckQrCodeForm {
    @NotBlank(message = "uuid不能为空")
    @Schema(description = "uuid")
    private String uuid;
}