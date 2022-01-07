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

package cn.beau.controller;

import cn.beau.base.KeyValueVo;
import cn.beau.component.WebConfigComponent;
import cn.beau.component.oauth.IOauthLogin;
import cn.beau.component.oauth.OauthFactory;
import cn.beau.component.oauth.OauthTypeEnum;
import cn.beau.dto.config.WebRegConfigDto;
import cn.beau.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller("userWeb")
@RequestMapping("/")
public class UserController extends CommonController {
    @Autowired
    private OauthFactory oauthFactory;
    @Autowired
    private WebConfigComponent webConfigComponent;

    @GetMapping("/login.html")
    public String login(ModelMap modelMap, String backUrl, HttpServletRequest request) {
        setTitle(modelMap, "会员登录");
        modelMap.put("backUrl", backUrl);
        List<KeyValueVo> list = new ArrayList<>();
        for (OauthTypeEnum oauthTypeEnum : OauthTypeEnum.values()) {
            IOauthLogin login = oauthFactory.get(oauthTypeEnum);
            KeyValueVo keyValueVo = new KeyValueVo();
            keyValueVo.setName(oauthTypeEnum.name().toLowerCase(Locale.ROOT));
            String url = login.loginUrl(backUrl);
            if (StringUtils.hasText(url)) {
                keyValueVo.setValue(url);
                list.add(keyValueVo);
            }
        }
        modelMap.put("loginList", list);
        return "login";
    }

    @GetMapping("/reg.html")
    public String reg(ModelMap modelMap) {
        setTitle(modelMap, "会员注册");
        WebRegConfigDto webRegConfig = webConfigComponent.getWebRegConfig();
        if (webRegConfig != null) {
            modelMap.put("canReg", StatusEnum.ENABLE.getCode().equals(webRegConfig.getCanReg()));
        }
        return "reg";
    }
}
