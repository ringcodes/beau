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
/**
 * 上传管理
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Getter
@Setter
@TableName("t_upload_file")
public class UploadFileEntity extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 上传来源
     * @see cn.beau.enums.UploadSourceEnum
     */
    @TableField("source")
    private Integer source;

    /**
     * 文件MD5
     */
    @TableField("md5")
    private String md5;

    /**
     * 文件大小 单位K
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;
}
