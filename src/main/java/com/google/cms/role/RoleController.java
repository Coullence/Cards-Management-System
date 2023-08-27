package com.google.cms.role;

import com.google.cms.utilities.Shared.EntityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/roles")
@Slf4j
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/add")
    public EntityResponse createRole(@RequestBody Role role){ return roleService.createRole(role);}
    @GetMapping("/all")
    public EntityResponse findAllRole()
    {
        return roleService.findAllRole();
    }
    @GetMapping("/find/role")
    public EntityResponse findById(@RequestParam Long id)
    {
        return roleService.findRoleById(id);
    }
    @PutMapping("/update")
    public EntityResponse updateRole(@RequestBody Role role)
    {
        return roleService.updateRole(role);
    }
    @DeleteMapping("/delete")
    public EntityResponse deleteRole(@RequestParam Long id)
    {
        return roleService.deleteRole(id);
    }
}