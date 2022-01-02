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
import cn.beau.dto.query.TopicQuery;
import cn.beau.dto.request.TopicRequest;
import cn.beau.dto.request.TopicSortRequest;
import cn.beau.enums.RoleEnum;
import cn.beau.enums.TopicPositionEnum;
import cn.beau.enums.TopicTypeEnum;
import cn.beau.manager.TopicManager;
import cn.beau.repository.model.TopicEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 主题
 *
 * @author liushilin
 * @date 2021/12/15
 */
@RestController
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private TopicManager topicManager;

    @GetMapping("/list/{type}")
    public ResultObject list(@PathVariable TopicTypeEnum type) {

        return ResultUtil.newSucc(topicManager.queryTopicByType(type));
    }

    @PostMapping("/page")
    @AuthTag(role = RoleEnum.PRE_ADMIN)
    public ResultObject page(@RequestBody TopicQuery topicQuery) {
        return ResultUtil.newSucc(topicManager.queryTopicPage(topicQuery));
    }

    @PostMapping("/save")
    @AuthTag(role = RoleEnum.PRE_ADMIN)
    public ResultObject save(LoginUser loginUser, @RequestBody TopicRequest request) {
        TopicEntity topic = new TopicEntity();
        topic.setId(request.getId());
        if (request.getTopicType() != null) {
            topic.setTopicType(request.getTopicType().getType());
        }
        topic.setTopicPic(request.getTopicPic());
        topic.setTopicName(request.getTopicName());
        if (request.getTopicPosition() != null) {
            topic.setTopicPosition(request.getTopicPosition().getCode());
        }
        topic.setUpdateId(loginUser.getId());
        topic.setCreateId(loginUser.getId());
        return ResultUtil.newSucc(topicManager.saveAndUpdate(topic));
    }

    @DeleteMapping("/del/{id}")
    @AuthTag(role = RoleEnum.PRE_ADMIN)
    public ResultObject del(LoginUser loginUser, @PathVariable Long id) {
        return ResultUtil.newSucc(topicManager.delTopic(id, loginUser.getId()));
    }

    @PostMapping("/topicSort")
    @AuthTag(role = RoleEnum.PRE_ADMIN)
    public ResultObject topicSort(LoginUser loginUser, @RequestBody TopicSortRequest topicSortRequest) {
        return ResultUtil.newSucc(topicManager.sort(loginUser.getId(), topicSortRequest));
    }

    @GetMapping("/listTopicPos")
    public ResultObject listTopicPos() {
        return ResultUtil.newSucc(TopicPositionEnum.getAll());
    }

    @GetMapping("/listTopicType")
    public ResultObject listTopicType() {
        return ResultUtil.newSucc(TopicTypeEnum.getAll());
    }
}
