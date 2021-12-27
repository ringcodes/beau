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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;
/**
 * spring工具类
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Component
public class SpringComponent implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    /**
     * 对象名称获取spring bean对象
     *
     * @param name
     * @return
     * @throws BeansException
     */
    public Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 对象名称获取spring bean对象
     *
     * @param annotation
     * @return
     * @throws BeansException
     */
    public Map<String, Object> getBeanByAnnotation(Class<? extends Annotation> annotation) throws BeansException {
        return applicationContext.getBeansWithAnnotation(annotation);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringComponent.applicationContext = applicationContext;
    }
}
