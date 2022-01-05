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
import cn.beau.component.EmailComponent;
import cn.beau.component.WebConfigComponent;
import cn.beau.dto.MailInfo;
import cn.beau.dto.config.WebRegConfigDto;
import cn.beau.dto.query.UserQuery;
import cn.beau.dto.request.ModifyPasswordRequest;
import cn.beau.dto.request.UserRegRequest;
import cn.beau.enums.RoleEnum;
import cn.beau.manager.UserManager;
import cn.beau.repository.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理
 *
 * @author liushilin
 * @date 2021/12/15
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserManager userManager;
    @Autowired
    private EmailComponent emailComponent;
    @Autowired
    private WebConfigComponent webConfigComponent;

    // rest 接口
    @PostMapping("/page")
    @AuthTag(role = RoleEnum.PRE_ADMIN)
    public ResultObject page(@RequestBody UserQuery query) {
        return ResultUtil.newSucc(userManager.queryUserPage(query));
    }

    @PostMapping("/{id}/forbid")
    @AuthTag(role = RoleEnum.ADMIN)
    public ResultObject forbid(LoginUser loginUser, @PathVariable Long id) {
        return ResultUtil.newSucc(userManager.forbid(id, loginUser.getId()));
    }

    @PostMapping("/save")
    @AuthTag(role = RoleEnum.ADMIN)
    public ResultObject save(LoginUser loginUser, @RequestBody UserEntity userEntity) {
        userEntity.setUpdateId(loginUser.getId());
        return ResultUtil.newSucc(userManager.update(userEntity));
    }

    @PostMapping("/add")
    @AuthTag(role = RoleEnum.ADMIN)
    public ResultObject add(LoginUser loginUser, @RequestBody UserEntity userEntity) {
        userEntity.setUpdateId(loginUser.getId());
        return ResultUtil.newSucc(userManager.insert(userEntity));
    }

    @PostMapping("/modifyPassword")
    @AuthTag(role = RoleEnum.USER)
    public ResultObject modifyPassword(LoginUser loginUser, @RequestBody ModifyPasswordRequest resq) {
        return ResultUtil.newSucc(userManager.modifyPassword(resq, loginUser.getId()));
    }

    @PostMapping("/reg")
    public ResultObject reg(@RequestBody UserRegRequest request) {
        Boolean reg = userManager.reg(request);
        if (reg) {
            WebRegConfigDto webRegConfigDto = webConfigComponent.getWebRegConfig();
            MailInfo mailInfo = new MailInfo();
            mailInfo.setContent(webRegConfigDto.getContent());
            if (StringUtils.hasText(webRegConfigDto.getSubject())) {
                mailInfo.setSubject(webRegConfigDto.getSubject());
            } else {
                mailInfo.setSubject("新用户注册");
            }
            mailInfo.setToAddress(request.getEmail());
            emailComponent.sendHtmlMail(mailInfo);
        }
        return ResultUtil.newSucc(reg);
    }
}
