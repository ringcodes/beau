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

package cn.beau.repository.mapper;

import cn.beau.dto.request.StaticsRequest;
import cn.beau.dto.response.StaticsChartVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaticsMapper {

    @Select("SELECT count(1) as val,DATE_FORMAT(create_time,'%Y-%m-%d') as title  from t_article" +
        " where create_time > #{createTimeStart} and create_time < #{createTimeEnd}" +
        " GROUP BY DATE_FORMAT(create_time,'%Y-%m-%d') ")
    List<StaticsChartVo> qushiList(StaticsRequest staticsRequest);
}
