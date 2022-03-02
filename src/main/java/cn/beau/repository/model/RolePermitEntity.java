package cn.beau.repository.model;

import cn.beau.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_role_permit")
public class RolePermitEntity extends BaseEntity {
    public static final int MENU = 1;
    public static final int API = 2;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("role")
    private Long role;

    @TableField("permit_code")
    private String permitCode;

    @TableField("permit_type")
    private Integer permitType;
}
