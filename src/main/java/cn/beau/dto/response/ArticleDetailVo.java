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

package cn.beau.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;
/**
 * 文章详情
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Data
public class ArticleDetailVo {
    private String title;

    private String titlePic;
    private String titlePicView;
    private Long id;

    private String content;

    private String contentMdView;

    private String description;

    private Integer points;

    private Long topicId;

    private String sourceName;

    private Date updateTime;

    private Long createId;

    private String createName;

    private Date createTime;

    private Integer sourceType;

    private String sourceUrl;

    private String topicName;

    private Integer articleType;

    private String seoKeys;

    private String seoDesc;

    private List<ArticleLabelVo> articleLabelVos;
}
