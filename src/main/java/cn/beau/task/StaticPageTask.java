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

package cn.beau.task;

import cn.beau.anno.StaticTag;
import cn.beau.component.SpringComponent;
import cn.beau.component.TemplateComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 静态化页面
 * 使用注释 {@link StaticTag} 标记的页面会定时静态化
 *
 * @author liushilin
 * @date 2021/12/8
 */
@Component
@Slf4j
public class StaticPageTask implements ITask<Void> {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private SpringComponent springComponent;
    @Autowired
    private TemplateComponent templateComponent;

    @Scheduled(cron = "0 0/30 * * * ?")
    public Void doRun() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> handlerMethodEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = handlerMethodEntry.getValue();
            Method method = handlerMethod.getMethod();
            StaticTag staticTag = method.getAnnotation(StaticTag.class);
            if (staticTag != null) {
                try {
                    ModelMap modelMap = new ModelMap();
                    ReflectionUtils.invokeMethod(method, springComponent.getBean(handlerMethod.getBean().toString()), modelMap);
                    templateComponent.generate(modelMap, staticTag.tpl());
                } catch (Exception e) {
                    log.error("static template file,method:{}", method.getName(), e);
                }
            }
        }
        return null;
    }

    @Override
    public String name() {
        return "articleGroup";
    }
}
