package ru.buisnesslogiclab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private String username;
    private String password;
    private String role;
}
