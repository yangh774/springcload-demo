package com.yangh.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/03/31 下午 07:57
 */
@Component
public class HeaderParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        String gateway = request.getHeader("gateway");
        if (gateway == null) {
            gateway = "buxuke!";
        }
        return gateway;
    }
}
