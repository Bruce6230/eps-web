package com.makiyo.eps.api.controller.form;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "图片")
@Data
public class ImageForm {

    private String photo;
}
