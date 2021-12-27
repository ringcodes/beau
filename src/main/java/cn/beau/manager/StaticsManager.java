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

import cn.beau.base.KeyValueVo;
import cn.beau.dto.query.ArticleQuery;
import cn.beau.dto.request.StaticsRequest;
import cn.beau.dto.response.StaticsChartVo;
import cn.beau.repository.mapper.ArticleMapper;
import cn.beau.repository.mapper.LabelMapper;
import cn.beau.repository.mapper.StaticsMapper;
import cn.beau.repository.mapper.TopicMapper;
import cn.beau.repository.mapper.UserMapper;
import cn.beau.repository.model.LabelEntity;
import cn.beau.repository.model.TopicEntity;
import cn.beau.repository.model.UserEntity;
import cn.beau.utils.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * 统计数据
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Component
public class StaticsManager {
    // 缓存
    private Cache<String, List> cache = Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .maximumSize(1000)
        .build();

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StaticsMapper staticsMapper;
    @Autowired
    private LabelMapper labelMapper;

    /**
     * 卡片数据统计
     * @return
     */
    public List label() {
        String key = "chart#label";
        return cache.get(key, (k) -> {
            List<KeyValueVo> list = new ArrayList<>(6);
            list.add(new KeyValueVo("文章数", "", articleMapper.queryArticleSimpleCount(new ArticleQuery())));
            list.add(new KeyValueVo("主题数", "", topicMapper.selectCount(
                Wrappers.lambdaQuery(TopicEntity.class)
                    .eq(TopicEntity::getDeleted, 0)))
            );
            list.add(new KeyValueVo("用户数", "", userMapper.selectCount(
                Wrappers.lambdaQuery(UserEntity.class)
                    .eq(UserEntity::getDeleted, 0)))
            );
            list.add(new KeyValueVo("标签数", "", labelMapper.selectCount(
                Wrappers.lambdaQuery(LabelEntity.class)
                    .eq(LabelEntity::getDeleted, 0))));
            return list;
        });
    }

    /**
     * 文章增长趋势数据
     * @param staticsRequest
     * @return
     */
    public List<StaticsChartVo> qushi(StaticsRequest staticsRequest) {
        String key = "chart#qushi";
        return cache.get(key, (k) -> {
            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isEmpty(staticsRequest.getCreateTimeEnd())) {
                staticsRequest.setCreateTimeEnd(dtf2.format(DateUtil.monthEnd(0)));
            }
            if (StringUtils.isEmpty(staticsRequest.getCreateTimeStart())) {
                staticsRequest.setCreateTimeStart(dtf2.format(DateUtil.monthStart(0)));
            }
            return staticsMapper.qushiList(staticsRequest);
        });
    }
}

