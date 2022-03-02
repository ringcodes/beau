package cn.beau.enums;

import cn.beau.base.KeyValueVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum MenuEnum {
    M_HOME("工作台"),
    M_TOPIC_ADMIN("主题管理"),
    M_ARTICLE_ADMIN("文章管理"),
    M_LABEL_ADMIN("标签管理"),
    M_SLIDER_ADMIN("轮播管理"),
    M_USER_ADMIN("用户管理"),
    M_AUTH_ADMIN("权限管理"),
    M_SYS_SETTING("系统设置"),
    M_LINK_ADMIN("友情链接"),
    M_THIRD_LOGIN("第三方登录配置"),
    M_WEB_CONFIG("网站配置"),
    M_ADMIN_TOOL("管理工具");

    private String desc;


    MenuEnum(String desc) {
        this.desc = desc;
    }

    public static List<KeyValueVo> listAll() {
        List<KeyValueVo> list = new ArrayList<>();
        for (MenuEnum r : values()) {
            list.add(new KeyValueVo(r.name(), r.getDesc()));
        }
        return list;
    }
}
