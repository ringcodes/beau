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

package cn.beau.repository.mapper;


import cn.beau.dto.ArticleGroupDto;
import cn.beau.dto.ArticleListDto;
import cn.beau.dto.query.ArticleQuery;
import cn.beau.dto.response.ArticleDetailVo;
import cn.beau.repository.model.ArticleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleMapper extends BaseMapper<ArticleEntity> {
    ArticleDetailVo getArticleAndContent(ArticleEntity article);

    Long queryArticleSimpleCount(ArticleQuery article);

    List<ArticleListDto> queryArticleSimplePage(ArticleQuery article);

    List<ArticleGroupDto> queryArticleGroup(@Param("groupCount") Integer groupCount);

    Long queryByLabelCount(@Param("labelId") Long labelId);

    List<ArticleListDto> queryByLabel(ArticleQuery query);
}
