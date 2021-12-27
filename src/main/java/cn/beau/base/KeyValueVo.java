package cn.beau.base;

import lombok.Data;

/**
 * 通用数据类
 *
 * @author liushilin
 * @date 2021/12/15
 */
@Data
public class KeyValueVo {

    private String name;
    private String desc;
    private Object value;

    public KeyValueVo() {
    }

    public KeyValueVo(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public KeyValueVo(String name, Object value) {
        this.name = name;
        this.desc = desc;
        this.value = value;
    }

    public KeyValueVo(String name, String desc, Object value) {
        this.name = name;
        this.desc = desc;
        this.value = value;
    }
}
