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

package cn.beau.controller.admin;

import cn.beau.anno.AuthTag;
import cn.beau.base.LoginUser;
import cn.beau.base.ResultObject;
import cn.beau.base.ResultUtil;
import cn.beau.dto.query.SliderQuery;
import cn.beau.dto.request.SliderRequest;
import cn.beau.enums.RoleEnum;
import cn.beau.enums.SliderTypeEnum;
import cn.beau.manager.SliderManager;
import cn.beau.repository.model.SliderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 轮播
 *
 * @author liushilin
 * @date 2021/12/15
 */
@RestController
@RequestMapping("/slider")
public class SliderController {

    @Autowired
    private SliderManager sliderManager;

    @GetMapping("/list")
    @AuthTag(role = {RoleEnum.PRE_ADMIN})
    public ResultObject list(SliderQuery config) {
        return ResultUtil.newSucc(sliderManager.querySliderPage(config));
    }

    @PostMapping("/save")
    @AuthTag(role = RoleEnum.ADMIN)
    public ResultObject save(LoginUser userLoginDto, @RequestBody SliderRequest sliderRequest) {
        SliderEntity slider = new SliderEntity();
        if (sliderRequest.getSliderType() != null){
            slider.setSliderType(sliderRequest.getSliderType().getCode());
        }
        slider.setId(sliderRequest.getId());
        slider.setPic(sliderRequest.getPic());
        if (sliderRequest.getStatus() != null){
            slider.setStatus(sliderRequest.getStatus().getCode());
        }
        slider.setTarget(sliderRequest.getTarget());
        slider.setTitle(sliderRequest.getTitle());
        slider.setUpdateId(userLoginDto.getId());
        return ResultUtil.newSucc(sliderManager.saveAndUpdate(slider));
    }

    @DeleteMapping("/del/{id}")
    @AuthTag(role = RoleEnum.ADMIN)
    public ResultObject del(LoginUser userLoginDto, @PathVariable Long id) {
        return ResultUtil.newSucc(sliderManager.delSlider(id, userLoginDto.getId()));
    }


    @RequestMapping("/get/{id}")
    @AuthTag(role = RoleEnum.ADMIN)
    public ResultObject get(@PathVariable Long id) {
        return ResultUtil.newSucc(sliderManager.getSliderById(id));
    }

    @RequestMapping("/listPosition")
    public ResultObject listPosition() {
        return ResultUtil.newSucc(SliderTypeEnum.getAll());
    }
}
