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

package cn.beau.component.oauth.impl;

import cn.beau.base.LoginUser;
import cn.beau.component.oauth.ConfigDto;
import cn.beau.component.oauth.OauthTypeEnum;
import cn.beau.component.oauth.OauthUser;
import cn.beau.exception.BizException;
import cn.beau.utils.SimpleHttpRequest;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

/**
 * 钉钉登录
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Component
@Slf4j
public class DingOauthLoginImpl extends AbstractOauthLogin {
    private static final String LOGIN = "https://oapi.dingtalk.com/sns/getuserinfo_bycode?accessKey=%s&timestamp=%s&signature=%s";
    private static final String LOGIN_URL = "https://oapi.dingtalk.com/connect/qrconnect?appid=%s&response_type=code&scope=snsapi_login&state=STATE&redirect_uri=%s";

    @Override
    public String loginUrl(String backUrl) {
        try {
            ConfigDto configDto = getConfig();
            return String.format(LOGIN_URL, configDto.getAppKey(), URLEncoder.encode(getLoginCallback() + "?back=" + backUrl, "UTF-8"));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public LoginUser login(HttpServletRequest request) {
        ConfigDto configDto = getConfig();
        JSONObject body = new JSONObject();
        body.put("tmp_auth_code", request.getParameter("code"));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign = sign(timestamp, configDto.getAppSecret());
        String url = String.format(LOGIN, configDto.getAppKey(), timestamp, sign);
        String data = SimpleHttpRequest.post(url, body.toJSONString());
        JSONObject jsonObject = JSONObject.parseObject(data);
        if (jsonObject.getInteger("errcode") == 0) {
            JSONObject userInfo = jsonObject.getJSONObject("user_info");
            String nick = userInfo.getString("nick");
            String unionid = userInfo.getString("unionid");
            OauthUser oauthUser = new OauthUser();
            oauthUser.setName(nick);
            oauthUser.setOpenId(unionid);
            oauthUser.setSource(oauthType().name());
            return saveUser(oauthUser);
        } else {
            log.error("ding login fail: {}", data);
            throw new BizException(jsonObject.getString("errmsg"));
        }
    }

    public String sign(String data, String appSecret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(appSecret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signatureBytes = mac.doFinal(data.getBytes("UTF-8"));
            String signature = new String(Base64.encodeBase64(signatureBytes));
            if ("".equals(signature)) {
                return "";
            }
            String encoded = URLEncoder.encode(signature, "UTF-8");
            String urlEncodeSignature = encoded.replace("+", "%20").replace("*", "%2A").replace("~", "%7E").replace("/", "%2F");
            return urlEncodeSignature;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public OauthTypeEnum oauthType() {
        return OauthTypeEnum.DING_DING;
    }
}
