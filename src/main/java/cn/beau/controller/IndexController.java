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

package cn.beau.controller;

import cn.beau.anno.StaticTag;
import cn.beau.base.BasePage;
import cn.beau.component.TemplateComponent;
import cn.beau.dto.query.ArticleQuery;
import cn.beau.dto.query.TopicQuery;
import cn.beau.dto.response.ArticleSimplePage;
import cn.beau.dto.response.TopicVo;
import cn.beau.enums.ArticleFlagEnum;
import cn.beau.enums.SliderTypeEnum;
import cn.beau.enums.TopicTypeEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * 页面
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Controller
public class IndexController extends CommonController {
    @Autowired
    private TemplateComponent templateComponent;

    @GetMapping(value = {"/", "/index.html"})
    @StaticTag(tpl = "index.html")
    public String index(ModelMap modelMap) {
        setTitle(modelMap, "首页");
        //分组文章
        List<JSONObject> result = new ArrayList<>();
        TopicQuery topicQuery = new TopicQuery();
        topicQuery.setTopicType(TopicTypeEnum.ARTICLE);
        List<TopicVo> topicVos = topicManager.list(topicQuery, false);
        if (!CollectionUtils.isEmpty(topicVos)) {
            topicVos.forEach(it -> {
                ArticleQuery articleQuery = new ArticleQuery();
                articleQuery.setPageSize(10);
                articleQuery.setTopicId(it.getId());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("topicTitle", it.getTopicName());
                jsonObject.put("topicId", it.getId());
                jsonObject.put("data", articleManager.queryRelateList(articleQuery, Boolean.TRUE));
                result.add(jsonObject);
            });
        }
        modelMap.put("articleGroup", result);
        // 最近的文章
        getLastArticle(modelMap, TopicTypeEnum.ARTICLE, null);
        // 主题列表
        getTopicList(modelMap, TopicTypeEnum.ARTICLE);
        // 广告
        getAdList(modelMap, SliderTypeEnum.INDEX);
        // 链接
        getLinkList(modelMap);
        // 轮播
        modelMap.put("sliderList", sliderManager.queryByPosition(SliderTypeEnum.NAV));
        // 所有的标签
        getAllLabel(modelMap);
        // 轮播右侧推荐文章
        modelMap.put("articleList", sliderManager.queryByPosition(SliderTypeEnum.NAV_TUI));
        return "index";
    }

    @GetMapping("/search.html")
    public String search(ModelMap modelMap, String key) {
        setTitle(modelMap, "搜索");
        this.searchData(modelMap, key, 1);
        modelMap.put("key", key);
        getTopicList(modelMap, TopicTypeEnum.ARTICLE);
        getAllLabel(modelMap);
        getHotArticle(modelMap);
        return "search";
    }

    @GetMapping("/searchMore.html")
    @ResponseBody
    public String searchMore(ModelMap modelMap, HttpServletRequest request) {
        String key = request.getParameter("key");
        String page = request.getParameter("page");
        this.searchData(modelMap, key, Integer.valueOf(page));
        return templateComponent.generateString(modelMap, "./fragment/articleModule.html");
    }

    /**
     * 搜索数据列表
     *
     * @param modelMap
     * @param key
     * @param page
     */
    private void searchData(ModelMap modelMap, String key, Integer page) {
        ArticleQuery query = new ArticleQuery();
        query.setTitle(key);
        query.setPageNumber(page);
        BasePage<ArticleSimplePage> pageData = articleManager.querySimpleArticlePage(query);
        modelMap.put("articleList", pageData.getList());
        modelMap.put("articleTotal", pageData.getTotalRow());
    }

    @GetMapping("/robots.txt")
    public void robotsTxt(HttpServletResponse response) throws IOException {
        Writer writer = response.getWriter();
        String lineSeparator = System.getProperty("line.separator", "\n");
        writer.append("User-agent: *").append(lineSeparator);
        writer.append("Disallow:").append("/auth").append(lineSeparator);
        writer.append("Disallow:").append("/config").append(lineSeparator);
        writer.append("Disallow:").append("/file").append(lineSeparator);
        writer.append("Disallow:").append("/tag").append(lineSeparator);
        writer.append("Disallow:").append("/slider").append(lineSeparator);
        writer.append("Disallow:").append("/statistics").append(lineSeparator);
        writer.append("Disallow:").append("/task").append(lineSeparator);
        writer.append("Disallow:").append("/topic").append(lineSeparator);
        writer.append("Disallow:").append("/user").append(lineSeparator);

    }
}
