
package com.xinyu.rpc.enums;

import lombok.Getter;

/**
 * 查找服务提供者结果的状态枚举
 */
@Getter
public enum StatusEnum {

    SUCCESS(200, "OK"),

    NOT_FOUND_SERVICE_PROVINDER(100001, "not found service provider");


    private Integer code;
    private String description;

    StatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
