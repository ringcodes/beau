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

package cn.beau.component;

import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Template;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

/**
 * 模板工具类
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Component
@Slf4j
public class TemplateComponent {
    @Autowired
    private BeetlGroupUtilConfiguration configuration;

    public String generateString(Map<String, Object> attr, String tpl) {
        try {
            Template template = configuration.getGroupTemplate().getTemplate(tpl);
            template.binding(attr);
            return template.render();
        } catch (Exception e) {
            log.error("generate fail", e);
        }
        return null;
    }

    public void generate(Map<String, Object> attr, String tpl) {
        try {
            Template template = configuration.getGroupTemplate().getTemplate(tpl);
            // 输出流
            File dest = new File(getFilePath(tpl));
            if (dest.exists()) {
                dest.delete();
            }
            if (!dest.getParentFile().exists()) {
                if (!dest.getParentFile().mkdirs()) {
                    log.error("创建目标文件所在目录失败！");
                }
            }
            attr.put("curMenu", "");
            template.binding(attr);
            FileWriter fileWriter = new FileWriter(dest);
            template.renderTo(fileWriter);
        } catch (Exception e) {
            log.error("create fail", e);
        }
    }

    public Boolean exist(String tpl) {
        File dest = new File(getFilePath(tpl));
        return dest.exists();
    }

    public String getFilePath(String tpl) {
        ApplicationHome home = new ApplicationHome(getClass());
        File jarFile = home.getSource();
        return jarFile.getParentFile().toString() + "/temp/" + tpl;
    }
}
