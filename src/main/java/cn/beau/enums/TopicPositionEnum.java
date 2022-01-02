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
 * 主题位置
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Getter
public enum TopicPositionEnum {
    UNKNOWN(0, "未知", "tips"),
    MENU(1, "全局菜单", "无需标题图片"),
    INDEX(2, "首页专题列表", "请上传190x120比例的图片"),
    LIST(3, "普通列表", "请上传35x35比例的图片");
    private int code;
    private String desc;
    private String tips;

    TopicPositionEnum(int code, String desc, String tips) {
        this.code = code;
        this.desc = desc;
        this.tips = tips;
    }

    public static TopicPositionEnum ofCode(int code) {
        for (TopicPositionEnum item : values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public static List<KeyValueVo> getAll() {
        List<KeyValueVo> paramVos = new ArrayList<>();
        for (TopicPositionEnum dictType : values()) {
            if (dictType != UNKNOWN) {
                paramVos.add(new KeyValueVo(dictType.name(), dictType.getDesc(), dictType.getTips()));
            }
        }
        return paramVos;
    }
}
