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

package cn.beau.dto;

import lombok.Data;

import java.util.List;
/**
 * 文章
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Data
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private String description;
    private Integer points;
    private Long topicId;
    private Integer publishStatus;
    private String sourceUrl;
    private String titlePic;
    private Integer sourceType;
    private Integer privilege;
    private String sourceName;
    private Integer articleType;

    private List<Long> tagList;
}
