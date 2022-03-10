package com.istiak.timezone.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private Boolean sysadmin;

    public static UserDTO of(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setUsername(user.getEmail());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setSysadmin(user.getSysadmin());

        return userDto;
    }
}
