
package com.xinyu.rpc.util;


import com.xinyu.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpUtil.class);

    public static String getHostAddress() {
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
            return host;
        } catch (UnknownHostException e) {
            LOGGER.error("Cannot get server host.", e);
        }
        return host;
    }

    /**
     * 获取实际的IP
     * @return
     */
    public static String getRealIp()  {
        String localIp = null;
        String netIp = null;

        try {
            // 获取当前主机所有网卡信息
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            boolean finded = false;
            InetAddress ip = null;
            // 遍历所有网卡信息
            while (networkInterfaces.hasMoreElements() && !finded) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                // 遍历IP信息
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        // 获取真实IP
                        netIp = ip.getHostAddress();
                        finded = true;
                        break;
                    } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        //当IP地址不是地区本地地址， 直接取主机IP
                        localIp = ip.getHostAddress();
                    }
                }
            }

            // 优先获取实际网络IP
            if (netIp != null && !"".equals(netIp)) {
                return netIp;
            } else {
                return localIp;
            }
        } catch (SocketException ex) {
            throw new RpcException(ex);
        }
    }
}
