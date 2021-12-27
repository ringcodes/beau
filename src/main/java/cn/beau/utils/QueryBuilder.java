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

package cn.beau.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.beau.base.BaseQuery;
import org.springframework.util.StringUtils;
/**
 * 查询工具类
 *
 * @author liushilin
 * @date 2021/12/15
 */
public class QueryBuilder {
    private QueryWrapper queryWrapper;
    private String limit;

    public QueryBuilder(BaseQuery baseQuery) {
        this.queryWrapper = new QueryWrapper();
        this.limit = "limit " + baseQuery.getStart() + "," + baseQuery.getPageSize();
    }

    public QueryBuilder() {
        this.queryWrapper = new QueryWrapper();
    }

    public static QueryBuilder init(BaseQuery baseQuery) {
        return new QueryBuilder(baseQuery);
    }

    public static QueryBuilder init() {
        return new QueryBuilder();
    }

    public QueryBuilder eq(String col, Long val) {
        if (StringUtils.isEmpty(val)) {
            return this;
        }
        if (val > 0) {
            queryWrapper.eq(col, val);
        }
        return this;
    }

    public QueryBuilder eq(String col, Object val) {
        if (StringUtils.isEmpty(val)) {
            return this;
        }
        queryWrapper.eq(col, val);
        return this;
    }

    public QueryBuilder ne(String col, Object val) {
        if (!StringUtils.isEmpty(val)) {
            queryWrapper.ne(col, val);
        }
        return this;
    }

    public QueryBuilder le(String col, Object val) {
        if (!StringUtils.isEmpty(val)) {
            queryWrapper.le(col, val);
        }
        return this;
    }

    public QueryBuilder ge(String col, Object val) {
        if (!StringUtils.isEmpty(val)) {
            queryWrapper.ge(col, val);
        }
        return this;
    }

    public QueryBuilder like(String col, Object val) {
        if (!StringUtils.isEmpty(val)) {
            queryWrapper.like(col, val);
        }
        return this;
    }

    public QueryBuilder orderByAsc(String... col) {
        if (!StringUtils.isEmpty(col)) {
            queryWrapper.orderByAsc(col);
        }
        return this;
    }

    public QueryBuilder in(String col, Object... val) {
        if (!StringUtils.isEmpty(col)) {
            queryWrapper.in(col, val);
        }
        return this;
    }

    public QueryBuilder notIn(String col, Object... val) {
        if (!StringUtils.isEmpty(col)) {
            queryWrapper.notIn(col, val);
        }
        return this;
    }

    public QueryBuilder between(String col, Object val, Object val2) {
        if (!StringUtils.isEmpty(col)) {
            queryWrapper.between(col, val, val2);
        }
        return this;
    }

    public QueryBuilder orderByDesc(String... col) {
        if (!StringUtils.isEmpty(col)) {
            queryWrapper.orderByDesc(col);
        }
        return this;
    }

    public QueryBuilder select(String... col) {
        queryWrapper.select(col);
        return this;
    }

    public QueryWrapper buildCount() {
        return this.queryWrapper;
    }

    public QueryWrapper build() {
        if (StringUtils.hasText(limit)) {
            this.queryWrapper.last(limit);
        }
        return this.queryWrapper;
    }
}
