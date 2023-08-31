package com.publicis.sapient.p2p.service;

import com.publicis.sapient.p2p.exception.BusinessException;
import com.publicis.sapient.p2p.exception.util.ErrorCode;
import com.publicis.sapient.p2p.model.CookieResponse;
import com.publicis.sapient.p2p.model.JwtRequestDto;
import com.publicis.sapient.p2p.model.User;
import com.publicis.sapient.p2p.repository.Impl.UserRepositoryImpl;
import com.publicis.sapient.p2p.utils.Constants;
import com.publicis.sapient.p2p.utils.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    
    @Autowired
    private UserRepositoryImpl userRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EncryptionUtil encryptionUtil;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    public CookieResponse validateUserAndGenerateToken(JwtRequestDto jwtRequestDto, User user) {
        logger.info("Entering validateUserAndGenerateToken method inside AuthServiceImpl");
        if(null!=user && jwtUtils.validateBCryptPassword(user.getEmail(), jwtRequestDto.getPassword())){
            return jwtUtils.generateCookie(user.getEmail());
        }else{
            throw new BusinessException(ErrorCode.UNAUTHENTICATED, Constants.AUTH_INVALID);
        }
    }

    @Override
    public CookieResponse invalidateUserToken() {
        logger.info("Entering invalidateUserToken method inside AuthServiceImpl");
        return jwtUtils.removeCookie();
    }
}
