package cn.beau.dto.request;

import lombok.Data;

@Data
public class TopicSortRequest {
    private Long id;
    private Long preId;
    private Long postId;
    private Boolean pre;
}
