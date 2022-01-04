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
import cn.beau.component.WebConfigComponent;
import cn.beau.component.oauth.ConfigDto;
import cn.beau.component.oauth.IOauthLogin;
import cn.beau.component.oauth.OauthUser;
import cn.beau.enums.ConfigTypeEnum;
import cn.beau.exception.BizException;
import cn.beau.exception.ParamException;
import cn.beau.manager.ConfigManager;
import cn.beau.manager.LoginManager;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public abstract class AbstractOauthLogin implements IOauthLogin {
    private Cache<String, String> cache = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(1000)
        .build();
    @Autowired
    private LoginManager loginManager;
    @Autowired
    private ConfigManager configManager;
    @Autowired
    private WebConfigComponent webConfigComponent;

    protected String getCache(String key) {
        return cache.getIfPresent(key);
    }

    protected void putCache(String key, String val) {
        cache.put(key, val);
    }

    protected LoginUser saveUser(OauthUser oauthUser) {
        return loginManager.loginOauth(oauthUser);
    }

    protected ConfigDto getConfig() {
        String content = configManager.getConfigContent(ConfigTypeEnum.LOGIN, oauthType());
        if (StringUtils.hasText(content)) {
            return JSONObject.parseObject(content, ConfigDto.class);
        }
        throw new ParamException("配置不存在");
    }

    protected String getLoginCallback() {
        try {
            if (webConfigComponent.getWebSiteConfig() == null || StringUtils.isEmpty(webConfigComponent.getWebSiteConfig().getHost())) {
                throw new BizException("网站第三方登录配置错误");
            }
            StringBuilder sb = new StringBuilder();
            sb.append(webConfigComponent.getWebSiteConfig().getHost());
            sb.append("/auth/").append(oauthType().name().toLowerCase(Locale.ROOT));
            return sb.toString();
        } catch (Exception e) {
            throw new BizException("网站配置错误");
        }
    }
}
