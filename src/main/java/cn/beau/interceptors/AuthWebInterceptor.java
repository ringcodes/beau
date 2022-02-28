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

package cn.beau.interceptors;

import cn.beau.anno.AuthTag;
import cn.beau.anno.StaticTag;
import cn.beau.base.LoginUser;
import cn.beau.component.TemplateComponent;
import cn.beau.enums.RoleEnum;
import cn.beau.exception.NoLoginException;
import cn.beau.exception.UnauthorizedException;
import cn.beau.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 权限相关的拦截器
 *
 * @author liushilin
 * @date 2021-12-08
 */
@Component
@Slf4j
public class AuthWebInterceptor implements HandlerInterceptor {
    @Autowired
    private TemplateComponent templateComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 静态化 且静态文件存在时直接输出静态视图
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            StaticTag staticTag = handlerMethod.getMethod().getAnnotation(StaticTag.class);
            if (staticTag != null && templateComponent.exist(staticTag.tpl())) {
                InputStream inputStream = null;
                ServletOutputStream sos = null;
                try {
                    File file = new File(templateComponent.getFilePath(staticTag.tpl()));
                    inputStream = new FileInputStream(file);
                    sos = response.getOutputStream();
                    int len = 1;
                    byte[] b = new byte[1024];
                    while ((len = inputStream.read(b)) != -1) {
                        sos.write(b, 0, len);
                    }
                    return false;
                } catch (Exception e) {
                    log.error("静态化出错", e);
                } finally {
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(sos);
                }
            }
        }
        // 权限拦截
        hasPermission(request, handler);
        return true;
    }

    /**
     * 校验权限
     *
     * @param request
     * @param handler
     */
    private void hasPermission(HttpServletRequest request, Object handler) {
        LoginUser loginUser = getLoginUser(request);
        request.setAttribute("LOGIN_USER", loginUser);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上的注解
            AuthTag requiredPermission = handlerMethod.getMethod().getAnnotation(AuthTag.class);
            // 如果方法上的注解为空 则获取类的注解
            if (requiredPermission == null) {
                requiredPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(AuthTag.class);
            }
            if (requiredPermission == null) {
                return;
            }

            if (loginUser == null) {
                throw new NoLoginException();
            }
            if (requiredPermission.role().length == 0) {
                return;
            }
            // 如果标记了注解，则判断权限
            for (RoleEnum roleEnum : requiredPermission.role()) {
                if (roleEnum.getCode() <= loginUser.getRole()) {
                    return;
                }
            }
            throw new UnauthorizedException();
        }
    }

    /**
     * 解析当前登录用户
     *
     * @param request
     * @return
     */
    private LoginUser getLoginUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            token = getTokenFromCookie(request);
        }
        LoginUser loginDto = null;
        if (StringUtils.hasText(token)) {
            loginDto = JwtUtil.verify(token);
            if (loginDto != null) {
                loginDto.setIp(getIpAddress(request));
                loginDto.setToken(token);
                request.setAttribute("LOGIN_USER", loginDto);
            }
        }
        return loginDto;
    }

    /**
     * 从cookie中获取token
     *
     * @param request
     * @return
     */
    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("token")) {
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }

    /**
     * 获取用户操作的IP
     *
     * @param request
     * @return
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
