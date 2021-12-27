package cn.beau.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleRelateVo {
    private Long id;

    private String title;

    private Integer sourceType;

    private String titlePic;

    private Date updateTime;

    private String description;

    private String sourceName;

    private Long topicId;

    private String topicName;
}
