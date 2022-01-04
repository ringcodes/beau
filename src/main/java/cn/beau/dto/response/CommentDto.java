package cn.beau.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private Long parentId;
    private List<CommentDto> children;
    private Date createTime;
    private String userName;
    private String avatar;
}
