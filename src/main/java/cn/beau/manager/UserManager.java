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

import cn.beau.base.BasePage;
import cn.beau.base.LoginUser;
import cn.beau.dto.query.UserQuery;
import cn.beau.dto.request.ModifyPasswordRequest;
import cn.beau.dto.request.UserRegRequest;
import cn.beau.dto.response.UserResp;
import cn.beau.enums.UserStatusEnum;
import cn.beau.exception.ParamException;
import cn.beau.repository.mapper.UserMapper;
import cn.beau.repository.model.UserEntity;
import cn.beau.utils.CheckUtil;
import cn.beau.utils.PasswordUtil;
import cn.beau.utils.QueryBuilder;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户管理
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Service
public class UserManager {
    @Autowired
    private UserMapper userMapper;
    // 缓存
    private Cache<Long, String> userCache = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .maximumSize(1000)
        .build();

    public LoginUser getByUserId(Long openId) {
        String content = userCache.getIfPresent(openId);
        if (content == null) {
            UserEntity userEntity = userMapper.selectById(openId);
            LoginUser loginDto = new LoginUser();
            if (userEntity != null) {
                loginDto.setId(userEntity.getId());
                loginDto.setAvatar(userEntity.getAvatar());
                loginDto.setName(userEntity.getName());
                userCache.put(loginDto.getId(), JSONObject.toJSONString(loginDto));
            }
            return loginDto;
        }
        return JSONObject.parseObject(content, LoginUser.class);
    }

    public UserEntity getByOpenId(String openId) {
        QueryBuilder queryBuilder = QueryBuilder.init();
        queryBuilder.eq("openId", openId);
        return userMapper.selectOne(queryBuilder.build());
    }

    public boolean insert(UserEntity user) {
        if (!StringUtils.hasText(user.getName())) {
            user.setName("未填");
        }
        user.setStatus(1);
        if (StringUtils.hasText(user.getPassword())) {
            String salt = PasswordUtil.getSalt();
            String passNew = PasswordUtil.getPassword(user.getPassword(), salt);
            user.setPassword(passNew);
            user.setSalt(salt);
        }
        return userMapper.insert(user) > 0;
    }

    /**
     * 更新用户，只能更新部分信息
     *
     * @param user
     * @return
     */
    public Boolean update(UserEntity user) {
        UserEntity save = new UserEntity();
        save.setId(user.getId());
        save.setName(user.getName());
        save.setRole(user.getRole());
        save.setSex(user.getSex());
        return userMapper.updateById(user) > 0;
    }

    public Boolean modifyPassword(ModifyPasswordRequest user, Long userId) {
        UserEntity query = userMapper.selectById(userId);
        ;
        String pass = PasswordUtil.getPassword(user.getOldPassword(), query.getSalt());
        if (!query.getPassword().equals(pass)) {
            throw new ParamException("原密码不正确");
        }
        UserEntity save = new UserEntity();
        save.setId(userId);
        String salt = PasswordUtil.getSalt();
        String passNew = PasswordUtil.getPassword(user.getNewPassword(), salt);
        save.setPassword(passNew);
        save.setSalt(salt);
        save.setUpdateId(userId);
        return userMapper.updateById(save) > 0;
    }


    public UserEntity getUser(UserEntity user) {
        QueryBuilder queryBuilder = QueryBuilder.init();
        queryBuilder.eq("openId", user.getOpenId());
        queryBuilder.eq("id", user.getId());
        queryBuilder.eq("name", user.getName());
        queryBuilder.eq("email", user.getEmail());
        queryBuilder.eq("mobile", user.getMobile());
        return userMapper.selectOne(queryBuilder.build());
    }

    public BasePage<UserResp> queryUserPage(UserQuery user) {
        QueryBuilder queryBuilder = QueryBuilder.init(user);
        queryBuilder.like("name", user.getName());
        long count = userMapper.selectCount(queryBuilder.buildCount());
        BasePage<UserResp> page = new BasePage<>();
        page.setPageNumber(user.getPageNumber());
        if (count > 0) {
            List<UserEntity> data = userMapper.selectList(queryBuilder.build());
            page.setList(data.stream().map(it -> {
                UserResp userResp = new UserResp();
                userResp.setId(it.getId());
                userResp.setAvatar(it.getAvatar());
                userResp.setEmail(it.getEmail());
                userResp.setName(it.getName());
                userResp.setRole(it.getRole());
                userResp.setSource(it.getSource());
                userResp.setMobile(it.getMobile());
                userResp.setStatus(it.getStatus());
                userResp.setUpdateTime(it.getUpdateTime());
                userResp.setSex(it.getSex());
                return userResp;
            }).collect(Collectors.toList()));
            page.setTotalRow(count);
        }
        return page;
    }

    public Map<Long, String> listUserName(List<Long> userIds) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("deleted", 0);
        queryWrapper.in("id", userIds);
        queryWrapper.select("id", "name");
        List<UserEntity> data = userMapper.selectList(queryWrapper);
        return data.stream().collect(Collectors.toMap(UserEntity::getId, UserEntity::getName));
    }

    /**
     * 禁用用户
     *
     * @param id
     * @param updateId
     * @return
     */
    public Boolean forbid(Long id, Long updateId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setUpdateId(updateId);
        userEntity.setStatus(UserStatusEnum.FORBID.getCode());
        return userMapper.updateById(userEntity) > 0;
    }

    public Boolean reg(UserRegRequest request) {
        Assert.hasText(request.getUsername(), "用户名不能为空");
        Assert.hasText(request.getEmail(), "邮箱不能为空");
        Assert.hasText(request.getPassword(), "密码不能为空");
        Assert.hasText(request.getPasswordAgain(), "确认密码不能为空");
        if (!request.getPassword().equals(request.getPasswordAgain())) {
            throw new ParamException("两次输入的密码不一样");
        }
        if (!CheckUtil.checkEmail(request.getEmail())){
            throw new ParamException("邮箱格式不正确");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", request.getEmail());
        queryWrapper.eq("deleted", 0);
        queryWrapper.last("limit 1");
        List<UserEntity> list = userMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            throw new ParamException("输入的邮箱已注册");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setName(request.getUsername());
        userEntity.setEmail(request.getEmail());
        userEntity.setSource("注册");
        userEntity.setStatus(1);
        String salt = PasswordUtil.getSalt();
        String passNew = PasswordUtil.getPassword(userEntity.getPassword(), salt);
        userEntity.setPassword(passNew);
        userEntity.setSalt(salt);
        return userMapper.insert(userEntity) > 0;
    }

}
