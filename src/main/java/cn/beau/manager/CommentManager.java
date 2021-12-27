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
import cn.beau.dto.query.CommentQuery;
import cn.beau.repository.mapper.CommentMapper;
import cn.beau.repository.model.CommentEntity;
import cn.beau.utils.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论管理
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Service
public class CommentManager {

    @Autowired
    private CommentMapper commentMapper;

    public boolean saveAndUpdate(CommentEntity comment) {
        int count;
        if (comment.getId() == null) {
            count = commentMapper.insert(comment);
        } else {
            count = commentMapper.updateById(comment);
        }
        return count > 0;
    }

    public BasePage<CommentEntity> queryCommentPage(CommentQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.init(query);
        queryBuilder.eq("article_id", query.getArticleId());
        queryBuilder.eq("deleted", 0);
        long count = commentMapper.selectCount(queryBuilder.buildCount());
        BasePage<CommentEntity> page = new BasePage<>();
        if (count > 0) {
            List<CommentEntity> data = commentMapper.selectList(queryBuilder.build());
            page.setList(data);
            page.setTotalRow(count);
        }
        return page;
    }

    public boolean delComment(Long id, Long updateId) {
        CommentEntity comment = new CommentEntity();
        comment.setId(id);
        comment.setUpdateId(updateId);
        return commentMapper.updateById(comment) > 0;
    }
}
