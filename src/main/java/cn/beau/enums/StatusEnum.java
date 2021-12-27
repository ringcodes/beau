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
 * 配置KEY
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Getter
public enum StatusEnum {
    ENABLE(1, "生效"),
    UNABLE(2, "失效");

    private Integer code;
    private String desc;

    StatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String name) {
        for (StatusEnum configEnum : values()) {
            if (configEnum.name().equals(name)) {
                return configEnum.getDesc();
            }
        }
        return null;
    }

    public static List<KeyValueVo> getList() {
        List<KeyValueVo> paramVos = new ArrayList<>(values().length);
        for (StatusEnum configEnum : values()) {
            paramVos.add(new KeyValueVo(configEnum.name(), configEnum.getDesc()));
        }
        return paramVos;
    }
}
