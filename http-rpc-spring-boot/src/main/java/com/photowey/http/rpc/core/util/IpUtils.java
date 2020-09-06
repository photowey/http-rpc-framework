/*
 * Copyright © 2020 photowey (photowey@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.photowey.http.rpc.core.util;

import com.photowey.http.rpc.core.enums.HRpcStatus;
import com.photowey.http.rpc.core.exception.HRpcException;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * IpUtils Util
 *
 * @author WcJun
 * @date 2020/09/06
 * @since 1.1.0
 */
public final class IpUtils {

    private IpUtils() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String UNKNOWN = "unknown";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String LOCALHOST = "127.0.0.1";
    private static final Integer LENGTH = 15;
    private static final String SEPARATOR_COMMA = ",";

    /**
     * get Client IpAddress by HttpServletRequest Header
     *
     * @param request HttpServletRequest
     * @return the real Ip
     */
    public static String getIpAddr(HttpServletRequest request) {

        // x-forwarded-for
        String ip = request.getHeader(X_FORWARDED_FOR);

        // Proxy-Client-IP
        if (HRpcUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }

        // WL-Proxy-Client-IP
        if (HRpcUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }

        // 127.0.0.1
        if (HRpcUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals(LOCALHOST)) {
                // het localhost IP by network cards
                InetAddress inetaddress = null;
                try {
                    inetaddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    throw new HRpcException("unknown host exception");
                }
                if (null != inetaddress) {
                    ip = inetaddress.getHostAddress();
                }
            }
        }
        // for multiple proxy the first is real address
        if (ip != null && ip.length() > LENGTH) {
            if (ip.indexOf(SEPARATOR_COMMA) > 0) {
                ip = ip.substring(0, ip.indexOf(SEPARATOR_COMMA));
            }
        }

        return ip;
    }

    // ==========================================================
    //  @copy from arthas
    //  @see * https://github.com/alibaba/arthas
    //  @see * https://github.com/alibaba/arthas/blob/master/core/src/main/java/com/taobao/arthas/core/util/IPUtils.java
    // ==========================================================

    private static final String WINDOWS = "windows";
    private static final String OS_NAME = "os.name";

    /**
     * check: whether current operating system is windows
     *
     * @return true---is windows
     */
    public static boolean isWindowsOS() {
        String osName = System.getProperty(OS_NAME);
        return osName.toLowerCase().contains(WINDOWS);
    }

    /**
     * get IP address, automatically distinguish the operating system.（windows or linux）
     *
     * @return String
     */
    public static String getLocalIP() {
        InetAddress ip = null;
        try {
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
            } else {
                // scan all NetWorkInterfaces if it's loopback address
                if (!InetAddress.getLocalHost().isLoopbackAddress()) {
                    ip = InetAddress.getLocalHost();
                } else {
                    boolean bFindIP = false;
                    Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                    while (netInterfaces.hasMoreElements()) {
                        if (bFindIP) {
                            break;
                        }
                        NetworkInterface ni = netInterfaces.nextElement();
                        // ----------特定情况，可以考虑用ni.getName判断
                        // iterator all IPs
                        Enumeration<InetAddress> ips = ni.getInetAddresses();
                        while (ips.hasMoreElements()) {
                            ip = ips.nextElement();
                            // IP starts with 127. is loopback address
                            if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                                bFindIP = true;
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new HRpcException(HRpcStatus.FAILURE.toValue(), "find the local ip exception", e);
        }

        return ip == null ? null : ip.getHostAddress();
    }
}
