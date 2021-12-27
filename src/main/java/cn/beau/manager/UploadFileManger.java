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
import cn.beau.dto.query.UploadFileQuery;
import cn.beau.repository.mapper.UploadFileMapper;
import cn.beau.repository.model.UploadFileEntity;
import cn.beau.utils.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 上传文件
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Service
public class UploadFileManger {
    @Autowired
    private UploadFileMapper uploadFileMapper;

    public UploadFileEntity getUploadFileById(Long id) {
        return uploadFileMapper.selectById(id);
    }

    public long save(UploadFileEntity uploadFile) {
        uploadFile.setCreateId(uploadFile.getUpdateId());
        uploadFileMapper.insert(uploadFile);
        return uploadFile.getId();
    }

    public BasePage<UploadFileEntity> queryUploadFilePage(UploadFileQuery uploadFile) {
        QueryBuilder queryBuilder = QueryBuilder.init(uploadFile);
        long count = uploadFileMapper.selectCount(queryBuilder.buildCount());
        BasePage page = new BasePage();
        page.setPageNumber(uploadFile.getPageNumber());
        if (count > 0) {
            List<UploadFileEntity> data = uploadFileMapper.selectList(queryBuilder.build());
            page.setList(data);
            page.setTotalRow(count);
        }
        return page;
    }

    public boolean delUploadFile(Long id, Long updateId) {
        UploadFileEntity UploadFile = new UploadFileEntity();
        UploadFile.setId(id);
        UploadFile.setDeleted(1);
        UploadFile.setUpdateId(updateId);
        return uploadFileMapper.updateById(UploadFile) > 0;
    }
}
