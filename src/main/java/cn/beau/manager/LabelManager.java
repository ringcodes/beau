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
import cn.beau.dto.query.LabelQuery;
import cn.beau.dto.response.ArticleLabelVo;
import cn.beau.dto.response.LabelListVo;
import cn.beau.dto.response.LabelPageListVo;
import cn.beau.repository.mapper.LabelMapper;
import cn.beau.repository.model.LabelEntity;
import cn.beau.utils.QueryBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标签管理
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Service
public class LabelManager {
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private UserManager userManager;

    public LabelEntity getById(Long id) {

        return labelMapper.selectById(id);
    }

    public boolean saveAndUpdate(LabelEntity label) {
        Integer count;
        if (label.getId() == null) {
            count = labelMapper.insert(label);
        } else {
            count = labelMapper.updateById(label);
        }
        return count > 0;
    }

    public BasePage<LabelPageListVo> queryLabelPage(LabelQuery label) {
        QueryBuilder queryBuilder = QueryBuilder.init(label);
        queryBuilder.eq("id", label.getId());
        queryBuilder.like("name", label.getName());
        queryBuilder.eq("deleted", 0);
        long count = labelMapper.selectCount(queryBuilder.buildCount());
        BasePage<LabelPageListVo> page = new BasePage();
        page.setPageNumber(label.getPageNumber());
        if (count > 0) {
            List<LabelEntity> data = labelMapper.selectList(queryBuilder.build());
            List<LabelPageListVo> result = new ArrayList<>(data.size());
            Map<Long, String> userNames = userManager.listUserName(data.stream().map(LabelEntity::getCreateId).distinct().collect(Collectors.toList()));
            data.forEach(it -> {
                LabelPageListVo labelPageListVo = new LabelPageListVo();
                labelPageListVo.setId(it.getId());
                labelPageListVo.setName(it.getName());
                labelPageListVo.setCreateName(userNames.get(it.getCreateId()));
                labelPageListVo.setUpdateTime(it.getUpdateTime());
                result.add(labelPageListVo);
            });
            page.setList(result);
            page.setTotalRow(count);
        }
        return page;
    }

    public boolean delLabel(Long id, Long updateId) {
        LabelEntity label = new LabelEntity();
        label.setId(id);
        label.setDeleted(1);
        return labelMapper.updateById(label) > 0;
    }

    public List<ArticleLabelVo> queryByArticle(List<Long> articleId) {
        if (CollectionUtils.isEmpty(articleId)) {
            return Collections.emptyList();
        }
        return labelMapper.queryByArticle(articleId);
    }

    public List<LabelListVo> listAll() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("deleted", 0);
        List<LabelEntity> list = labelMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            List<LabelListVo> result = new ArrayList<>();
            list.forEach(it -> {
                LabelListVo labelListVo = new LabelListVo();
                labelListVo.setId(it.getId());
                labelListVo.setName(it.getName());
                result.add(labelListVo);
            });
            return result;
        }
        return Collections.emptyList();
    }
}
