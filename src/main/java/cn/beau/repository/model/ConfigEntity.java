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

import cn.beau.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 配置
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Data
@TableName("t_config")
public class ConfigEntity extends BaseEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置名
     */
    @TableField(value = "config_name")
    private String configName;
    /**
     * 配置类型
     */
    @TableField(value = "config_type")
    private String configType;

    /**
     * 配置KEY
     */
    @TableField(value = "config_key")
    private String configKey;

    /**
     * 配置内容
     */
    @TableField(value = "config_content")
    private String configContent;

    /**
     * 配置内容
     */
    @TableField(value = "config_md5")
    private String configMd5;

    /**
     * 配置状态
     */
    @TableField(value = "config_status")
    private Integer configStatus;
}
