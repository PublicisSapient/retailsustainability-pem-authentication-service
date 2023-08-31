package com.publicis.sapient.p2p.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "JwtResponseDto", description = "Dto of the Jwt Response")
@Data
public class JwtResponseDto implements Serializable {

    @Schema(type = "String", description = "Output Response of the User")
    private String output;
    @Schema(type = "String", description = "Response Status Code of the User")
    private String statusCode;
    @Schema(type = "String", description = "Response Error Message of the User")
    private String errorMessge;
}
