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

package cn.beau.manager;

import cn.beau.base.LoginUser;
import cn.beau.component.oauth.OauthUser;
import cn.beau.exception.BizException;
import cn.beau.exception.ParamException;
import cn.beau.repository.model.UserEntity;
import cn.beau.utils.PasswordUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 登录管理
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Service
public class LoginManager {
    /**
     * 登录token缓存
     */
    private Cache<String, String> cache = Caffeine.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .maximumSize(1000)
        .build();

    @Autowired
    private UserManager userManager;

    public LoginUser login(String name, String password) {
        Assert.notNull(name, "用户名必填");
        Assert.notNull(password, "密码必填");
        String key = "login_name_" + name;
        String val = cache.getIfPresent(key);
        if (val != null) {
            if (Integer.valueOf(val) > 5) {
                throw new BizException("登录次数过多，请稍候再试", Level.INFO);
            }
            cache.put(key, String.valueOf(Integer.valueOf(val) + 1));
        } else {
            cache.put(key, String.valueOf(1));
        }
        UserEntity user = new UserEntity();
        int index = name.indexOf("@");
        if (index > -1) {
            user.setEmail(name);
        } else {
            user.setMobile(name);
        }
        UserEntity query = userManager.getUser(user);
        if (query == null) {
            throw new ParamException("用户名或密码错误");
        }
        if (StringUtils.isEmpty(query.getPassword())) {
            throw new ParamException("用户名或密码错误");
        }
        String pass = PasswordUtil.getPassword(password, query.getSalt());
        if (!query.getPassword().equals(pass)) {
            cache.put(key, String.valueOf(1));
            throw new ParamException("用户名或密码错误");
        }
        LoginUser loginUser = saveSession(query.getName(), query.getId(), query.getRole());
        return loginUser;
    }

    @Async
    public void registry(UserEntity user) {
        UserEntity query = new UserEntity();
        query.setOpenId(user.getOpenId());
        UserEntity db = userManager.getUser(query);
        if (db == null) {
            userManager.insert(user);
        }
    }

    public LoginUser loginOauth(OauthUser oauthUser) {
        UserEntity query = new UserEntity();
        if (StringUtils.hasText(oauthUser.getEmail())) {
            query.setEmail(oauthUser.getEmail());
        }
        if (StringUtils.hasText(oauthUser.getName())) {
            query.setName(oauthUser.getName());
        }
        UserEntity db = userManager.getUser(query);
        LoginUser loginUser = new LoginUser();
        if (db == null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setAvatar(oauthUser.getAvatar());
            userEntity.setEmail(oauthUser.getEmail());
            userEntity.setName(oauthUser.getName());
            userEntity.setOpenId(oauthUser.getOpenId());
            userEntity.setSource(oauthUser.getSource());
            userManager.insert(userEntity);
            loginUser.setId(userEntity.getId());
            loginUser.setName(userEntity.getName());
        } else {
            loginUser.setId(db.getId());
            loginUser.setName(db.getName());
        }
        return loginUser;
    }

    private LoginUser saveSession(String name, Long id, Integer position) {
        LoginUser loginUser = new LoginUser();
        loginUser.setName(name);
        loginUser.setId(id);
        loginUser.setRole(position);
        return loginUser;
    }
}
