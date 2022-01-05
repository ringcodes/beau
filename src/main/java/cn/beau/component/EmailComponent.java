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

package cn.beau.component;

import cn.beau.dto.MailInfo;
import cn.beau.dto.config.WebSiteConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件服务
 *
 * @author liushilin
 * @date 2022/1/5
 */
@Component
@Slf4j
public class EmailComponent {
    @Autowired
    private WebConfigComponent webConfigComponent;
    @Autowired
    private TheadPoolComponent theadPoolComponent;


    public void sendHtmlMail(MailInfo info) {
        theadPoolComponent.addTask(() -> {
            try {
                Assert.hasText(info.getContent(), "内容不能为空");
                WebSiteConfigDto webRegConfigDto = webConfigComponent.getWebSiteConfig();
                if (webRegConfigDto == null
                    || StringUtils.isEmpty(webRegConfigDto.getEmail())
                    || StringUtils.isEmpty(webRegConfigDto.getEmailPass())
                ) {
                    log.warn("注册邮箱未配置，不发送欢迎邮件");
                    return;
                }
                Message message = getMessage(info, webRegConfigDto);
                // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
                Multipart mainPart = new MimeMultipart();
                // 创建一个包含HTML内容的MimeBodyPart
                BodyPart html = new MimeBodyPart();
                // 设置HTML内容
                html.setContent(info.getContent(), "text/html; charset=utf-8");
                mainPart.addBodyPart(html);
                // 将MiniMultipart对象设置为邮件内容
                message.setContent(mainPart);
                Transport.send(message);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private Message getMessage(MailInfo info, WebSiteConfigDto webRegConfigDto) throws Exception {
        final Properties p = System.getProperties();
        p.setProperty("mail.smtp.host", webRegConfigDto.getEmailHost());
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.user", webRegConfigDto.getEmail());
        p.setProperty("mail.smtp.pass", webRegConfigDto.getEmailPass());

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = Session.getInstance(p, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(p.getProperty("mail.smtp.user"), p.getProperty("mail.smtp.pass"));
            }
        });
        session.setDebug(true);
        Message message = new MimeMessage(session);
        //消息发送的主题
        message.setSubject(info.getSubject());
        //接受消息的人
        message.setReplyTo(InternetAddress.parse(webRegConfigDto.getEmail()));
        //消息的发送者
        if (StringUtils.hasText(webRegConfigDto.getEmailName())) {
            message.setFrom(new InternetAddress(p.getProperty("mail.smtp.user"), webRegConfigDto.getEmailName()));
        } else {
            message.setFrom(new InternetAddress(p.getProperty("mail.smtp.user"), webRegConfigDto.getEmail()));
        }

        // 创建邮件的接收者地址，并设置到邮件消息中
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(info.getToAddress()));
        // 消息发送的时间
        message.setSentDate(new Date());
        return message;
    }
}
