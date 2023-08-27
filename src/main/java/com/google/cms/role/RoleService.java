package com.google.cms.role;

import com.google.cms.utilities.Shared.AuditTrailService;
import com.google.cms.utilities.Shared.EntityResponse;
import com.google.cms.utilities.Shared.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoleService {
    private final ResponseService responseService;
    private final AuditTrailService auditTrailService;
    private final RoleRepository roleRepository;

    public RoleService(ResponseService responseService, AuditTrailService auditTrailService, RoleRepository roleRepository) {
        this.responseService = responseService;
        this.auditTrailService = auditTrailService;
        this.roleRepository = roleRepository;
    }


    public EntityResponse createRole(Role role){
        try {
            return responseService.response(
                    HttpStatus.CREATED,
                    "CREATED SUCCESSFULLY!",
                    RoleService.this.roleRepository.save(auditTrailService.POSTAudit(role))
            );
        }catch (Exception e){
            log.info(e.toString());
            return null;
        }
    }
    public EntityResponse findAllRole(){
        try {
            List<Role> rolesList = RoleService.this.roleRepository.findByDeletedFlag('N');
            if (rolesList.size()>0){
                return responseService.response(
                        HttpStatus.FOUND,
                        "ROLES FOUND!",
                        rolesList
                );
            }else {
                return responseService.response(
                        HttpStatus.NOT_FOUND,
                        "ROLES NOT FOUND!",
                        null
                );
            }
        }catch (Exception e){
            log.info(e.toString());
            return null;
        }
    }
    public EntityResponse findRoleById(Long id){
        try {
            Optional<Role> roleOptional = RoleService.this.roleRepository.findById(id);
            if (roleOptional.isPresent()){
                return responseService.response(
                        HttpStatus.FOUND,
                        "ROLE FOUND!",
                        roleOptional.get()
                );
            }else {
                return responseService.response(
                        HttpStatus.NOT_FOUND,
                        "ROLE  NOT FOUND!",
                        null
                );
            }

        }catch (Exception e){
            log.info(e.toString());
            return null;
        }
    }
    public EntityResponse updateRole(Role role) {
        try {
            Optional<Role> roleOptional = RoleService.this.roleRepository.findById(role.getId());
            if (roleOptional.isPresent()) {
                Role existingRole = roleOptional.get();
                return responseService.response(
                        HttpStatus.OK,
                        "UPDATED SUCCESSFULLY!",
                        RoleService.this.roleRepository.save(auditTrailService.MODIFYAudit(existingRole))
                );
            } else {
                return responseService.response(
                        HttpStatus.NOT_FOUND,
                        "ROLE NOT FOUND!",
                        null
                );
            }
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    public EntityResponse deleteRole(Long id){
        try {
            Optional<Role> roleOptional = RoleService.this.roleRepository.findById(id);
            if (roleOptional.isPresent()){
                Role role = roleOptional.get();
                return responseService.response(
                        HttpStatus.OK,
                        "DELETED SUCCESSFULLY!",
                        RoleService.this.roleRepository.save(auditTrailService.DELETEAudit(role))
                );
            }else {
                return responseService.response(
                        HttpStatus.NOT_FOUND,
                        "ROLE NOT FOUND!",
                        null
                );
            }
        }catch (Exception e){
            log.info(e.toString());
            return null;
        }
    }
}