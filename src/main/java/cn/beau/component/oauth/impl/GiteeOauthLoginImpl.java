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

package cn.beau.component.oauth.impl;

import cn.beau.base.LoginUser;
import cn.beau.component.oauth.ConfigDto;
import cn.beau.component.oauth.OauthTypeEnum;
import cn.beau.component.oauth.OauthUser;
import cn.beau.utils.SimpleHttpRequest;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class GiteeOauthLoginImpl extends AbstractOauthLogin {
    private static final String LOGIN_URL = "https://gitee.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String USER_INFO = "https://gitee.com/api/v5/user?access_token=%s";

    @Override
    public String loginUrl(String backUrl) {
        try {
            ConfigDto configDto = getConfig();
            return String.format(LOGIN_URL, configDto.getAppKey(), URLEncoder.encode(getLoginCallback(), "UTF-8"));
        } catch (Exception e) {
            log.warn("获取配置", e);
            return null;
        }
    }

    @Override
    public LoginUser login(HttpServletRequest request) {
        try {
            ConfigDto configDto = getConfig();
            JSONObject body = new JSONObject();
            String code = request.getParameter("code");
            Map<String, String> head = new HashMap<>();
            head.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 ");
            body.put("client_secret", configDto.getAppSecret());
            body.put("grant_type", "authorization_code");
            body.put("code", code);
            body.put("client_id", configDto.getAppKey());
            body.put("redirect_uri", getLoginCallback());
            String data = SimpleHttpRequest.post("https://gitee.com/oauth/token", body.toJSONString(), head);
            JSONObject jsonObject = JSONObject.parseObject(data);
            String token = jsonObject.getString("access_token");
            Assert.hasText(token, "获取TOKEN失败");
            String user = SimpleHttpRequest.get(String.format(USER_INFO, token), head);
            JSONObject userInfo = JSONObject.parseObject(user);
            OauthUser oauthUser = new OauthUser();
            oauthUser.setOpenId(userInfo.getString("id"));
            oauthUser.setName(userInfo.getString("name"));
            oauthUser.setAvatar(userInfo.getString("avatar_url"));
            return saveUser(oauthUser);
        } catch (Exception e) {
            log.error("gitee登录失败", e);
        }
        return null;
    }

    @Override
    public OauthTypeEnum oauthType() {
        return OauthTypeEnum.GIT_EE;
    }
}
