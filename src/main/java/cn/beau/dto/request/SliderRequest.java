package cn.beau.dto.request;

import cn.beau.enums.SliderTypeEnum;
import cn.beau.enums.StatusEnum;
import lombok.Data;

@Data
public class SliderRequest {
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 图片地址
     */
    private String pic;

    /**
     * 跳转地址
     */
    private String target;

    /**
     * 类型
     *
     * @see cn.beau.enums.SliderTypeEnum
     */
    private SliderTypeEnum sliderType;

    /**
     * 状态
     *
     * @see
     */
    private StatusEnum status;
}
