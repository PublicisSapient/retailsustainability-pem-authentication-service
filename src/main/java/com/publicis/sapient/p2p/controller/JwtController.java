package com.publicis.sapient.p2p.controller;

import com.publicis.sapient.p2p.model.JwtRequestDto;
import com.publicis.sapient.p2p.model.User;
import com.publicis.sapient.p2p.model.UserDto;
import com.publicis.sapient.p2p.service.AuthServiceImpl;
import com.publicis.sapient.p2p.repository.Impl.UserRepositoryImpl;
import com.publicis.sapient.p2p.service.JwtUtils;
import com.publicis.sapient.p2p.utils.Constants;
import com.publicis.sapient.p2p.vo.ServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/authenticate")
@Tag(name = "Authentication", description = "Authentication Service API for Login")
public class JwtController {

    private final Logger logger = LoggerFactory.getLogger(JwtController.class);

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    @Operation(operationId = "createToken", description = "Provides Jwt Token Post User Validation", summary = "Validates the email id and password from database and generates the Auth Token.", tags = {"Authentication"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product Details Dto",
                    content = @Content(schema = @Schema(implementation = JwtRequestDto.class)), required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User Logged In Successfully", content = @Content(schema = @Schema(implementation = ServiceResponse.class))),
                    @ApiResponse(responseCode = "400", description = Constants.INVALID_USERID_PASSWORD_MESSAGE, content = @Content(schema = @Schema(implementation = ServiceResponse.class))),
            })
    public ResponseEntity<ServiceResponse> createToken(HttpServletRequest request, HttpServletResponse response, @RequestBody JwtRequestDto jwtRequestDto) {
        logger.info("Entering createToken method with endpoint: /authenticate");
        ServiceResponse serverResponse = new ServiceResponse();
        String email = jwtRequestDto.getEmail().toLowerCase();
        User user = userRepository.findUserByEmail(email);
        var cookieResponse = authService.validateUserAndGenerateToken(jwtRequestDto, user);
        response.addCookie(cookieResponse.getTokenCookie());
        response.addCookie(cookieResponse.getRefreshTokenCookie());
        response.addCookie(cookieResponse.getNormalCookie());
        var userDto = modelMapper.map(user, UserDto.class);
        serverResponse.setOutput(userDto);
        serverResponse.setStatusCode(HttpStatus.OK.getReasonPhrase());
        return ResponseEntity.ok(serverResponse);
    }

    @GetMapping
    @Operation(operationId = "logOut", description = "Removing  Cookies from browser to make user logged out", summary = "Removing  Cookies from browser to make user logged out ", tags = {"Authentication"},
            responses = {
                    @ApiResponse(responseCode = "200", description = Constants.LOGOUT_MESSAGE, content = @Content(schema = @Schema(implementation = ServiceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid Jwt Token", content = @Content(schema = @Schema(implementation = ServiceResponse.class))),
            })
    public ResponseEntity<ServiceResponse> logOut(HttpServletRequest request, HttpServletResponse response){
        logger.info("Entering logOut method with endpoint: /authenticate");
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setOutput("Logout Successful");
        serviceResponse.setStatusCode(HttpStatus.OK.getReasonPhrase());
        var cookieResponse = authService.invalidateUserToken();
        response.addCookie(cookieResponse.getTokenCookie());
        response.addCookie(cookieResponse.getRefreshTokenCookie());
        response.addCookie(cookieResponse.getNormalCookie());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(serviceResponse);
    }
}
