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

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import cn.beau.base.LoginUser;
import cn.beau.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
/**
 * JWT处理
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Slf4j
public class JwtUtil {
    private static final String SECRET = "9a96349e2345385785e804e0f4254dee";
    private static final String ISSUER = "admin";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);
    private static final JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();

    public static String sign(LoginUser userInfo) {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            localDateTime = localDateTime.plusHours(3);
            //创建jwt
            JWTCreator.Builder builder = JWT.create().
                withIssuer(ISSUER). //发行人
                withExpiresAt(dateTime2Date(localDateTime)); //过期时间点
            //传入参数
            builder.withClaim("id", userInfo.getId());
            builder.withClaim("name", userInfo.getName());
            builder.withClaim("role", userInfo.getRole());
            //签名加密
            return builder.sign(algorithm);
        } catch (Exception e) {
            log.error("verify", e);
            throw new BizException("sign", e);
        }
    }

    public static LoginUser verify(String token) throws RuntimeException {
        try {
            //解密
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> map = jwt.getClaims();
            LoginUser userInfo = new LoginUser();
            userInfo.setId(map.get("id").asLong());
            userInfo.setName(map.get("name").asString());
            userInfo.setRole(map.get("role").asInt());
            return userInfo;
        } catch (Exception e) {
            log.debug("verify", e);
            return null;
        }
    }

    private static Date dateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }
}
