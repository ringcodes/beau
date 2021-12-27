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
 * 文章发布状态
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Getter
public enum PublishStatusEnum {
    PUBLISH(0, "未发布"),
    PUBLISHING(1, "发布中"),
    PUBLISHED(2, "已发布"),
    PUBLISH_FAIL(3, "发布失败");

    private int code;
    private String desc;

    PublishStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PublishStatusEnum getByCode(int code) {
        for (PublishStatusEnum publishStatusEnum : values()) {
            if (publishStatusEnum.code == code) {
                return publishStatusEnum;
            }
        }
        return null;
    }

    public static List<KeyValueVo> getAll(){
        List<KeyValueVo> paramVos = new ArrayList<>();
        for (PublishStatusEnum dictType : values()) {
            paramVos.add(new KeyValueVo(dictType.getDesc(),dictType.getCode()));
        }
        return paramVos;
    }
}
