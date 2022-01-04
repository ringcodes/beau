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

/**
 * github登录
 *
 * @author liushilin
 * @date 2022/1/4
 */
@Component
@Slf4j
public class GithubOauthLoginImpl extends AbstractOauthLogin {
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s&redirect_uri=%s";
    private static final String USER_INFO_URL = "https://api.github.com/user";
    private static final String LOGIN_URL = "https://github.com/login/oauth/authorize?client_id=%s&state=STATE&redirect_uri=%s";

    @Override
    public String loginUrl(String backUrl) {
        try {
            ConfigDto configDto = getConfig();
            return String.format(LOGIN_URL, configDto.getAppKey(), URLEncoder.encode(getLoginCallback(), "UTF-8"));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public LoginUser login(HttpServletRequest request) {
        try {
            ConfigDto configDto = getConfig();
            String code = request.getParameter("code");
            String data = SimpleHttpRequest.get(String.format(TOKEN_URL, configDto.getAppKey(), configDto.getAppSecret(), code, getLoginCallback()));
            Assert.hasText(data, "登录失败");
            String[] dataSplit = data.split("&");
            String token = dataSplit[0].split("=")[1];
            Map<String, String> head = new HashMap<>();
            head.put("Authorization", "token " + token);
            String user = SimpleHttpRequest.get(USER_INFO_URL, head);
            Assert.hasText(user, "登录失败");
            JSONObject userInfo = JSONObject.parseObject(user);
            OauthUser oauthUser = new OauthUser();
            oauthUser.setOpenId(userInfo.getString("id"));
            oauthUser.setName(userInfo.getString("name"));
            oauthUser.setAvatar(userInfo.getString("avatar_url"));
            return saveUser(oauthUser);
        } catch (Exception e) {
            log.error("github登录失败", e);
        }
        return null;
    }

    @Override
    public OauthTypeEnum oauthType() {
        return OauthTypeEnum.GITHUB;
    }
}
