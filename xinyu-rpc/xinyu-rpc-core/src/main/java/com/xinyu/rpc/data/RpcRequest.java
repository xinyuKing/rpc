
package com.xinyu.rpc.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest {
    private String requestId;
    private String className;// 接口名称
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
}
