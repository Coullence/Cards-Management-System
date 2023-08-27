package com.google.cms.utilities.requests_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UseranalyticsDTO {
    private Integer newUserRequests;
    private Integer issuedAccounts;
    private Integer activeUsers;
    private Integer lockedAccounts;
    private Integer activeSessions;
}
