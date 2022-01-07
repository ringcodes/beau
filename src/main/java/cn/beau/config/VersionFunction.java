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
package cn.beau.config;

import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 版本号
 */
public class VersionFunction implements Function {
    private Map<String, String> versionMap = new ConcurrentHashMap<>();
    private final static String DELIMITER = "?";

    @Override
    public String call(Object[] paras, Context ctx) {
        String path = String.valueOf(paras[0]);
        try {
            String pathVersion = versionMap.get(path);
            if (!StringUtils.hasText(pathVersion)) {
                pathVersion = md5(path);
                versionMap.put(path, pathVersion);
            }
            StringJoiner sj = new StringJoiner(DELIMITER);
            sj.add(path);
            sj.add(pathVersion);
            return sj.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private String md5(String path) {
        try {
            ClassPathResource cpr = new ClassPathResource(path);
            byte[] content = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            return DigestUtils.md5DigestAsHex(content);
        } catch (Exception e) {
            return null;
        }
    }
}
