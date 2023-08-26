package com.google.cms.utilities.Shared;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {
    public EntityResponse response(HttpStatus status, String message, Object entity) {
        EntityResponse response = new EntityResponse();
        response.setStatusCode(status.value());
        response.setMessage(message);
        response.setEntity(entity);
        return response;
    }
}
