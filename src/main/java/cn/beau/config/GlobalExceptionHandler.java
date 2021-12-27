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

package cn.beau.config;

import cn.beau.base.ResultObject;
import cn.beau.base.ResultUtil;
import cn.beau.exception.BizException;
import cn.beau.exception.NoLoginException;
import cn.beau.exception.StateCode;
import cn.beau.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 异常处理类
 *
 * @author: liushilin
 * @date: 2020/3/16 10:51 上午
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResultObject handleBindException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception) {
        final BindingResult result = exception.getBindingResult();
        final List<FieldError> allErrors = result.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError errorMessage : allErrors) {
            sb.append(errorMessage.getField()).append(": ").append(errorMessage.getDefaultMessage()).append(", ");
        }
        defineStatus(response, exception);
        return ResultUtil.newFailed(StateCode.INPUT_ERROR, sb.toString());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResultObject<String> handleInvalidFormatException(HttpServletRequest request, HttpServletResponse response, InvalidFormatException exception) {
        defineStatus(response, exception);
        return ResultUtil.newFailed(StateCode.INPUT_ERROR, exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultObject<String> handleHttpMessageNotReadableException(HttpServletRequest request, HttpServletResponse response, HttpMessageNotReadableException exception) {
        log.warn("handleHttpMessageNotReadableException error:", exception);
        defineStatus(response, exception);
        return ResultUtil.newFailed(StateCode.INPUT_ERROR, exception.getRootCause().getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResultObject<String> handleIllegalArgumentException(HttpServletRequest request, HttpServletResponse response, IllegalArgumentException exception) {
        log.warn("handleIllegalArgumentException error:", exception);
        defineStatus(response, exception);
        return ResultUtil.newFailed(StateCode.ERROR, exception.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResultObject<String> handleNullPointerException(HttpServletRequest request, HttpServletResponse response, NullPointerException exception) {
        log.warn("NullPointerException error:", exception);
        defineStatus(response, exception);
        return ResultUtil.newFailed(StateCode.ERROR, "内部服务空指针异常");
    }

    @ExceptionHandler(Exception.class)
    public ResultObject<String> handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        defineStatus(response, exception);
        if (exception instanceof BizException) {
            BizException bizException = (BizException) exception;
            return ResultUtil.newFailed(bizException.getStateCode(), bizException.getMessage());
        }
        log.error("global-error:", exception);
        return ResultUtil.newFailed(StateCode.ERROR, exception.getMessage());
    }

    /**
     * 处理HTTP状态
     *
     * @param response
     * @param e
     */
    private void defineStatus(HttpServletResponse response, Exception e) {
        if (e instanceof NoLoginException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else if (e instanceof UnauthorizedException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
