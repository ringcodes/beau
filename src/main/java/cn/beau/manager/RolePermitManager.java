package cn.beau.manager;

import cn.beau.dto.RolePermitSave;
import cn.beau.dto.request.PermitRequest;
import cn.beau.enums.MenuEnum;
import cn.beau.enums.RoleEnum;
import cn.beau.repository.mapper.RolePermitMapper;
import cn.beau.repository.model.RolePermitEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RolePermitManager {
    @Autowired
    private RolePermitMapper rolePermitMapper;
    // 缓存
    private Cache<String, List<String>> userCache = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .maximumSize(1000)
        .build();

    public List<String> getPermit(Long role) {
        return getPermit(role, RolePermitEntity.API);
    }

    public List<String> getPermit(Long role, Integer type) {
        String key = role + "#" + type;
        List<String> data = userCache.getIfPresent(key);
        if (!CollectionUtils.isEmpty(data)) {
            return data;
        }
        data = queryPermitCode(role, type);
        if (!CollectionUtils.isEmpty(data)) {
            userCache.put(key, data);
        }
        return data;
    }

    private List<String> queryPermitCode(Long role, Integer type) {
        if (role == RoleEnum.SYS.getCode() && RolePermitEntity.MENU == type.intValue()) {
            List<String> list = new ArrayList<>();
            for (MenuEnum menuEnum : MenuEnum.values()) {
                list.add(menuEnum.name());
            }
            return list;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role", role);
        queryWrapper.eq("permit_type", type);
        List<RolePermitEntity> list = rolePermitMapper.selectList(queryWrapper);
        return list.stream().map(RolePermitEntity::getPermitCode).collect(Collectors.toList());
    }

    public Boolean save(PermitRequest permitRequest) {
        RoleEnum roleEnum = RoleEnum.valueOf(permitRequest.getRoleEnum());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role", roleEnum.getCode());
        rolePermitMapper.delete(queryWrapper);
        if (CollectionUtils.isEmpty(permitRequest.getPermit())) {
            return Boolean.TRUE;
        }
        List<RolePermitSave> saves = new ArrayList<>();
        permitRequest.getPermit().forEach(it -> {
            int type;
            if (it.startsWith("M_")) {
                type = RolePermitEntity.MENU;
            } else {
                type = RolePermitEntity.API;
            }
            RolePermitSave rolePermitSave = new RolePermitSave();
            rolePermitSave.setRole(Long.valueOf(roleEnum.getCode()));
            rolePermitSave.setCreateId(permitRequest.getCreateId());
            rolePermitSave.setPermitCode(it);
            rolePermitSave.setPermitType(type);
            saves.add(rolePermitSave);
        });
        userCache.invalidate(roleEnum.getCode());
        return rolePermitMapper.batchInsert(saves) > 0;
    }
}
