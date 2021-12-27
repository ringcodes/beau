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

import cn.beau.base.ResultObject;
import cn.beau.base.ResultUtil;
import cn.beau.task.ITask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 定时任务
 *
 * @author liushilin
 * @date 2021/12/15
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private List<ITask> taskList;

    @GetMapping("/{taskName}")
    public ResultObject doRun(@PathVariable String taskName){
        taskList.forEach(it -> {
            if (it.name().equals(taskName)){
                it.doRun();
            }
        });
        return ResultUtil.newSucc();
    }
}
