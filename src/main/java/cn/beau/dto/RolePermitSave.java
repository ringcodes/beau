package cn.beau.dto;

import lombok.Data;

@Data
public class RolePermitSave {
    private Long role;
    private String permitCode;
    private Long createId;
    private Integer permitType;
}
