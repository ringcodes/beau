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

package cn.beau.component;

import cn.beau.dto.config.WebRegConfigDto;
import cn.beau.dto.config.WebSiteConfigDto;
import cn.beau.enums.ConfigKeyEnum;
import cn.beau.enums.ConfigTypeEnum;
import cn.beau.exception.BizException;
import cn.beau.manager.ConfigManager;
import cn.beau.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配置
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Component
public class WebConfigComponent {
    @Autowired
    private ConfigManager configManager;

    public WebSiteConfigDto getWebSiteConfig() {
        try {
            String content = configManager.getConfigContent(ConfigTypeEnum.WEB_CONFIG, ConfigKeyEnum.WEB);
            return JsonUtil.string2Obj(content, WebSiteConfigDto.class);
        } catch (Exception e) {
            throw new BizException("网站配置错误");
        }
    }

    public WebRegConfigDto getWebRegConfig() {
        try {
            String content = configManager.getConfigContent(ConfigTypeEnum.WEB_CONFIG, ConfigKeyEnum.REG);

            return JsonUtil.string2Obj(content, WebRegConfigDto.class);
        } catch (Exception e) {
            throw new BizException("网站配置错误");
        }
    }
}
