package com.publicis.sapient.p2p.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "JwtRequestDto", description = "Dto of the Jwt Request")
public class JwtRequestDto implements Serializable {

    @Schema(type = "String", description = "email id of the User")
    private String email;
    
    @Schema(type = "String", description = "password of the User")
    private String password;
}
