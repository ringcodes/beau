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
import cn.beau.dto.query.SliderQuery;
import cn.beau.dto.response.SliderResp;
import cn.beau.enums.SliderTypeEnum;
import cn.beau.repository.mapper.SliderMapper;
import cn.beau.repository.model.SliderEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 轮播管理
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Service
public class SliderManager {
    @Autowired
    private OssService ossService;
    @Autowired
    private SliderMapper sliderMapper;

    public SliderEntity getSliderById(Long id) {

        return sliderMapper.selectById(id);
    }

    public boolean saveAndUpdate(SliderEntity slider) {
        if (slider.getId() != null && slider.getId()> 0){
            return sliderMapper.updateById(slider) > 0;
        } else {
            slider.setCreateId(slider.getUpdateId());
            return sliderMapper.insert(slider) > 0;
        }

    }

    public BasePage<SliderResp> querySliderPage(SliderQuery slider) {
        QueryWrapper queryWrapper = new QueryWrapper();
        BasePage<SliderResp> page = new BasePage();
        page.setPageSize(slider.getPageSize());
        page.setPageNumber(slider.getPageNumber());
        queryWrapper.eq("slider_status",0);
        queryWrapper.eq("deleted",0);
        if (slider.getSliderType() != null) {
            queryWrapper.eq("slider_type", slider.getSliderType().getCode());
        }
        Integer count = sliderMapper.selectCount(queryWrapper);
        page.setTotalRow(count);
        if (count > 0) {
            List<SliderEntity> list = sliderMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(list)) {
                List<SliderResp> result = new ArrayList<>(list.size());
                list.forEach(it -> result.add(buildSliderResp(it)));
                page.setList(result);
            }
        }
        return page;
    }

    private SliderResp buildSliderResp(SliderEntity it) {
        SliderResp sliderResp = new SliderResp();
        sliderResp.setId(it.getId());
        sliderResp.setTitle(it.getTitle());
        sliderResp.setPic(ossService.getViewUrl(it.getPic()));
        sliderResp.setPosition(it.getSliderType());
        sliderResp.setPositionName(SliderTypeEnum.ofCode(it.getSliderType()).getDesc());
        sliderResp.setTarget(it.getTarget());
        sliderResp.setSliderStatus(it.getStatus());
        sliderResp.setUpdateTime(it.getUpdateTime());
        return sliderResp;
    }

    public boolean delSlider(Long id, Long updateId) {
        SliderEntity entity = new SliderEntity();
        entity.setId(id);
        entity.setUpdateId(updateId);
        entity.setDeleted(1);
        return sliderMapper.updateById(entity) > 0;
    }

    public List<SliderResp> queryByPosition(SliderTypeEnum positionEnum) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("slider_type", positionEnum.getCode());
        queryWrapper.eq("slider_status",0);
        List<SliderEntity> list = sliderMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            List<SliderResp> result = new ArrayList<>();
            list.forEach(it -> result.add(buildSliderResp(it)));
            return result;
        }
        return Collections.emptyList();
    }
}
