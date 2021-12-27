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
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.AnonymousCOSCredentials;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 腾讯OSS
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Component
@ConditionalOnProperty(name = "oss.tencent.enable", havingValue = "true")
public class TencentOssServiceImpl implements OssService, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(TencentOssServiceImpl.class);

    @Value("${oss.tencent.bucket}")
    private String bucket;
    @Value("${oss.tencent.access-key}")
    private String accessKey;
    @Value("${oss.tencent.secret-key}")
    private String secretKey;
    @Value("${oss.tencent.region-name}")
    private String regionName;

    private COSClient cosClient;
    private COSClient anonymousCosClient;

    @Override
    public FileInfoDto upload(byte[] data, long size, String fileName) {
        InputStream input = new ByteArrayInputStream(data);
        return upload(input, size, fileName);
    }

    @Override
    public FileInfoDto upload(InputStream data, long size, String fileName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, data, objectMetadata);
        putObjectRequest.setStorageClass(StorageClass.Standard);
        try {
            PutObjectResult result = cosClient.putObject(putObjectRequest);
            FileInfoDto fileInfoVo = new FileInfoDto();
            fileInfoVo.setFileSize(size);
            fileInfoVo.setFileName(fileName);
            fileInfoVo.setPreviewUrl(getViewUrl(fileName));
            fileInfoVo.setMd5(result.getContentMd5());
            return fileInfoVo;
        } catch (CosServiceException e) {
            logger.error("", e);
        } catch (CosClientException e) {
            logger.error("", e);
        } finally {
            IOUtils.closeQuietly(data);
        }
        return null;
    }

    @Override
    public String getViewUrl(String name) {
        try {
            if (StringUtils.isBlank(name)) {
                return null;
            }
            if (name.startsWith("http") || name.startsWith("https")) {
                return name;
            }
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, name, HttpMethodName.GET);
            URL url = anonymousCosClient.generatePresignedUrl(req);
            return url.toString();
        } catch (Exception e) {
            logger.error("getViewUrl", e);
        }
        return null;
    }

    @Override
    public String getViewUrl(String name, int timeout) {
        try {
            GeneratePresignedUrlRequest req =
                    new GeneratePresignedUrlRequest(bucket, name, HttpMethodName.GET);
            Date expirationDate = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
            req.setExpiration(expirationDate);
            URL url = anonymousCosClient.generatePresignedUrl(req);
            return url.toString();
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    @Override
    public boolean delFile(String fileName) {
        try {
            cosClient.deleteObject(bucket, fileName);
        } catch (CosServiceException e) {
            logger.error("", e);
        } catch (CosClientException e) {
            logger.error("", e);
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() {
        if (cosClient == null) {
            COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
            ClientConfig clientConfig = new ClientConfig(new Region(regionName));
            clientConfig.setHttpProtocol(HttpProtocol.https);
            cosClient = new COSClient(cred, clientConfig);
        }
        if (anonymousCosClient == null) {
            COSCredentials cosCredentials = new AnonymousCOSCredentials();
            ClientConfig anonymousCosConfig = new ClientConfig(new Region(regionName));
            anonymousCosConfig.setHttpProtocol(HttpProtocol.https);
            anonymousCosClient = new COSClient(cosCredentials, anonymousCosConfig);
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);
        // 设置高级接口的分块上传阈值和分块大小为10MB
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(10 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(10 * 1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);
    }

    @Override
    public void destroy() {
        cosClient.shutdown();
        anonymousCosClient.shutdown();
    }
}
