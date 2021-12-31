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

package cn.beau.controller.admin;

import cn.beau.base.LoginUser;
import cn.beau.base.ResultObject;
import cn.beau.base.ResultUtil;
import cn.beau.component.TemplateComponent;
import cn.beau.component.WebConfigComponent;
import cn.beau.component.oauth.IOauthLogin;
import cn.beau.component.oauth.OauthFactory;
import cn.beau.component.oauth.OauthTypeEnum;
import cn.beau.dto.request.LoginRequest;
import cn.beau.manager.LoginManager;
import cn.beau.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 登录
 *
 * @author liushilin
 * @date 2021/12/15
 */
@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private LoginManager loginManager;
    @Autowired
    private OauthFactory oauthFactory;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private WebConfigComponent webConfigComponent;

    @PostMapping("/login")
    public ResultObject login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        LoginUser loginUser = loginManager.login(loginRequest.getUsername(), loginRequest.getPassword());
        setCookie(loginUser, request, response);
        return ResultUtil.newSucc();
    }

    @GetMapping("/current")
    public String current(LoginUser loginUser) {
        if (loginUser == null) {
            return "";
        }
        Map<String, Object> data = new HashMap<>();
        data.put("user", loginUser);
        return templateComponent.generateString(data, "./layout/login.html");
    }

    @PostMapping("/doLogin")
    public ResultObject doLogin(@RequestBody LoginRequest loginRequest) {
        LoginUser loginUser = loginManager.login(loginRequest.getUsername(), loginRequest.getPassword());
        Map<String, String> data = new HashMap<>();
        data.put("name", loginUser.getName());
        data.put("avatar", loginUser.getAvatar());
        data.put("token", JwtUtil.sign(loginUser));
        return ResultUtil.newSucc(data);
    }

    @GetMapping(value = "/{source}")
    public ResultObject dingLogin(@PathVariable String source, HttpServletRequest request, HttpServletResponse response) {
        OauthTypeEnum oauthTypeEnum = OauthTypeEnum.valueOf(source.toUpperCase(Locale.ROOT));
        IOauthLogin oauthLogin = oauthFactory.get(oauthTypeEnum);
        LoginUser loginUser = oauthLogin.login(request);
        if (loginUser != null) {
            setCookie(loginUser, request, response);
            try {
                String back = request.getParameter("back");
                response.sendRedirect(StringUtils.hasText(back) ? back : "/");
            } catch (Exception e) {

            }
        }
        return ResultUtil.newFailed("登录失败");
    }

    @GetMapping("/info")
    public ResultObject info(LoginUser loginUser) {
        return ResultUtil.newSucc(loginUser);
    }

    private void setCookie(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", JwtUtil.sign(loginUser));
        cookie.setHttpOnly(true);
        String host = webConfigComponent.getWebSiteConfig().getHost();
        if (StringUtils.hasText(host)){
            Integer index = host.indexOf(".");
            cookie.setDomain(host.substring(index + 1));
        }
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(cookie);
    }
}
