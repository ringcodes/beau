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
/**
 * 响应对象工具类
 *
 * @author liushilin
 * @date 2021/12/15
 */
public class ResultUtil {
    public static ResultObject<Void> newSucc(){
        return new ResultObject<Void>(StateCode.SUCCESS.getCode(), "操作成功");
    }

    public static <E> ResultObject newSucc(E t){
        return new ResultObject(StateCode.SUCCESS.getCode(), "操作成功", t);
    }

    public static <E> ResultObject newSucc(E t,String msg){
        return new ResultObject(StateCode.SUCCESS.getCode(), msg, t);
    }

    public static <E> ResultObject newFailed(String msg){
        return new ResultObject(StateCode.ERROR.getCode(),msg);
    }

    public static <E> ResultObject newFailed(StateCode statuCode,String msg){
        return new ResultObject(statuCode.getCode(), msg);
    }
}
