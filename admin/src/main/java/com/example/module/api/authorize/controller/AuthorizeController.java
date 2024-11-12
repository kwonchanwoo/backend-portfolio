package com.example.module.api.authorize.controller;


import com.example.module.api.authorize.dto.request.RequestLoginDto;
import com.example.module.api.authorize.dto.response.TokenInfo;
import com.example.module.api.authorize.service.AuthorizeService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("authorize")
public class AuthorizeController {
    private final AuthorizeService authorizeService;

    @PostMapping("/login")
    public TokenInfo login(
            @Valid @RequestBody RequestLoginDto loginDto
    ) {
        return authorizeService.login(loginDto);
    }
    //Todo

    @PostMapping("/refresh-token")
    public TokenInfo refreshToken(
            @RequestBody TokenInfo tokenInfo
    ) {
        return authorizeService.refreshToken(tokenInfo);
    }

    @DeleteMapping("/logout")
    public void logout(){
        authorizeService.logout();
    }


    @ExceptionHandler({SecurityException.class, MalformedJwtException.class, IllegalArgumentException.class, ExpiredJwtException.class, UnsupportedJwtException.class})
    public ResponseEntity<Void> jwtExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
