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

import cn.beau.base.LoginUser;
import cn.beau.base.ResultObject;
import cn.beau.base.ResultUtil;
import cn.beau.component.oss.OssService;
import cn.beau.dto.FileInfoDto;
import cn.beau.enums.UploadSourceEnum;
import cn.beau.manager.UploadFileManger;
import cn.beau.repository.model.UploadFileEntity;
import cn.beau.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 文件
 *
 * @author liushilin
 * @date 2021/12/15
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private OssService ossService;
    @Autowired
    private UploadFileManger uploadFileManger;


    @PostMapping("/upload")
    public ResultObject upload(LoginUser loginUser, @RequestParam("file") MultipartFile file, @RequestParam String source, @RequestParam String code) throws IOException {
        UploadSourceEnum uploadSourceEnum = UploadSourceEnum.valueOf(source);
        Integer index = file.getOriginalFilename().lastIndexOf(".");
        String filename = file.getOriginalFilename();
        if (index != -1 && index != 0) {
            LocalDateTime now = LocalDateTime.now();
            filename = filename.substring(0, index) + "-" + DateUtil.timeFormatFull(now) + filename.substring(index);
        }
        // 上传
        FileInfoDto info = ossService.upload(file.getBytes(), file.getSize(), filename);
        //入库
        UploadFileEntity uploadFile = new UploadFileEntity();
        uploadFile.setSource(uploadSourceEnum.getType());
        uploadFile.setFilePath(info.getFileName());
        uploadFile.setFileName(file.getOriginalFilename());
        uploadFile.setFileSize(file.getSize());
        uploadFile.setMd5(info.getMd5());
        uploadFile.setUpdateId(loginUser.getId());
        Long id = uploadFileManger.save(uploadFile);
        info.setFileId(id);
        return ResultUtil.newSucc(info);
    }
}
