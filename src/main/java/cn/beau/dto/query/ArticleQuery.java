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

package cn.beau.dto.query;

import cn.beau.base.BaseQuery;
import cn.beau.enums.ArticleFlagEnum;
import cn.beau.enums.TopicPositionEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 文章查询
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Getter
@Setter
@Accessors(chain = true)
public class ArticleQuery extends BaseQuery {
    private Long id;
    private String title;
    private Long topicId;
    private Integer topicType;
    private Integer publishStatus;
    private Long labelId;
    private Long createId;
    private Integer articleType;
    private ArticleFlagEnum articleFlagEnum;
}
