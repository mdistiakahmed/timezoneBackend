package com.istiak.timezone.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private Boolean sysadmin;

    public static UserDTO of(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setUsername(user.getEmail());
        userDto.setSysadmin(user.getSysadmin());

        return userDto;
    }

    /**
     * Convert SignUp model to UserDto class.
     * Make sys admin as false during signup
     * @param userSignUpModel
     * @return
     */
    public static UserDTO of(UserSignUpModel userSignUpModel) {
        UserDTO userDto = new UserDTO();
        userDto.setUsername(userSignUpModel.getUsername());
        userDto.setPassword(userSignUpModel.getPassword());
        userDto.setSysadmin(false);

        return userDto;
    }
}
