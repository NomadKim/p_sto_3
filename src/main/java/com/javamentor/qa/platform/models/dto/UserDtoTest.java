package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoTest {

    private Long id;
    private String about;
    private String city;
    private String email;
    private String fullName;
    private String nickname;
    private String password;
}
