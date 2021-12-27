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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

/**
 * jdk8 获取当天，本周，本月，本季度，本年起始时间工具类
 *
 * @author liushilin
 * @date 2021/12/15
 */
public class DateUtil {

    public static final int CURRENT = 0;

    public static final int NEXT = 1;

    public static final int PREVIOUS = -1;

    public static final int QUARTER = 3;

    /**
     * 获取某天的开始日期
     *
     * @param offset 0今天，1明天，-1昨天，依次类推
     * @return
     */
    public static LocalDateTime dayStart(int offset) {

        return LocalDateTime.of(LocalDate.now().plusDays(offset), LocalTime.MIN);
    }

    /**
     * 获取某周的开始日期
     *
     * @param offset 0本周，1下周，-1上周，依次类推
     * @return
     */
    public static LocalDateTime weekStart(int offset) {

        return LocalDateTime.now().plusWeeks(offset).with(DayOfWeek.MONDAY).minusDays(NEXT);
    }

    /**
     * 获取某月的开始日期
     *
     * @param offset 0本月，1下个月，-1上个月，依次类推
     * @return
     */
    public static LocalDateTime monthStart(int offset) {
        return LocalDateTime.now().plusMonths(offset).with(TemporalAdjusters.firstDayOfMonth()).minusDays(NEXT);
    }

    public static LocalDateTime monthEnd(int offset) {
        return LocalDateTime.now().plusMonths(offset).with(TemporalAdjusters.lastDayOfMonth()).minusDays(NEXT);
    }


    /**
     * 获取某季度的开始日期
     * 季度一年四季， 第一季度：2月-4月， 第二季度：5月-7月， 第三季度：8月-10月， 第四季度：11月-1月
     *
     * @param offset 0本季度，1下个季度，-1上个季度，依次类推
     * @return
     */
    public static LocalDateTime quarterStart(int offset) {
        final LocalDateTime date = LocalDateTime.now().plusMonths(offset * 3);
        int month = date.getMonth().getValue();//当月
        int start = 0;
        if (month >= 2 && month <= 4) {//第一季度
            start = 2;
        } else if (month >= 5 && month <= 7) {//第二季度
            start = 5;
        } else if (month >= 8 && month <= 10) {//第三季度
            start = 8;
        } else if ((month >= 11 && month <= 12)) {//第四季度
            start = 11;
        } else if (month == 1) {//第四季度
            start = 11;
            month = 13;
        }
        return date.plusMonths(start - month).with(TemporalAdjusters.firstDayOfMonth()).minusDays(NEXT);
    }

    /**
     * 获取某年的开始日期
     *
     * @param offset 0今年，1明年，-1去年，依次类推
     * @return
     */
    public static LocalDateTime yearStart(int offset) {
        return LocalDateTime.now().plusYears(offset).with(TemporalAdjusters.firstDayOfYear());
    }

}
