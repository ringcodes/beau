## 简介
Beau是一款基于java开发的轻量级博客、个人建站平台
> 目前Doing的所有功能均为个人开发,欢迎各位共同交流,同时如若本项目对您有所帮助,请为它点个star
* 演示站点: [https://www.gz640.cn](https://www.gz640.cn)

## 使用技术
* 基于SpringBoot2.2.5，Mybatis，Mybatis-plus
* 使用beetl模板引擎
* Vue+antd+axios的前后分离的后台管理
* 支持腾讯OSS、七牛云OSS文件存储
* 支持钉钉、github、gitee的登录
* Less样式

## 功能简介
* 设计简洁，界面美观
* 支持[Markdown](https://www.markdownguide.org/)、富文本两种格式编辑文章
* 使用css变量定义主题，简单快捷自定义出各种样式，举例见 主题定义
* 支持友情链接
* 支持附件管理，附件在线预览
* 项目结构清晰，安装部署简单
* 支持首页静态化，首屏秒开

## 安装教程
1. 执行数据库脚本ddl.sql
2. 源码Docker安装(推荐)  
   ```
   git clone https://gitee.com/ringcode/beau.git  
   ```
   修改配置文件 application-prod.yaml
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/beau-pro?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=CST&allowPublicKeyRetrieval=true
       username: root
       password: root
   oss:
     tencent: #腾讯OSS文件存储,详情见 https://www.gz640.cn
       enable: true #是否启用
       bucket: #BUCKET
       secret-key: # Secret Key
       access-key: #Access Key
       region-name: #Region Name
   ```
   
   ``` 
   cd beau   
   docker build -t beau:lastest . 
   ```
   
   ```
   docker run -d -e ENV=prod beau:lastest
   ```

3. 使用安装包  
   下载安装包并解压  
   修改配置文件application-prod.yaml  
   执行启动命令  
   ```
   java -jar -Dspring.profiles.active=prod beau_web.jar
   ```


4. 管理后台UI  
   参考 [文档](https://gitee.com/ringcode/beau-ui)
## 主题定义
  基于文件src/main/resources/static/theme/theme.css  
```css
:root {
   --main-color: #1abc9c;/* 主色调 */
   --primay-color: #1abcb3;/* 浅主色调 */
   --font-color: #333;/* 字体主色 */
   --font-color-desc: #62625d;/* 字体浅色 */
   --font-color-gray: #999;/* 字体灰色 */
}
```
学习交流群  
![](https://gitee.com/lsl52640/files/raw/master/contact_me_qr.png)
