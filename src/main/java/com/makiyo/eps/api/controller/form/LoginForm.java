package com.makiyo.eps.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Schema(description = "登陆表单")
public class LoginForm {
    @Schema(description = "用户名")
    @NotBlank(message = "username不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$",message = "username内容不正确")
    private String username;

    @Schema(description = "密码")
    @NotBlank(message = "password不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$",message = "password内容不正确")
    private String password;
}
