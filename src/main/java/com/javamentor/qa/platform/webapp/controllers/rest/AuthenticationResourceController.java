package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.models.dto.JwtTokenDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.security.jwt.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@Api("Authentication Api")
@RequestMapping("/api")
public class AuthenticationResourceController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResourceController(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth/token/")
    @ApiOperation("Возвращает строку токена в виде объекта JwtTokenDto, на вход получает объект AuthenticationRequest, " +
            "который содержит username, password и значение поля isRemember")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получен JWT токен"),
            @ApiResponse(code = 400, message = "Ошибка аутентификации: имя или пароль неправильны")
    })
    public ResponseEntity<?> getToken(@RequestBody AuthenticationRequest request)
    {
        JwtTokenDto jwtTokenDTO = new JwtTokenDto();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            User user = (User) authentication.getPrincipal();
            if (request.isRemember()) {
                jwtTokenDTO.setToken(jwtUtil.generateLongToken(user));
            } else {
                jwtTokenDTO.setToken(jwtUtil.generateAccessToken(user));
            }
        }
        catch (BadCredentialsException exception) {
            return new ResponseEntity<>("Имя или пароль неправильны", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(jwtTokenDTO, HttpStatus.OK);
    }

    @GetMapping("/user/check_auth")
    @ApiOperation("В случае успешного прохождения авторизации пользователь перенаправляется на запрашиваемую страницу")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Авторизация успешно пройдена"),
            @ApiResponse(code = 401, message = "Пользователь не авторизован. Перенаправление на страницу /login"),
            @ApiResponse(code = 403, message = "Доступ запрещен")
    })
    public ResponseEntity<Void> checkAuthorization() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_USER")) {
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
