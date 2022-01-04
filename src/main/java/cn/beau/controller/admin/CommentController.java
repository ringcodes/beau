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
import cn.beau.dto.query.CommentQuery;
import cn.beau.enums.RoleEnum;
import cn.beau.manager.CommentManager;
import cn.beau.repository.model.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 评论
 *
 * @author liushilin
 * @date 2021/12/15
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentManager commentManager;

    @PostMapping("/page")
    @AuthTag(role = RoleEnum.PRE_ADMIN)
    public ResultObject page(@RequestBody CommentQuery query) {
        return ResultUtil.newSucc(commentManager.queryCommentPage(query));
    }

    @PostMapping("/save")
    @AuthTag(role = RoleEnum.USER)
    public ResultObject save(LoginUser loginUser, @RequestBody CommentEntity comment) {
        comment.setCreateId(loginUser.getId());
        comment.setUpdateId(loginUser.getId());
        return ResultUtil.newSucc(commentManager.saveAndUpdate(comment));
    }

    @GetMapping("/list/{articleId}")
    public ResultObject list(@PathVariable Long articleId) {
        return ResultUtil.newSucc(commentManager.queryByArticleId(articleId));
    }

    @DeleteMapping("/del/{id}")
    @AuthTag(role = RoleEnum.PRE_ADMIN)
    public ResultObject del(LoginUser loginUser, @PathVariable Long id) {
        return ResultUtil.newSucc(commentManager.delComment(id, loginUser.getId()));
    }
}
