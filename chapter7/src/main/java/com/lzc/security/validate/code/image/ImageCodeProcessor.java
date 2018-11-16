package com.lzc.security.validate.code.image;

import com.lzc.security.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;

@Component("imageCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {
    private static final String FORMAT_NAME = "JPEG";

    /**
     * 发送图形验证码，将其写到相应中
     *
     * @param request   ServletWebRequest实例对象
     * @param imageCode 验证码
     * @throws Exception 异常
     */
    @Override
    protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
        ImageIO.write(imageCode.getImage(), FORMAT_NAME, request.getResponse().getOutputStream());
    }
}
