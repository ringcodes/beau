/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more
 *  * contributor license agreements.  See the NOTICE file distributed with
 *  * this work for additional information regarding copyright ownership.
 *  * The ASF licenses this file to You under the Apache License, Version 2.0
 *  * (the "License"); you may not use this file except in compliance with
 *  * the License.  You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package cn.beau.enums;

import cn.beau.base.KeyValueVo;
import cn.beau.exception.BizException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum SliderTypeEnum {
    NAV(1, "首页轮播","请上传720x320的图片"),
    LIST(2, "列表广告","请上传320x180的图片"),
    DETAIL(3, "详情广告","请上传320x180的图片"),
    INDEX(4, "首页广告","请上传320x180的图片"),
    NAV_TUI(5, "首页推荐","请上传320x180的图片"),
    ;


    private int code;
    private String desc;
    private String tips;

    SliderTypeEnum(int code, String desc,String tips) {
        this.code = code;
        this.desc = desc;
        this.tips = tips;
    }

    public static SliderTypeEnum ofCode(Integer code) {
        for (SliderTypeEnum dictType : values()) {
            if (dictType.getCode() == code) {
                return dictType;
            }
        }
        throw new BizException("类型不存在");
    }

    public static List<KeyValueVo> getAll() {
        List<KeyValueVo> paramVos = new ArrayList<>();
        for (SliderTypeEnum dictType : values()) {
            paramVos.add(new KeyValueVo(dictType.name(), dictType.getDesc(), dictType.getTips()));
        }
        return paramVos;
    }
}
