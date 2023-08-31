package com.publicis.sapient.p2p.service;

import com.publicis.sapient.p2p.exception.BusinessException;
import com.publicis.sapient.p2p.model.CookieResponse;
import com.publicis.sapient.p2p.model.JwtRequestDto;
import com.publicis.sapient.p2p.model.User;
import com.publicis.sapient.p2p.repository.Impl.UserRepositoryImpl;
import com.publicis.sapient.p2p.utils.EncryptionUtil;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {AuthServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AuthServiceImplTest {

    @Autowired
    AuthServiceImpl authService;

    @MockBean
    EncryptionUtil encryptionUtil;

    @MockBean
    UserRepositoryImpl userRepository;

    @MockBean
    JwtUtils jwtUtils;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void validateUserAndGenerateTokenTest() {

        JwtRequestDto jwtRequestDto = new JwtRequestDto();
        jwtRequestDto.setEmail("admin@gmail.com");
        jwtRequestDto.setPassword(new BCryptPasswordEncoder().encode("admin@123"));

        com.publicis.sapient.p2p.model.User user = new com.publicis.sapient.p2p.model.User();
        user.setId("1");
        user.setPassword("admin@123");
        user.setEmail("a@a.com");
        user.setFirstName("admin");
        user.setStatus("active");
        user.setLastName("admin");

        Cookie jwtCookie = new Cookie("token", "token");
        jwtCookie.setMaxAge(120);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(user);
        when(jwtUtils.validateBCryptPassword(any(String.class),any(String.class))).thenReturn(true);
        var cookieResponse = new CookieResponse("1", "a@a.com", "token", new Cookie("a", "a"), new Cookie("a", "a"), new Cookie("a", "a"));
        when(jwtUtils.generateCookie(any(String.class))).thenReturn(cookieResponse);

        CookieResponse response = authService.validateUserAndGenerateToken(jwtRequestDto, user);
        Assertions.assertEquals(response.getEmail(), user.getEmail());

    }

    @Test
    void validateUserAndGenerateTokenUserNotFoundTest() {

        JwtRequestDto jwtRequestDto = new JwtRequestDto();
        jwtRequestDto.setEmail("admin@gmail.com");
        jwtRequestDto.setPassword(new BCryptPasswordEncoder().encode("admin@123"));

        User user = new User();
        user.setId("1");
        user.setPassword("admin@123");
        user.setFirstName("admin");
        user.setStatus("active");
        user.setLastName("admin");

        Cookie jwtCookie = new Cookie("token", "token");
        jwtCookie.setMaxAge(120);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(null);
        when(jwtUtils.validateBCryptPassword(any(String.class),any(String.class))).thenReturn(true);
        var cookieResponse = new CookieResponse("1", "a@a.com", "token", new Cookie("a", "a"), new Cookie("a", "a"), new Cookie("a", "a"));
        when(jwtUtils.generateCookie(any(String.class))).thenReturn(cookieResponse);

        Assertions.assertThrows(BusinessException.class, () -> authService.validateUserAndGenerateToken(jwtRequestDto, user));

    }

    @Test
    void validateUserAndGenerateTokenPasswordIncorrectTest() {

        JwtRequestDto jwtRequestDto = new JwtRequestDto();
        jwtRequestDto.setEmail("admin@gmail.com");
        jwtRequestDto.setPassword(new BCryptPasswordEncoder().encode("admin@123"));

        User user = new User();
        user.setId("1");
        user.setEmail("admin@gmail.com");
        user.setPassword("admin@123");
        user.setFirstName("admin");
        user.setStatus("active");
        user.setLastName("admin");

        Cookie jwtCookie = new Cookie("token", "token");
        jwtCookie.setMaxAge(120);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(user);
        when(jwtUtils.validateBCryptPassword(any(String.class),any(String.class))).thenReturn(false);

        Assertions.assertThrows(BusinessException.class, () -> authService.validateUserAndGenerateToken(jwtRequestDto, user));

    }

    @Test
    void validateUserAndGenerateTokenUserNotFoundAndPasswordIncorrectTest()  {

        JwtRequestDto jwtRequestDto = new JwtRequestDto();
        jwtRequestDto.setEmail("admin@gmail.com");
        jwtRequestDto.setPassword(new BCryptPasswordEncoder().encode("admin@123"));

        User user = new User();
        user.setId("1");
        user.setPassword("admin@123");
        user.setFirstName("admin");
        user.setStatus("active");
        user.setLastName("admin");

        Cookie jwtCookie = new Cookie("token", "token");
        jwtCookie.setMaxAge(120);
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(null);
        when(jwtUtils.validateBCryptPassword(any(String.class),any(String.class))).thenReturn(false);

        Assertions.assertThrows(BusinessException.class, () -> authService.validateUserAndGenerateToken(jwtRequestDto, user));

    }

    @Test
    void invalidateUserTokenTest() {
        var cookieResponse = new CookieResponse("1", "a@a.com", "token", new Cookie("a", "a"), new Cookie("a", "a"), new Cookie("a", "a"));

        when(jwtUtils.removeCookie()).thenReturn(cookieResponse);
        when(jwtUtils.validateJwtToken("token")).thenReturn(true);
        CookieResponse response = authService.invalidateUserToken();
        Assertions.assertEquals(response.getEmail(), cookieResponse.getEmail());
    }

}
