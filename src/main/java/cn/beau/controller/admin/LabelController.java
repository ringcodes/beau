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
import cn.beau.dto.query.LabelQuery;
import cn.beau.enums.ResourceEnum;
import cn.beau.enums.RoleEnum;
import cn.beau.manager.LabelManager;
import cn.beau.repository.model.LabelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 标签接口
 *
 * @author liushilin
 * @date 2021/12/8
 */
@RestController
@RequestMapping("/tag")
public class LabelController {
    @Autowired
    private LabelManager labelManager;

    @GetMapping("/list")
    @AuthTag(name = ResourceEnum.LABEL_LIST)
    public ResultObject list() {
        return ResultUtil.newSucc(labelManager.listAll());
    }

    @PostMapping("/page")
    @AuthTag(name = ResourceEnum.LABEL_LIST)
    public ResultObject page(@RequestBody LabelQuery query) {
        return ResultUtil.newSucc(labelManager.queryLabelPage(query));
    }

    @PostMapping("/save")
    @AuthTag(name = ResourceEnum.LABEL_EDIT)
    public ResultObject save(LoginUser loginUser, @RequestBody LabelEntity labelEntity) {
        labelEntity.setCreateId(loginUser.getId());
        labelEntity.setUpdateId(loginUser.getId());
        return ResultUtil.newSucc(labelManager.saveAndUpdate(labelEntity));
    }

    @DeleteMapping("/del/{id}")
    @AuthTag(name = ResourceEnum.LABEL_DEL)
    public ResultObject del(LoginUser loginUser, @PathVariable Long id) {
        return ResultUtil.newSucc(labelManager.delLabel(id, loginUser.getId()));
    }
}
