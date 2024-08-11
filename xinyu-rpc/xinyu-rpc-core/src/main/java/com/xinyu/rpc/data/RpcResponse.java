
package com.xinyu.rpc.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse {
    private String requestId;
    private Object result;
    private Throwable cause;

    public boolean isError() {
        return cause != null;
    }
}
