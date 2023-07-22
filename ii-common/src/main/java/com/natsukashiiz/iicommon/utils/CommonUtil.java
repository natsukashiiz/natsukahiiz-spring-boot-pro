package com.natsukashiiz.iicommon.utils;

import com.natsukashiiz.iicommon.common.DeviceCode;
import com.natsukashiiz.iicommon.model.Pagination;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;


public class CommonUtil {
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_AGENT);
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    public static Pageable getPaginate(Pagination paginate) {
        return PageRequest.of(paginate.getPage() > 0 ? paginate.getPage() - 1 : 0, paginate.getLimit());
    }

    public static DeviceCode getDevice(String userAgent) {
        if (userAgent.contains("Android")) {
            return DeviceCode.ANDROID;
        } else if (userAgent.contains("iPhone")) {
            return DeviceCode.IPHONE;
        } else if (userAgent.contains("Windows")) {
            return DeviceCode.WINDOWS;
        } else if (userAgent.contains("Macintosh")) {
            return DeviceCode.MAC;
        } else if (userAgent.contains("Linux")) {
            return DeviceCode.LINUX;
        } else {
            return DeviceCode.UNKNOWN;
        }
    }


    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
