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

import cn.beau.dto.ArticleListDto;
import cn.beau.dto.query.ArticleQuery;
import cn.beau.dto.response.LabelListVo;
import cn.beau.enums.SliderTypeEnum;
import cn.beau.enums.TopicPositionEnum;
import cn.beau.enums.TopicTypeEnum;
import cn.beau.manager.ArticleManager;
import cn.beau.manager.ConfigManager;
import cn.beau.manager.LabelManager;
import cn.beau.manager.SliderManager;
import cn.beau.manager.TopicManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 公共方法
 *
 * @author liushilin
 * @date 2021/12/15
 */
public class CommonController {
    @Autowired
    protected ArticleManager articleManager;
    @Autowired
    protected ConfigManager configManager;
    @Autowired
    protected TopicManager topicManager;
    @Autowired
    protected LabelManager labelManager;
    @Autowired
    protected SliderManager sliderManager;
    @Value("${web.title:'个人网'}")
    private String webTitle;

    protected void setTitle(ModelMap modelMap, String title) {
        modelMap.put("webTitle", title);
        modelMap.put("webName", webTitle);
        // 菜单
        modelMap.put("menuList", topicManager.listTopicPos(TopicPositionEnum.MENU, Boolean.FALSE));
    }

    protected void getLastArticle(ModelMap modelMap, TopicTypeEnum topicTypeEnum, Long topicId) {
        ArticleQuery article = new ArticleQuery();
        if (topicId != null) {
            article.setTopicId(topicId);
        }
        article.setTopicType(topicTypeEnum.getType());
        article.setPageSize(10);
        List<ArticleListDto> list = articleManager.queryRelateList(article, true);
        if (topicTypeEnum.equals(TopicTypeEnum.ARTICLE)) {
            modelMap.put("relateList", list);
        }
    }

    protected void getHotArticle(ModelMap modelMap) {
        ArticleQuery article = new ArticleQuery();
        article.setTopicType(TopicTypeEnum.ARTICLE.getType());
        article.setPageSize(6);
        List<ArticleListDto> list = articleManager.queryRelateList(article, false);
        modelMap.put("hotList", list);
    }

    protected void getTopicList(ModelMap modelMap, TopicTypeEnum topicTypeEnum) {
        modelMap.put("topicList", topicManager.listTopicPos(TopicPositionEnum.INDEX, Boolean.TRUE));
    }

    protected void getAdList(ModelMap modelMap, SliderTypeEnum sliderTypeEnum) {
        modelMap.put("adList", sliderManager.queryByPosition(sliderTypeEnum));
    }

    protected void getLinkList(ModelMap modelMap) {
        modelMap.put("linkList", configManager.getLinkList());
    }

    protected int getPage(HttpServletRequest request) {
        String page = request.getParameter("p");
        if (StringUtils.isNotBlank(page)) {
            return Integer.valueOf(page);
        }
        return 1;
    }

    protected void getAllLabel(ModelMap modelMap) {
        List<LabelListVo> list = labelManager.listAll();
        modelMap.put("labelList", list);
    }

    protected void getSliderTui(ModelMap modelMap) {
        ArticleQuery articleQuery = new ArticleQuery();
        articleQuery.setPageSize(6);
        modelMap.put("tuiList", articleManager.querySimpleArticlePage(articleQuery).getList());
    }
}
