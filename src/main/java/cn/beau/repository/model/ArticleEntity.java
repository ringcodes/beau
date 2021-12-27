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

package cn.beau.repository.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.beau.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
/**
 * 文章
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Getter
@Setter
@TableName("t_article")
public class ArticleEntity extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("description")
    private String description;

    @TableField("points")
    private Integer points;

    @TableField("topic_id")
    private Long topicId;

    @TableField("publish_status")
    private Integer publishStatus;

    @TableField("source_url")
    private String sourceUrl;

    @TableField("title_pic")
    private String titlePic;

    @TableField("flag_type")
    private Integer flagType;

    @TableField("article_type")
    private Integer articleType;

    @TableField("source_type")
    private Integer sourceType;

    @TableField("source_name")
    private String sourceName;

    @TableField("publish_time")
    private Date publishTime;

    @TableField("seo_keys")
    private String seoKeys;

    @TableField("seo_desc")
    private String seoDesc;
}
