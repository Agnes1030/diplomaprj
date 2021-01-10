package com.febs.security.code.sms;

public interface SmsCodeSender {
    void send(String mobile, String code);
}
