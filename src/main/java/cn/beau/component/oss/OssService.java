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

package cn.beau.component.oss;


import cn.beau.dto.FileInfoDto;

import java.io.File;
import java.io.InputStream;
/**
 * 文件存储接口
 *
 * @author liushilin
 * @date 2021/12/15
 */
public interface OssService {
    /**
     * 文件上传
     * @param data
     * @param size
     * @param fileName
     * @return
     */
    FileInfoDto upload(byte[] data, long size, String fileName);

    /**
     * 文件上传
     * @param data
     * @param size
     * @param fileName
     * @return
     */
    FileInfoDto upload(InputStream data, long size, String fileName);

    /**
     * 获取文件链接
     * @param name
     * @return
     */
    String getViewUrl(String name);

    /**
     * 获取文件链接
     * @param name
     * @param timeout
     * @return
     */
    String getViewUrl(String name, int timeout);

    /**
     * 删除文件
     * @param fileName
     * @return
     */
    boolean delFile(String fileName);
}
