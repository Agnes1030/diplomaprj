package com.febs.web.controller.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.febs.common.domain.FebsConstant;

@Controller
public class ExceptionController {

    @RequestMapping(FebsConstant.FEBS_ACCESS_DENY_URL)
    public String accessDeny(){
        return "error/403";
    }
}
