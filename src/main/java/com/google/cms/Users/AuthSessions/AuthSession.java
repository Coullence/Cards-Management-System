package com.google.cms.Users.AuthSessions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class AuthSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sn", updatable = false)
    private Long sn;
    @Column(unique = true)
    private String refreshToken;
    private String username;
    private String Status;
    private Character isActive = 'N';
    private String activity;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private String address;
    private String os;
    private String browser;
}
