
package com.xinyu.rpc.util;


public class RequestIdUtil {

    /**
     * 获取一个全局唯一的字符串ID作为RequestId
     */
    public static String requestId() {
        return GlobalIDGenerator.getInstance().nextStrId();
    }
}
