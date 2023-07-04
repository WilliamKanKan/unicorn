package com.sztus.unicorn.lib.core.util;

import com.sztus.unicorn.lib.core.constant.GlobalConst;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {

    public static String getIpAddr(HttpServletRequest request) {

        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-for");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        //it's possible to show the multiple ip addresses in forwarded ip
        if (StringUtils.isNotEmpty(ip) && ip.contains(GlobalConst.STR_COMMA)) {
            String[] ipAddressArray = ip.split(GlobalConst.STR_COMMA);
            int length = ipAddressArray.length - 1;
            ip = ipAddressArray[length - 1].trim();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
