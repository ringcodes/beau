/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.beau.exception;

import lombok.Getter;
/**
 * 异常状态码
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Getter
public enum StateCode {
    SUCCESS(200, "SYS", "成功"),
    //系统异常
    ERROR(1001, "SYS", "系统异常"),
    INPUT_ERROR(1003, "SYS", "参数错误"),
    NO_PAGE(1004, "SYS", "页面未找到"),
    CACHE_ERROR(1005, "SYS", "缓存异常"),
    //参数错误
    NOTNULL(3001, "INPUT", "不能为空"),

    //业务异常
    NO_USER(4001, "SERVICE", "用户不存在"),
    ERROR_PASSWORD(4002, "SERVICE", "密码错误"),
    LOCK_USER(4003, "SERVICE", "用户被锁"),
    NO_ROLE(4004, "SERVICE", "角色不存在"),
    BAN_USER(4005, "SERVICE", "用户被禁用"),
    NOT_LOGIN(4006, "SYS", "未登录"),
    NOT_RIGHT(4007, "SYS", "无权限"),
    ILLEGAL_SIGN(4008, "SYS", "非法的签名");

    private int code;
    private String type;
    private String desc;

    StateCode(int code, String type, String desc) {
        this.code = code;
        this.type = type;
        this.desc = desc;
    }

    /**
     * 根据编码返回描述
     *
     * @param code
     * @return String
     * @author 640
     * @date 2016年8月12日
     */
    public static String getDescByType(int code) {
        StateCode[] exceptionCode = StateCode.values();
        for (StateCode ex : exceptionCode) {
            if (ex.getCode() == code) {
                return ex.getDesc();
            }
        }
        return null;
    }
}
