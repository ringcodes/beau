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

package cn.beau.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
/**
 * 基础实体类
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Data
public class BaseEntity {
    @TableField(value = "create_time")
    protected Date createTime;

    @TableField(value = "update_time")
    protected Date updateTime;

    @TableField(value = "create_id")
    protected Long createId;

    @TableField(value = "update_id")
    protected Long updateId;

    @TableField(value = "deleted")
    protected Integer deleted;
}
