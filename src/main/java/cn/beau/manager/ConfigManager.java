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
import cn.beau.dto.config.LinkVo;
import cn.beau.dto.query.ConfigQuery;
import cn.beau.dto.response.ConfigListPage;
import cn.beau.enums.ConfigKeyEnum;
import cn.beau.enums.ConfigTypeEnum;
import cn.beau.enums.IConfigTypeEnum;
import cn.beau.enums.StatusEnum;
import cn.beau.repository.mapper.ConfigMapper;
import cn.beau.repository.model.ConfigEntity;
import cn.beau.utils.QueryBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 配置管理
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Service
public class ConfigManager {
    @Autowired
    private ConfigMapper configMapper;

    private Cache<String, String> cache = Caffeine.newBuilder()
        .expireAfterAccess(12, TimeUnit.HOURS)
        .maximumSize(10_000)
        .build();

    public ConfigEntity getConfigById(Long id) {
        return configMapper.selectById(id);
    }

    public List<LinkVo> getLinkList() {
        List<ConfigEntity> list = querySimpleList(ConfigTypeEnum.WEB_CONFIG, ConfigKeyEnum.LINK);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(item -> {
            LinkVo linkVo = new LinkVo();
            linkVo.setName(item.getConfigName());
            linkVo.setUrl(item.getConfigContent());
            return linkVo;
        }).collect(Collectors.toList());
    }

    public String getConfigContent(ConfigTypeEnum configTypeEnum, IConfigTypeEnum config) {
        String cacheDb = cache.get(configTypeEnum.name() + "_" + config.getCode(), k -> {
            QueryBuilder queryBuilder = QueryBuilder.init();
            queryBuilder.eq("config_type", configTypeEnum.name());
            queryBuilder.eq("config_key", config.getCode());
            queryBuilder.eq("config_status", StatusEnum.ENABLE.getCode());
            ConfigEntity c = configMapper.selectOne(queryBuilder.build());
            if (c == null) {
                return null;
            }
            return c.getConfigContent();
        });
        return cacheDb;
    }

    public boolean saveAndUpdate(ConfigEntity config) {
        ConfigTypeEnum.valid(config.getConfigType());
        Assert.hasText(config.getConfigContent(), "配置内容不能为空");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("config_type", config.getConfigType());
        queryWrapper.eq("config_key", config.getConfigKey());
        queryWrapper.eq("deleted", 0);
        if (StringUtils.isEmpty(config.getConfigMd5())) {
            config.setConfigMd5(DigestUtils.md5Hex(config.getConfigContent()));
        }
        queryWrapper.eq("config_md5", config.getConfigMd5());

        queryWrapper.last("limit 1");
        List<ConfigEntity> exist = configMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(exist)) {
            config.setCreateId(config.getUpdateId());
            configMapper.insert(config);
        } else {
            config.setId(exist.get(0).getId());
            config.setConfigStatus(StatusEnum.ENABLE.getCode());
            configMapper.updateById(config);
        }
        cache.invalidate(config.getConfigType() + "_" + config.getConfigKey());
        return false;
    }

    public BasePage<ConfigListPage> queryConfigPage(ConfigQuery config) {
        QueryBuilder queryBuilder = QueryBuilder.init(config);
        queryBuilder.eq("config_key", config.getConfigKeyEnum());
        queryBuilder.eq("config_name", config.getName());
        queryBuilder.eq("config_type", config.getConfigTypeEnum());
        queryBuilder.eq("deleted", 0);
        long count = configMapper.selectCount(queryBuilder.buildCount());
        BasePage<ConfigListPage> page = new BasePage<>();
        page.setPageNumber(config.getPageNumber());
        if (count > 0) {
            if (config.getQueryContent() != null && config.getQueryContent()) {
                queryBuilder.select("config_md5", "config_type", "config_key", "config_name", "config_content", "update_time", "config_status", "update_id");
            } else {
                queryBuilder.select("config_md5", "config_type", "config_key", "config_name", "update_time", "config_status", "update_id");
            }
            List<ConfigEntity> data = configMapper.selectList(queryBuilder.build());
            List<ConfigListPage> list = new ArrayList<>(data.size());
            if (data != null) {
                for (ConfigEntity c : data) {
                    ConfigListPage configListPage = new ConfigListPage();
                    configListPage.setTypeName(ConfigKeyEnum.getDesc(c.getConfigType()));
                    configListPage.setId(c.getId());
                    configListPage.setKeyName(ConfigKeyEnum.getDesc(c.getConfigKey()));
                    configListPage.setConfigName(c.getConfigName());
                    configListPage.setConfigContent(c.getConfigContent());
                    configListPage.setUpdateTime(c.getUpdateTime());
                    configListPage.setConfigMd5(c.getConfigMd5());
                    list.add(configListPage);
                }
            }
            page.setList(list);
            page.setTotalRow(count);
        }
        return page;
    }

    public boolean delConfig(Long id, Long updateId) {
        ConfigEntity config = new ConfigEntity();
        config.setId(id);
        config.setUpdateId(updateId);
        config.setDeleted(1);
        ConfigEntity exist = getConfigById(id);
        cache.invalidate(exist.getConfigType() + "_" + exist.getConfigKey());
        return configMapper.updateById(config) > 0;
    }

    private List<ConfigEntity> querySimpleList(ConfigTypeEnum configTypeEnum, ConfigKeyEnum configKeyEnum) {
        QueryWrapper queryBuilder = new QueryWrapper();
        queryBuilder.eq("config_type", configTypeEnum.name());
        queryBuilder.in("config_key", configKeyEnum.name());
        queryBuilder.eq("deleted", 0);
        queryBuilder.select("id", "config_name", "config_content");
        return configMapper.selectList(queryBuilder);
    }

    public List<ConfigEntity> query(ConfigQuery query) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.eq("config_type", query.getConfigTypeEnum());
        queryBuilder.eq("config_key", query.getConfigKeyEnum());
        queryBuilder.eq("deleted", 0);
        if (query.getQueryContent() != null && query.getQueryContent()) {
            queryBuilder.select("config_type", "config_md5", "config_key", "config_content");
        } else {
            queryBuilder.select("config_type", "config_md5", "config_key");
        }
        return configMapper.selectList(queryBuilder.build());
    }
}
