package com.lzc.security.validate.code.image;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.lzc.security.validate.code.ValidateCode;
import com.lzc.security.validate.code.ValidateCodeGenerator;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.image.BufferedImage;

public class ImageCodeGenerator implements ValidateCodeGenerator {

    private DefaultKaptcha defaultKaptcha;

    @Override
    public ValidateCode generator(ServletWebRequest request) {
        //生产验证码字符串
        String code = defaultKaptcha.createText();
        //使用生产的验证码字符串返回一个BufferedImage对象
        BufferedImage image = defaultKaptcha.createImage(code);
        return new ImageCode(image, code, 60*5); // 过期时间设置5分钟
    }

    public DefaultKaptcha getDefaultKaptcha() {
        return defaultKaptcha;
    }

    public void setDefaultKaptcha(DefaultKaptcha defaultKaptcha) {
        this.defaultKaptcha = defaultKaptcha;
    }

}
