package cn.beau.dto.request;

import cn.beau.enums.TopicPositionEnum;
import cn.beau.enums.TopicTypeEnum;
import lombok.Data;

@Data
public class TopicRequest {
    private Long id;
    /**
     * 主题名
     */
    private String topicName;

    /**
     * 主题图片
     */
    private String topicPic;

    /**
     * 主题类型
     *
     * @see cn.beau.enums.TopicTypeEnum
     */
    private TopicTypeEnum topicType;

    /**
     * 主题展示位置
     *
     * @see cn.beau.enums.TopicPositionEnum
     */
    private TopicPositionEnum topicPosition;
}
