package cn.beau.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PermitRequest {
    private String roleEnum;
    private List<String> permit;
    private Long createId;
}
