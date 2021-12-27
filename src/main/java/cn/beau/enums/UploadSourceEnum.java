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

import lombok.Getter;
/**
 * 文件上传来源
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Getter
public enum UploadSourceEnum {
    DEFAULT(0, "default"),
    BOOK(1, "ebook"),
    TOPIC(2, "topic"),
    ARTICLE(3, "article"),
    SLIDER(4, "slider"),
    ;
    private int type;
    String path;

    UploadSourceEnum(int type, String path) {
        this.type = type;
        this.path = path;
    }

    public static UploadSourceEnum getBy(Integer type) {
        for (UploadSourceEnum uploadSourceEnum : values()) {
            if (uploadSourceEnum.type == type) {
                return uploadSourceEnum;
            }
        }
        return DEFAULT;
    }
}
