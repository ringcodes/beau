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

package cn.beau.component.oss.impl;

import cn.beau.component.oss.OssService;
import cn.beau.dto.FileInfoDto;
import cn.beau.exception.BizException;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 七牛云OSS
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Component
@ConditionalOnProperty(name = "oss.qiniu.enable", havingValue = "true")
public class QiniuOssServiceImpl implements OssService, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(QiniuOssServiceImpl.class);
    @Value("${oss.qiniu.bucket}")
    private String bucket;
    @Value("${oss.qiniu.access-key}")
    private String accessKey;
    @Value("${oss.qiniu.secret-key}")
    private String secretKey;
    @Value("${oss.qiniu.domain:qiniu.com}")
    private String domain;

    //构造一个带指定 Region 对象的配置类
    private Configuration cfg = new Configuration(Region.region0());
    private UploadManager uploadManager = new UploadManager(cfg);
    private Auth auth;

    @Override
    public FileInfoDto upload(byte[] data, long size, String fileName) {
        InputStream input = new ByteArrayInputStream(data);
        return upload(input, size, fileName);
    }

    @Override
    public FileInfoDto upload(InputStream data, long size, String fileName) {
        try {
            Response response = uploadManager.put(data, fileName, auth.uploadToken(bucket), null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);
            FileInfoDto fileInfoDto = new FileInfoDto();
            fileInfoDto.setFileName(fileName);
            fileInfoDto.setMd5(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            logger.error("上传文件失败,result:{}", r, ex);
        }
        return null;
    }

    @Override
    public String getViewUrl(String name) {
        int expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        return getViewUrl(name, expireInSeconds);
    }

    @Override
    public String getViewUrl(String name, int timeout) {
        try {
            DownloadUrl url = new DownloadUrl(domain, Boolean.TRUE, name);
            // 带有效期
            String urlString = url.buildURL(auth, timeout);
            return urlString;
        } catch (Exception e) {
            logger.error("getViewUrl", e);
        }
        return null;
    }

    @Override
    public boolean delFile(String fileName) {
        throw new BizException("未实现");
    }

    @Override
    public void afterPropertiesSet() {
        this.auth = Auth.create(accessKey, secretKey);
    }

    @Override
    public void destroy() {
    }
}
