package com.google.cms.utilities.Shared;

import com.google.cms.utilities.requests_proxy.UserRequestContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditTrailService {

    public <T extends Audittrails> T POSTAudit(T data) {
        data.setPostedBy(UserRequestContext.getCurrentUser());
        data.setPostedFlag('Y');
        data.setPostedTime(LocalDateTime.now());
        return data;
    }
    public <T extends Audittrails> T MODIFYAudit(T data) {
        data.setModifiedBy(UserRequestContext.getCurrentUser());
        data.setModifiedFlag('Y');
        data.setModifiedTime(LocalDateTime.now());
        return data;
    }
    public <T extends Audittrails> T DELETEAudit(T data) {
        data.setDeletedBy(UserRequestContext.getCurrentUser());
        data.setDeletedFlag('Y');
        data.setDeletedTime(LocalDateTime.now());
        return data;
    }

}
