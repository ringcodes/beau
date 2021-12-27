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

import cn.beau.component.TemplateComponent;
import cn.beau.dto.ArticleListDto;
import cn.beau.dto.query.ArticleQuery;
import cn.beau.dto.response.ArticleDetailVo;
import cn.beau.enums.SliderTypeEnum;
import cn.beau.enums.TopicTypeEnum;
import cn.beau.repository.model.LabelEntity;
import cn.beau.repository.model.TopicEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("articleWeb")
@RequestMapping("/article")
public class ArticleController extends CommonController {
    @Autowired
    private TemplateComponent templateComponent;

    @GetMapping(value = {"/tag/{id}.html"})
    public String tag(ModelMap modelMap, HttpServletRequest request, @PathVariable(required = false) Long id) {
        setTitle(modelMap, "标签文章");
        modelMap.put("articleList", articleManager.queryByLabel(new ArticleQuery()));
        modelMap.put("page", getPage(request));
        LabelEntity label = labelManager.getById(id);
        modelMap.put("labelName", label.getName());
        modelMap.put("labelId", label.getId());
        getTopicList(modelMap, TopicTypeEnum.ARTICLE);
        getAdList(modelMap, SliderTypeEnum.LIST);
        getAllLabel(modelMap);
        // 推荐
        getSliderTui(modelMap);
        //
        getHotArticle(modelMap);
        return "article/label";
    }

    @GetMapping("/{idStr}.html")
    public String detail(@PathVariable String idStr, ModelMap modelMap) {
        ArticleDetailVo article = articleManager.getArticleAndContent(Long.valueOf(idStr));
        modelMap.put("article", article);
        setTitle(modelMap, article.getTitle());
        modelMap.put("meta_keys", article.getSeoKeys());
        modelMap.put("meta_desc", StringUtils.hasText(article.getSeoDesc()) ? article.getSeoDesc() : article.getDescription());
        getLastArticle(modelMap, TopicTypeEnum.ARTICLE, article.getTopicId());
        getTopicList(modelMap, TopicTypeEnum.ARTICLE);
        getAdList(modelMap, SliderTypeEnum.DETAIL);
        getAllLabel(modelMap);
        // 推荐
        getSliderTui(modelMap);
        // 热门
        getHotArticle(modelMap);
        return "article/detail";
    }

    @PostMapping("/loadMore.html")
    @ResponseBody
    public String loadMore(ArticleQuery query) {
        Map<String, Object> data = new HashMap<>();
        if (query.getTopicId() != null) {
            data.put("articleList", articleManager.queryRelateList(query, Boolean.TRUE));
        } else if (query.getLabelId() != null) {
            data.put("articleList", articleManager.queryByLabel(query));
        }
        return templateComponent.generateString(data, "./fragment/articleModule.html");
    }

    @GetMapping(value = {"/t/{topicId}.html"})
    public String article(ModelMap modelMap, HttpServletRequest request, @PathVariable Long topicId) {
        //ad
        modelMap.put("slider", new ArrayList<>());
        ArticleQuery query = new ArticleQuery();
        String topicName = "";
        if (topicId != null) {
            TopicEntity topic = topicManager.getTopicById(topicId);
            query.setTopicId(topicId);
            topicName = topic.getTopicName();
        }
        setTitle(modelMap, topicName);
        modelMap.put("topicName", topicName);
        modelMap.put("topicId", topicId);

        query.setTopicType(TopicTypeEnum.ARTICLE.getType());
        query.setPageNumber(getPage(request));
        query.setSortColumn("create_time");
        query.setSortDesc("desc");
        List<ArticleListDto> pageData = articleManager.queryRelateList(query, Boolean.TRUE);
        modelMap.put("articleList", pageData);
        modelMap.put("page", getPage(request));
        getTopicList(modelMap, TopicTypeEnum.ARTICLE);
        getAdList(modelMap, SliderTypeEnum.LIST);
        getAllLabel(modelMap);
        // 推荐
        getSliderTui(modelMap);
        //
        getHotArticle(modelMap);
        return "article/article";
    }
}
