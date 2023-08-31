package com.publicis.sapient.p2p.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.publicis.sapient.p2p.model.CookieResponse;
import com.publicis.sapient.p2p.model.JwtRequestDto;
import com.publicis.sapient.p2p.model.UserDto;
import com.publicis.sapient.p2p.repository.Impl.UserRepositoryImpl;
import com.publicis.sapient.p2p.service.AuthServiceImpl;
import com.publicis.sapient.p2p.service.JwtUtils;
import com.publicis.sapient.p2p.vo.ServiceResponse;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {JwtController.class})
@ExtendWith(SpringExtension.class)
class JwtControllerTest {

    @Autowired
    JwtController jwtController;

    @MockBean
    AuthServiceImpl authService;

    @MockBean
    UserRepositoryImpl userRepository;

    @MockBean
    ModelMapper modelMapper;

    @MockBean
    JwtUtils jwtUtils;

    @Bean
    private BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Test
    void createTokenTest() throws Exception {
        JwtRequestDto jwtRequestDto = new JwtRequestDto();
        jwtRequestDto.setEmail("admin@gmail.com");
        jwtRequestDto.setPassword(new BCryptPasswordEncoder().encode("admin@123"));

        Cookie jwtCookie = new Cookie("token", "token");
        jwtCookie.setMaxAge(120);

        com.publicis.sapient.p2p.model.User user = new com.publicis.sapient.p2p.model.User();
        user.setId("1");
        user.setEmail("admin@gmail.com");
        user.setPassword("admin@123");
        user.setFirstName("admin");
        user.setStatus("active");
        user.setLastName("admin");

        UserDto userDto =new UserDto();
        userDto.setId("1");
        userDto.setEmail("admin@gmail.com");
        userDto.setFirstName("admin");
        user.setLastName("admin");

        when(userRepository.findUserByEmail(jwtRequestDto.getEmail())).thenReturn(user);
        when(modelMapper.map(userRepository.findUserByEmail(jwtRequestDto.getEmail()),UserDto.class)).thenReturn(userDto);
        var cookieResponse = new CookieResponse("1", "a@a.com", "token", new Cookie("a", "a"), new Cookie("a", "a"), new Cookie("a", "a"));

        when(authService.validateUserAndGenerateToken(any(JwtRequestDto.class), any())).thenReturn(cookieResponse);


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
          {
            "email": "admin@gmail.com",
            "password": "admin@123"
          }
        """);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(jwtController)
                .build()
                .perform(requestBuilder);
        MockHttpServletResponse response = actualPerformResult.andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        String content = response.getContentAsString();
        ServiceResponse resp = new ObjectMapper().readValue(content, ServiceResponse.class);
        assertEquals("OK", resp.getStatusCode());

    }

    @Test
    void logOutTest() throws Exception {
        var cookieResponse = new CookieResponse("1", "a@a.com", "token", new Cookie("a", "a"), new Cookie("a", "a"), new Cookie("a", "a"));

        when(authService.invalidateUserToken()).thenReturn(cookieResponse);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/authenticate")
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(jwtController)
                .build()
                .perform(requestBuilder);
        MockHttpServletResponse response = actualPerformResult.andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        String content = response.getContentAsString();
        ServiceResponse resp = new ObjectMapper().readValue(content, ServiceResponse.class);
        assertEquals("Logout Successful", resp.getOutput());
    }


}
