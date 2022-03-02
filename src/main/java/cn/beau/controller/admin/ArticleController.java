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
import cn.beau.dto.query.ArticleQuery;
import cn.beau.dto.request.ArticleRequest;
import cn.beau.dto.response.ArticleDetailVo;
import cn.beau.enums.ResourceEnum;
import cn.beau.manager.ArticleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章
 *
 * @author liushilin
 * @date 2021/12/15
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleManager articleManager;

    @PostMapping("/list")
    @ResponseBody
    @AuthTag(name = ResourceEnum.ARTICLE_LIST)
    public ResultObject list(@RequestBody ArticleQuery articleQuery) {
        return ResultUtil.newSucc(articleManager.querySimpleArticlePage(articleQuery));
    }

    @GetMapping("/getById/{id}")
    @ResponseBody
    @AuthTag(name = ResourceEnum.ARTICLE_LIST)
    public ResultObject getById(@PathVariable Long id) {
        ArticleDetailVo article = articleManager.getArticleAndContent(id);
        return ResultUtil.newSucc(article);
    }

    @PostMapping(value = "/save")
    @AuthTag(name = ResourceEnum.ARTICLE_EDIT)
    @ResponseBody
    public ResultObject save(LoginUser loginUser, @RequestBody ArticleRequest articleRequest) {
        articleRequest.setUpdateId(loginUser.getId());
        return ResultUtil.newSucc(articleManager.saveAndUpdate(articleRequest));
    }

    @DeleteMapping(value = "del/{id}")
    @AuthTag(name = ResourceEnum.ARTICLE_DEL)
    @ResponseBody
    public ResultObject del(LoginUser loginUser, @PathVariable Long id) {
        return ResultUtil.newSucc(articleManager.delArticle(id, loginUser.getId()));
    }
}
