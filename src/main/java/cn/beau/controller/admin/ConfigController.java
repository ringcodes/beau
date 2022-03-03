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

import cn.beau.anno.AuthTag;
import cn.beau.base.LoginUser;
import cn.beau.base.ResultObject;
import cn.beau.base.ResultUtil;
import cn.beau.dto.query.ConfigQuery;
import cn.beau.enums.ConfigKeyEnum;
import cn.beau.enums.ConfigTypeEnum;
import cn.beau.enums.ResourceEnum;
import cn.beau.manager.ConfigManager;
import cn.beau.repository.model.ConfigEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置
 *
 * @author liushilin
 * @date 2021/12/15
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigManager configManager;

    @PostMapping("/save")
    @AuthTag(name = ResourceEnum.CONFIG_EDIT)
    public ResultObject save(LoginUser userLoginDto, @RequestBody ConfigEntity config) {
        if (StringUtils.isBlank(config.getConfigType())) {
            return ResultUtil.newFailed("biz不能为空");
        }
        config.setUpdateId(userLoginDto.getId());
        configManager.saveAndUpdate(config);
        return ResultUtil.newSucc();
    }

    @GetMapping("/{type}/{key}")
    @AuthTag(name = ResourceEnum.CONFIG_LIST)
    public ResultObject getByBiz(@PathVariable("type") ConfigTypeEnum configTypeEnum, @PathVariable("key") ConfigKeyEnum config) {
        return ResultUtil.newSucc(configManager.getConfigContent(configTypeEnum, config));
    }

    @GetMapping("/{type}")
    public ResultObject getBiz(@PathVariable("type") ConfigTypeEnum configTypeEnum) {
        return ResultUtil.newSucc(ConfigKeyEnum.getList(configTypeEnum.name()));
    }

    @DeleteMapping("/del/{id}")
    @AuthTag(name = ResourceEnum.CONFIG_DEL)
    public ResultObject del(LoginUser userLoginDto, @PathVariable Long id) {
        return ResultUtil.newSucc(configManager.delConfig(id, userLoginDto.getId()));
    }


    @GetMapping("/get/{id}")
    @AuthTag(name = ResourceEnum.CONFIG_LIST)
    public ResultObject get(@PathVariable Long id) {
        return ResultUtil.newSucc(configManager.getConfigById(id));
    }

    @PostMapping("/page")
    @AuthTag(name = ResourceEnum.CONFIG_LIST)
    public ResultObject list(@RequestBody ConfigQuery config) {
        return ResultUtil.newSucc(configManager.queryConfigPage(config));
    }

    @PostMapping("/query")
    @AuthTag(name = ResourceEnum.CONFIG_LIST)
    public ResultObject query(@RequestBody ConfigQuery config) {
        return ResultUtil.newSucc(configManager.query(config));
    }
}
