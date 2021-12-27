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

@Getter
public enum AppSource {
    WX_H5("wx","微信h5"),
    XCX("wx_app","微信小程序"),
    WEB("web","网站");

    private String code;
    private String msg;

    AppSource(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static boolean notWeb(String code){
        if (code == null) {
            return false;
        }
        if (code==WX_H5.code){
            return true;
        }
        return false;
    }
}
