package cn.beau.repository.mapper;

import cn.beau.dto.RolePermitSave;
import cn.beau.repository.model.RolePermitEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermitMapper extends BaseMapper<RolePermitEntity> {

    Integer batchInsert(List<RolePermitSave> list);
}
