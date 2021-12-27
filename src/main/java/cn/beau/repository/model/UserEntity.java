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
 * 用户
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Getter
@Setter
@TableName("t_user")
public class UserEntity extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("name")
    private String name;

    /**
     * 手机
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 性别
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 密码加密盐
     */
    @TableField("salt")
    private String salt;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 角色
     */
    @TableField("role")
    private Integer role;

    /**
     * 第三方标识
     */
    @TableField("open_id")
    private String openId;
}
