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

import cn.beau.base.BasePage;
import cn.beau.base.KeyValueVo;
import cn.beau.component.oss.OssService;
import cn.beau.dto.query.TopicQuery;
import cn.beau.dto.request.TopicSortRequest;
import cn.beau.dto.response.TopicCountVo;
import cn.beau.dto.response.TopicListVo;
import cn.beau.dto.response.TopicVo;
import cn.beau.enums.TopicPositionEnum;
import cn.beau.enums.TopicTypeEnum;
import cn.beau.repository.mapper.TopicMapper;
import cn.beau.repository.model.TopicEntity;
import cn.beau.utils.QueryBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 主题管理
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Service
public class TopicManager {
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private OssService ossService;

    public TopicEntity getTopicById(Long id) {
        return topicMapper.selectById(id);
    }

    public boolean saveAndUpdate(TopicEntity topic) {
        Assert.notNull(topic.getTopicName(), "主题名不能为空");
        Assert.notNull(topic.getTopicType(), "类型不能为空");
        Integer count;
        TopicEntity exist = topicMapper.selectById(topic.getId());
        if (exist == null) {
            count = topicMapper.insert(topic);
        } else {
            count = topicMapper.updateById(topic);
        }
        return count > 0;
    }

    public BasePage<TopicListVo> queryTopicPage(TopicQuery candidate) {
        QueryBuilder queryBuilder = QueryBuilder.init(candidate);
        queryBuilder.eq("id", candidate.getId());
        if (candidate.getTopicType() != null) {
            queryBuilder.eq("topic_type", candidate.getTopicType().getType());
        }
        if (candidate.getTopicPositionEnum() != null) {
            queryBuilder.eq("topic_position", candidate.getTopicPositionEnum().getCode());
        }
        queryBuilder.eq("topic_name", candidate.getTopicName());
        queryBuilder.eq("deleted", 0);
        long count = topicMapper.selectCount(queryBuilder.buildCount());
        BasePage<TopicListVo> page = new BasePage<>();
        page.setPageNumber(candidate.getPageNumber());
        if (count > 0) {
            queryBuilder.orderByAsc("topic_sort");
            List<TopicEntity> data = topicMapper.selectList(queryBuilder.build());
            List<TopicListVo> list = new ArrayList<>(data.size());
            for (int i = 0; i < data.size(); i++) {
                TopicEntity t = data.get(i);
                TopicListVo topicRo = new TopicListVo();
                topicRo.setId(t.getId());
                topicRo.setTopicName(t.getTopicName());
                topicRo.setUpdateTime(t.getUpdateTime());
                topicRo.setTopicPic(t.getTopicPic());
                TopicPositionEnum topicPositionEnum = TopicPositionEnum.ofCode(t.getTopicPosition());
                topicRo.setTopicPosition(new KeyValueVo(topicPositionEnum.name(), topicPositionEnum.getDesc(), topicPositionEnum.getCode()));
                if (StringUtils.isNotBlank(t.getTopicPic())) {
                    topicRo.setTopicPicView(formatUrl(t.getTopicPic()));
                }
                TopicTypeEnum topicTypeEnum = TopicTypeEnum.ofCode(t.getTopicType());
                topicRo.setTopicType(new KeyValueVo(topicTypeEnum.name(), topicTypeEnum.getDesc(), topicTypeEnum.getType()));
                list.add(topicRo);
            }
            page.setList(list);
            page.setTotalRow(count);
        }
        return page;
    }

    public List<TopicVo> queryTopicByType(TopicTypeEnum type) {
        TopicQuery query = new TopicQuery();
        query.setTopicType(type);
        return list(query, Boolean.FALSE);
    }

    public boolean delTopic(Long id, Long updateId) {
        TopicEntity topic = new TopicEntity();
        topic.setId(id);
        topic.setDeleted(1);
        topic.setUpdateId(updateId);
        return topicMapper.updateById(topic) > 0;
    }

    public List<TopicCountVo> queryTopicArticleCount(TopicTypeEnum topicTypeEnum) {
        TopicEntity topic = new TopicEntity();
        if (topicTypeEnum != null) {
            topic.setTopicType(topicTypeEnum.getType());
        }
        List<TopicCountVo> list = topicMapper.queryTopicArticleCount(topic);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    public List<TopicVo> list(TopicQuery topicQuery, Boolean pic) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (topicQuery.getTopicPositionEnum() != null) {
            queryWrapper.eq("topic_position", topicQuery.getTopicPositionEnum().getCode());
        }
        if (topicQuery.getTopicType() != null) {
            queryWrapper.eq("topic_type", topicQuery.getTopicType().getType());
        }

        queryWrapper.eq("deleted", 0);
        queryWrapper.select("id", "topic_name", "topic_type", "topic_pic");
        queryWrapper.orderByAsc("topic_sort");
        List<TopicEntity> list = topicMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(it -> {
            TopicVo topicVo = new TopicVo();
            topicVo.setId(it.getId());
            topicVo.setTopicName(it.getTopicName());
            topicVo.setTopicType(it.getTopicType());
            if (pic) {
                topicVo.setTopicPic(formatUrl(it.getTopicPic()));
            }
            return topicVo;
        }).collect(Collectors.toList());
    }

    public List<TopicVo> listTopicPos(TopicPositionEnum topicTypeEnum, Boolean pic) {
        TopicQuery topicQuery = new TopicQuery();
        topicQuery.setTopicPositionEnum(topicTypeEnum);
        return list(topicQuery, pic);

    }

    private String formatUrl(String url) {
        if (StringUtils.isBlank(url)) {
            url = "topic/topic_1.jpg";
        }
        return ossService.getViewUrl(url);
    }

    public boolean sort(Long userId, TopicSortRequest request) {
        TopicEntity exist = getTopicById(request.getId());
        if (request.getPre()) {
            TopicEntity pre = getTopicById(request.getPreId());
            if (pre.getTopicSort() == exist.getTopicSort()) {
                exist.setTopicSort(exist.getTopicSort() + 1);
            }
            TopicEntity preEntity = new TopicEntity();
            preEntity.setId(request.getPreId());
            preEntity.setTopicSort(exist.getTopicSort());
            preEntity.setUpdateId(userId);
            TopicEntity entity = new TopicEntity();
            entity.setId(request.getId());
            entity.setTopicSort(pre.getTopicSort());
            entity.setUpdateId(userId);
            topicMapper.updateById(preEntity);
            topicMapper.updateById(entity);
        } else {
            TopicEntity post = getTopicById(request.getPostId());
            if (post.getTopicSort() == exist.getTopicSort()) {
                post.setTopicSort(post.getTopicSort() + 1);
            }
            TopicEntity postEntity = new TopicEntity();
            postEntity.setId(request.getPostId());
            postEntity.setTopicSort(exist.getTopicSort());
            postEntity.setUpdateId(userId);
            TopicEntity entity = new TopicEntity();
            entity.setId(request.getId());
            entity.setTopicSort(post.getTopicSort());
            entity.setUpdateId(userId);
            topicMapper.updateById(postEntity);
            topicMapper.updateById(entity);
        }
        return Boolean.TRUE;
    }
}
