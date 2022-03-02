package cn.beau.controller.admin;

import cn.beau.base.KeyValueVo;
import cn.beau.base.LoginUser;
import cn.beau.base.ResultObject;
import cn.beau.base.ResultUtil;
import cn.beau.dto.request.PermitRequest;
import cn.beau.enums.MenuEnum;
import cn.beau.enums.ResourceEnum;
import cn.beau.enums.RoleEnum;
import cn.beau.manager.RolePermitManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/permit")
public class PermitController {
    @Autowired
    private RolePermitManager rolePermitManager;

    @GetMapping("/roleList")
    public ResultObject roleList() {
        return ResultUtil.newSucc(RoleEnum.listAll());
    }

    @GetMapping("/menuList")
    public ResultObject menuList() {
        return ResultUtil.newSucc(MenuEnum.listAll());
    }

    @GetMapping("/permitPage")
    public ResultObject permitPage() {
        return ResultUtil.newSucc(ResourceEnum.listAll().stream().collect(Collectors.groupingBy(KeyValueVo::getValue)));
    }

    @GetMapping("/{roleEnum}")
    public ResultObject queryPermit(@PathVariable RoleEnum roleEnum) {
        return ResultUtil.newSucc(rolePermitManager.getPermit(Long.valueOf(roleEnum.getCode())));
    }

    @PostMapping("/save")
    public ResultObject savePermit(LoginUser loginUser, @RequestBody PermitRequest request) {
        request.setCreateId(loginUser.getId());
        return ResultUtil.newSucc(rolePermitManager.save(request));
    }
}
