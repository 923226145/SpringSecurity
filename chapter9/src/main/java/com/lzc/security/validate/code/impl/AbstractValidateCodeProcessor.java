package com.lzc.security.validate.code.impl;

import com.lzc.security.validate.code.ValidateCode;
import com.lzc.security.validate.code.ValidateCodeGenerator;
import com.lzc.security.validate.code.ValidateCodeProcessor;
import com.lzc.security.validate.code.sms.SmsCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    private static final String SEPARATOR = "/code/";

    /**
     * 这是Spring的一个特性，就是在项目启动的时候会自动收集系统中 {@link ValidateCodeGenerator} 接口的实现类对象
     */
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGeneratorMap;

    @Override
    public void create(ServletWebRequest request) throws Exception {
        C validateCode = generate(request);
        save(request, validateCode);
        send(request, validateCode);
    }

    /**
     * 生成验证码
     *
     * @param request ServletWebRequest实例对象
     * @return 验证码实例对象
     */
    @SuppressWarnings("unchecked")
    private C generate(ServletWebRequest request) {
        String type = getProcessorType(request);
        ValidateCodeGenerator validateCodeGenerator = validateCodeGeneratorMap.get(type.concat(ValidateCodeGenerator.CODE_GENERATOR));
        return (C) validateCodeGenerator.generator(request);
    }

    /**
     * 保存验证码到session中
     *
     * @param request      ServletWebRequest实例对象
     * @param validateCode 验证码
     */
    private void save(ServletWebRequest request, C validateCode) {
        if (validateCode instanceof SmsCode) {
            request.getRequest().getSession().setAttribute(SESSION_KEY_PREFIX.concat(getProcessorType(request).toUpperCase()), validateCode);
        } else {
            ValidateCode c = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
            request.getRequest().getSession().setAttribute(SESSION_KEY_PREFIX.concat(getProcessorType(request).toUpperCase()), c);
        }
    }

    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

    /**
     * 获取请求URL中具体请求的验证码类型
     *
     * @param request ServletWebRequest实例对象
     * @return 验证码类型
     */
    private String getProcessorType(ServletWebRequest request) {
        // 获取URI分割后的第二个片段
        return StringUtils.substringAfter(request.getRequest().getRequestURI(), SEPARATOR);
    }
}
