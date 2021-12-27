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


import cn.beau.base.ResultObject;
import cn.beau.base.ResultUtil;
import cn.beau.dto.request.StaticsRequest;
import cn.beau.dto.response.TopicCountVo;
import cn.beau.enums.TopicTypeEnum;
import cn.beau.manager.StaticsManager;
import cn.beau.manager.TopicManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * 数据统计
 *
 * @author liushilin
 * @date 2021/12/15
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private StaticsManager staticsManager;
    @Autowired
    private TopicManager topicManager;

    @GetMapping("/acount")
    public ResultObject statistic() {
        return ResultUtil.newSucc(staticsManager.label());
    }

    @GetMapping("/chartData")
    public ResultObject chartData() {
        List<TopicCountVo> list = topicManager.queryTopicArticleCount(TopicTypeEnum.ARTICLE);
        if (CollectionUtils.isEmpty(list)) {
            return ResultUtil.newSucc();
        }
        return ResultUtil.newSucc(list);
    }

    @GetMapping("/qushi")
    public ResultObject qushi(StaticsRequest staticsRequest) {
        return ResultUtil.newSucc(staticsManager.qushi(staticsRequest));
    }
}
