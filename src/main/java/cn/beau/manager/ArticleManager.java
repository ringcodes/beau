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

package cn.beau.manager;

import cn.beau.base.BasePage;
import cn.beau.component.oss.OssService;
import cn.beau.dto.ArticleListDto;
import cn.beau.dto.converts.ArticleConvert;
import cn.beau.dto.query.ArticleQuery;
import cn.beau.dto.request.ArticleRequest;
import cn.beau.dto.response.ArticleDetailVo;
import cn.beau.dto.response.ArticleLabelVo;
import cn.beau.dto.response.ArticleSimplePage;
import cn.beau.enums.ArticleContentEnum;
import cn.beau.exception.BizException;
import cn.beau.repository.mapper.ArticleMapper;
import cn.beau.repository.model.ArticleEntity;
import cn.beau.utils.MarkdownUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleManager {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleLabelManager articleLabelManager;
    @Autowired
    private LabelManager labelManager;
    @Autowired
    private OssService ossService;

    @Autowired
    private ArticleConvert articleConvert;

    public ArticleDetailVo getArticleAndContent(Long id) {
        ArticleEntity article = new ArticleEntity();
        article.setId(id);
        if (id > 0) {
            ArticleDetailVo query = articleMapper.getArticleAndContent(article);
            List<ArticleLabelVo> articleLabels = labelManager.queryByArticle(Collections.singletonList(query.getId()));
            query.setArticleLabelVos(articleLabels);
            query.setTitlePicView(formatUrl(query.getTitlePic()));
            if (ArticleContentEnum.MARKDOWN.getType().equals(query.getArticleType())) {
                query.setContentMdView(MarkdownUtil.render(query.getContent()));
            } else {
                query.setContentMdView(query.getContent());
            }
            return query;
        } else {
            ArticleDetailVo add = new ArticleDetailVo();
            return add;
        }
    }

    public ArticleEntity saveAndUpdate(ArticleRequest articleRequest) {
        ArticleEntity exist = articleMapper.selectById(articleRequest.getId());
        ArticleEntity entity = new ArticleEntity();
        entity.setId(articleRequest.getId());
        entity.setDescription(articleRequest.getDescription());
        entity.setUpdateId(articleRequest.getUpdateId());
        entity.setTitle(articleRequest.getTitle());
        entity.setTitlePic(articleRequest.getTitlePic());
        if (StringUtils.isNotBlank(articleRequest.getContent())) {
            entity.setContent(articleRequest.getContent());
        }
        entity.setPoints(articleRequest.getPoints());
        if (articleRequest.getFlagType() != null){
            entity.setFlagType(articleRequest.getFlagType().getCode());
        }
        entity.setSourceUrl(articleRequest.getSourceUrl());
        if (articleRequest.getPublishStatus() != null){
            entity.setPublishStatus(articleRequest.getPublishStatus().getCode());
        }
        entity.setTopicId(articleRequest.getTopicId());
        if (articleRequest.getArticleType() != null){
            entity.setArticleType(articleRequest.getArticleType().getType());
        }
        entity.setSeoDesc(articleRequest.getSeoDesc());
        entity.setSeoKeys(articleRequest.getSeoKeys());
        if (exist == null) {
            if (StringUtils.isBlank(articleRequest.getTitle())) {
                throw new BizException("文章标题不能为空");
            }
            if (StringUtils.isBlank(articleRequest.getContent())) {
                throw new BizException("文章内容不能为空");
            }
            if (StringUtils.isBlank(entity.getDescription()) && StringUtils.isNotBlank(articleRequest.getContent())) {
                Integer len = Math.min(articleRequest.getContent().length(), 400);
                entity.setDescription(delHtmlTags(articleRequest.getContent().substring(0, len)));
            }
            entity.setCreateId(articleRequest.getUpdateId());
            articleMapper.insert(entity);
        } else {
            entity.setId(exist.getId());
            if (StringUtils.isBlank(entity.getDescription()) && StringUtils.isBlank(exist.getDescription()) && StringUtils.isNotBlank(articleRequest.getContent())) {
                Integer len = Math.min(articleRequest.getContent().length(), 400);
                entity.setDescription(delHtmlTags(articleRequest.getContent().substring(0, len)));
            }
            articleMapper.updateById(entity);
        }
        articleLabelManager.save(entity.getId(), articleRequest.getTagList(), articleRequest.getUpdateId());
        return entity;
    }

    private String delHtmlTags(String htmlStr) {
        //定义script的正则表达式，去除js可以防止注入
        String scriptRegex = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        //定义style的正则表达式，去除style样式，防止css代码过多时只截取到css样式代码
        String styleRegex = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        //定义HTML标签的正则表达式，去除标签，只提取文字内容
        String htmlRegex = "<[^>]+>";
        //定义空格,回车,换行符,制表符
        String spaceRegex = "\\s*|\t|\r|\n";

        // 过滤script标签
        htmlStr = htmlStr.replaceAll(scriptRegex, "");
        // 过滤style标签
        htmlStr = htmlStr.replaceAll(styleRegex, "");
        // 过滤html标签
        htmlStr = htmlStr.replaceAll(htmlRegex, "");
        // 过滤空格等
        htmlStr = htmlStr.replaceAll(spaceRegex, "");
        return htmlStr.trim(); // 返回文本字符串
    }

    public BasePage<ArticleSimplePage> querySimpleArticlePage(ArticleQuery article) {

        if (StringUtils.isNotBlank(article.getTitle())) {
            article.setTitle(article.getTitle() + "%");
        }
        BasePage<ArticleSimplePage> page = new BasePage();
        page.setPageNumber(article.getPageNumber());
        article.setSortColumn("create_time");
        article.setSortDesc("desc");
        long count = articleMapper.queryArticleSimpleCount(article);
        if (count > 0) {
            List<ArticleListDto> data = articleMapper.queryArticleSimplePage(article);
            List<Long> articleIds = data.stream().map(ArticleListDto::getId).collect(Collectors.toList());
            List<ArticleLabelVo> articleLabels = labelManager.queryByArticle(articleIds);
            Map<Long, List<ArticleLabelVo>> map = articleLabels.stream().collect(Collectors.groupingBy(ArticleLabelVo::getArticleId));
            List<ArticleSimplePage> list = new ArrayList<>(data.size());
            for (ArticleListDto a : data) {
                ArticleSimplePage articleSimplePage = articleConvert.toPage(a);
                articleSimplePage.setTitlePic(ossService.getViewUrl(a.getTitlePic()));
                articleSimplePage.setArticleLabelVos(map.get(a.getId()));
                list.add(articleSimplePage);
            }
            page.setList(list);
            page.setTotalRow(count);
        } else {
            page.setList(Collections.emptyList());
            page.setTotalRow(0);
        }
        return page;
    }

    public boolean delArticle(Long id, Long updateId) {
        ArticleEntity article = new ArticleEntity();
        article.setId(id);
        article.setDeleted(1);
        article.setUpdateId(updateId);
        return articleMapper.updateById(article) > 0;
    }

    public List<ArticleListDto> queryRelateList(ArticleQuery candidate, boolean pic) {
        List<ArticleListDto> list = articleMapper.queryArticleSimplePage(candidate);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(it -> {
                if (pic) {
                    it.setTitlePic(formatUrl(it.getTitlePic()));
                }
            });
            return list;
        }
        return Collections.emptyList();
    }

    private String formatUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return "";
        }
        return ossService.getViewUrl(url);
    }

    public List<ArticleListDto> queryByLabel(ArticleQuery query) {
        List<ArticleListDto> list = articleMapper.queryByLabel(query);
        list.forEach(it -> it.setTitlePic(ossService.getViewUrl(it.getTitlePic())));
        return list;
    }
}
