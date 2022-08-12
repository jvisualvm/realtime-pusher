package com.risen.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/7 10:30
 */
public class ServiceUtil {


    public static String getServiceIp() {
        String ip = null;
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static String getServiceIpByPrefix(String prefix) {
        String finalIp = null;
        try {
            List<String> lst = getServiceIpList();
            if (PredicateUtil.isNotEmpty(lst)) {
                List<String> filterIp = lst.stream().filter(s -> s.startsWith(prefix)).collect(Collectors.toList());
                if (PredicateUtil.isNotEmpty(filterIp)) {
                    finalIp = filterIp.get(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalIp;
    }

    public static List<String> getServiceIpList() {
        List<String> lst = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresss = ni.getInetAddresses();
                while (addresss.hasMoreElements()) {
                    InetAddress nextElement = addresss.nextElement();
                    String hostAddress = nextElement.getHostAddress();
                    lst.add(hostAddress);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lst;
    }


}
