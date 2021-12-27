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
import lombok.Data;
/**
 *
 * 轮播管理
 * @author liushilin
 * @date 2021/12/15
 */
@Data
@TableName("t_slider")
public class SliderEntity extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;
    /**
     * 图片地址
     */
    @TableField(value = "pic")
    private String pic;

    /**
     * 跳转地址
     */
    @TableField(value = "target")
    private String target;

    /**
     * 类型
     * @see cn.beau.enums.SliderTypeEnum
     */
    @TableField(value = "slider_type")
    private Integer sliderType;

    /**
     * 状态
     * @see
     */
    @TableField(value = "slider_status")
    private Integer status;
}
