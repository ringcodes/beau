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

package cn.beau.component.oauth;

import cn.beau.base.KeyValueVo;
import cn.beau.enums.IConfigTypeEnum;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录类型
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Getter
public enum OauthTypeEnum implements IConfigTypeEnum {
    DING_DING("钉钉登录"),
    GITHUB("github登录"),
    GIT_EE("码云登录");

    private String desc;

    OauthTypeEnum(String desc) {
        this.desc = desc;
    }

    public static List<KeyValueVo> getList() {
        List<KeyValueVo> paramVos = new ArrayList<>(values().length);
        for (OauthTypeEnum configEnum : values()) {
            paramVos.add(new KeyValueVo(configEnum.name(), configEnum.getDesc()));
        }
        return paramVos;
    }


    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getDesc() {
        return getDesc();
    }
}
