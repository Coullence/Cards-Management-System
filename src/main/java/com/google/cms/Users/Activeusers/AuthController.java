package com.google.cms.Users.Activeusers;

import com.google.cms.Security.jwt.JwtUtils;
import com.google.cms.Security.refreshtoken.RefreshToken;
import com.google.cms.Security.refreshtoken.RefreshTokenService;
import com.google.cms.utilities.Shared.EntityResponse;
import com.google.cms.utilities.requests_dto.LoginRequest;
import com.google.cms.utilities.requests_dto.auth_dto.JwtResponsedto;
import com.google.cms.utilities.requests_dto.res.RoleResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @PostMapping("/signin")
    public EntityResponse signin(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) throws MessagingException {
        EntityResponse response = new EntityResponse();
        User user = userRepository.findByUsername(loginRequest.getEmail()).orElse(null);
        if (user == null) {
            response.setMessage("Wrong Username");
            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            return response;
        } else {
//        Check if Account is Locked
            if (user.getDeletedFlag() == 'Y') {
                response.setMessage("This account has been deleted!");
                response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                return response;
            } else {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
                JwtResponsedto jwtResponsedto = getAccessToken(loginRequest.getEmail());
                jwtResponsedto.setRefreshToken(refreshToken.getToken());
                jwtResponsedto.setOtpEnabled(false);
                jwtResponsedto.setEmail(user.getEmail());
                userRepository.save(user);
                response.setMessage("Welcome " + user.getUsername() + ", You have been Authenticated Successfully at " + new Date());
                response.setStatusCode(HttpStatus.OK.value());
                response.setEntity(jwtResponsedto);
            }
        }
        return response;
    }
    private JwtResponsedto getAccessToken(String username){
        Optional<User> userCheck = userRepository.findByUsername(username);
        User user = userCheck.get();
        RoleResDTO roleResDTO = new RoleResDTO();
        roleResDTO.setName(user.getRoles().iterator().next().getName());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        String jwt = jwtUtils.generateTokenFromUsername(username);
        JwtResponsedto jwtResponsedto = new JwtResponsedto();
        jwtResponsedto.setToken(jwt);
        jwtResponsedto.setRefreshToken(refreshToken.getToken());
        jwtResponsedto.setId(user.getId().longValue());
        jwtResponsedto.setUsername(user.getUsername());
        jwtResponsedto.setEmail(user.getEmail());
        jwtResponsedto.setFirstName(user.getFirstName());
        jwtResponsedto.setLastName(user.getLastName());
        jwtResponsedto.setRole(roleResDTO);
        return jwtResponsedto;
    }
}
