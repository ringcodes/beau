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

package cn.beau.base;

import cn.beau.exception.StateCode;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
/**
 * 响应对象类
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Data
public class ResultObject<T> {
    private int code;
    private String msg;
    private T data;
    private Map<String, Object> params;

    private synchronized void init() {
        if (params == null) {
            params = new HashMap<>();
        }
    }

    public ResultObject() {

    }

    public ResultObject(T t) {
        this.code = StateCode.SUCCESS.getCode();
        this.data = t;
    }

    public ResultObject(int code, String msg, T t) {
        this.code = code;
        this.msg = msg;
        this.data = t;
    }

    public ResultObject(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void put(String key, Object obj) {
        init();
        params.put(key, obj);
    }

    public boolean isOk() {
        return this.code == (StateCode.SUCCESS.getCode());
    }
}
