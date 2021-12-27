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
import cn.beau.exception.BizException;
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
public enum ConfigKeyEnum implements IConfigTypeEnum {
    LINK(ConfigTypeEnum.WEB_CONFIG, "友情链接"),
    OAUTH_DING(ConfigTypeEnum.LOGIN, "钉钉登录"),
    OAUTH_GITHUB(ConfigTypeEnum.LOGIN, "github登录"),
    OAUTH_GITEE(ConfigTypeEnum.LOGIN, "gitee登录"),
    THEME(ConfigTypeEnum.WEB_CONFIG, "主题"),
    WEB_NAME(ConfigTypeEnum.WEB_CONFIG, "网站名"),
    OSS(ConfigTypeEnum.WEB_CONFIG, "文件存储");

    private String desc;
    private ConfigTypeEnum type;

    ConfigKeyEnum(ConfigTypeEnum type, String desc) {
        this.desc = desc;
        this.type = type;
    }

    public static void valid(String name) {
        try {
            ConfigKeyEnum.valueOf(name);
        } catch (Exception e) {
            throw new BizException("类型不存在");
        }
    }

    public static String getDesc(String name) {
        for (ConfigKeyEnum configEnum : values()) {
            if (configEnum.name().equals(name)) {
                return configEnum.getDesc();
            }
        }
        return null;
    }

    public static List<KeyValueVo> getList(String name) {
        List<KeyValueVo> paramVos = new ArrayList<>(values().length);
        for (ConfigKeyEnum configEnum : values()) {
            if (configEnum.getType().name().equals(name)) {
                paramVos.add(new KeyValueVo(configEnum.name(), configEnum.getDesc()));
            }
        }
        return paramVos;
    }

    @Override
    public String getCode() {
        return name();
    }
}
