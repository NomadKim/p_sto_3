package com.javamentor.qa.platform.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AuthenticationRequest {
    private String username;
    private String password;

    @JsonProperty(value="isRemember")
    private boolean isRemember;
}
