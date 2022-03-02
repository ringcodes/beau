package cn.beau.enums;

import cn.beau.base.KeyValueVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum ResourceEnum {
    ARTICLE_LIST(1, "查询文章"),
    ARTICLE_EDIT(1, "编辑文章"),
    ARTICLE_DEL(1, "删除文章"),
    TOPIC_LIST(2, "查询主题"),
    TOPIC_EDIT(2, "编辑主题"),
    TOPIC_DEL(2, "删除主题"),
    SLIDER_LIST(3, "查询轮播"),
    SLIDER_EDIT(3, "编辑轮播"),
    SLIDER_DEL(3, "删除轮播"),
    LABEL_LIST(4, "查询标签"),
    LABEL_EDIT(4, "编辑标签"),
    LABEL_DEL(4, "删除标签"),
    USER_LIST(5, "查询用户"),
    USER_EDIT(5, "编辑用户"),
    USER_ADD(5, "新增用户"),
    USER_FORBID(5, "禁用用户"),
    USER_DEL(5, "删除用户"),
    CONFIG_LIST(6, "查询配置"),
    CONFIG_EDIT(6, "编辑配置"),
    CONFIG_DEL(6, "删除配置"),
    COMMENT_LIST(7, "查询评论"),
    COMMENT_DEL(7, "删除评论"),
    PERMIT_LIST(8, "查询权限"),
    PERMIT_EDIT(8, "编辑权限"),
    PERMIT_DEL(8, "删除权限"),
    FILE_UPLOAD(15, "文件上传"),
    TASK_LIST(20, "查询定时任务"),
    TASK_RUN(20, "执行定时任务");

    private int group;
    private String desc;

    ResourceEnum(int group, String desc) {
        this.desc = desc;
        this.group = group;
    }

    public static List<KeyValueVo> listAll() {
        List<KeyValueVo> list = new ArrayList<>();
        for (ResourceEnum r : values()) {
            list.add(new KeyValueVo(r.name(), r.getDesc(), r.getGroup()));
        }
        return list;
    }
}
