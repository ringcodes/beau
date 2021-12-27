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

import cn.beau.repository.mapper.ArticleLabelMapper;
import cn.beau.repository.model.ArticleLabelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * 标签管理
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Service
public class ArticleLabelManager {
    @Autowired
    private ArticleLabelMapper articleLabelMapper;

    /**
     * 保存文章标签
     * @param articleId
     * @param tagList
     * @param updateId
     */
    public void save(Long articleId, List<Long> tagList, Long updateId) {
        if (CollectionUtils.isEmpty(tagList)) {
            return;
        }
        List<ArticleLabelEntity> list = new ArrayList<>();
        tagList.forEach(it->{
            ArticleLabelEntity articleLabel = new ArticleLabelEntity();
            articleLabel.setArticleId(articleId);
            articleLabel.setLabelId(it);
            articleLabel.setCreateId(updateId);
            articleLabel.setUpdateId(updateId);
            list.add(articleLabel);
        });
        articleLabelMapper.delByArticleId(articleId);
        articleLabelMapper.insertBatch(list);
    }
}
