package com.febs.security.code.sms;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.febs.security.code.ValidateCode;
import com.febs.security.code.ValidateCodeGenerator;
import com.febs.security.properties.FebsSecurityProperties;

@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private FebsSecurityProperties securityProperties;

    @Override
    public ValidateCode createCode() {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }
}
