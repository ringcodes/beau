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

package cn.beau.exception;

import lombok.Data;
import org.slf4j.event.Level;
/**
 * 基础业务异常
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Data
public class BizException extends RuntimeException {
    private StateCode stateCode;
    private Level level = Level.ERROR;

    public BizException(String message) {
        super(message);
        this.stateCode = StateCode.ERROR;
    }

    public BizException(String message, Level level) {
        super(message);
        this.level = level;
        this.stateCode = StateCode.ERROR;
    }

    public BizException(String message, Throwable e, Level level) {
        super(message, e);
        this.stateCode = StateCode.ERROR;
        this.level = level;
    }

    public BizException(String message, Throwable e, StateCode stateCode, Level level) {
        super(message, e);
        this.stateCode = stateCode;
        this.level = level;
    }

    public BizException(String message, Throwable e) {
        super(message, e);
        this.stateCode = StateCode.ERROR;
    }


    public BizException(String message, StateCode stateCode) {
        super(message);
        this.stateCode = stateCode;
    }

    public BizException(String message, StateCode stateCode, Level level) {
        super(message);
        this.stateCode = stateCode;
        this.level = level;
    }
}
