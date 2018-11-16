package com.lzc.security.validate.code;

import org.springframework.web.context.request.ServletWebRequest;


public interface ValidateCodeGenerator {
    public final String CODE_GENERATOR = "CodeGenerator";
    ValidateCode generator(ServletWebRequest request);
}
