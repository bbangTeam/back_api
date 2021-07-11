package io.my.bbang.commons.service;

import org.springframework.stereotype.Service;

import io.my.bbang.commons.utils.JwtUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtUtil jwtUtil;

    public String accessTokenIssued(String accessToken) {
        String id = jwtUtil.parseAccessToken(accessToken).get("userId").toString();
        return jwtUtil.createAccessToken(id);
    }
    
}
