package com.publicis.sapient.p2p.service;

import com.publicis.sapient.p2p.model.CookieResponse;
import com.publicis.sapient.p2p.model.JwtRequestDto;
import com.publicis.sapient.p2p.model.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    CookieResponse validateUserAndGenerateToken(JwtRequestDto jwtRequestDto, User user);
    CookieResponse invalidateUserToken();
}
