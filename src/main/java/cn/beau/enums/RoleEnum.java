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

package cn.beau.enums;

import cn.beau.base.KeyValueVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色类型
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Getter
public enum RoleEnum {
    SYS(99, "系统管理员"),
    ADMIN(10, "超级管理员"),
    PRE_ADMIN(3, "普通管理员"),
    USER(1, "普通用户");

    private int code;
    private String desc;

    RoleEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static List<KeyValueVo> listAll() {
        List<KeyValueVo> list = new ArrayList<>();
        for (RoleEnum r : values()) {
            if (r != RoleEnum.SYS){
                list.add(new KeyValueVo(r.name(), r.getDesc(), r.getCode()));
            }
        }
        return list;
    }
}
