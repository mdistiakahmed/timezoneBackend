package com.istiak.timezone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateModel {
    private String email;
    private Boolean sysadmin;

    public static UserUpdateModel of(User user) {
        UserUpdateModel userUpdateModel = new UserUpdateModel();
        userUpdateModel.setEmail(user.getEmail());
        userUpdateModel.setSysadmin(user.getSysadmin());

        return userUpdateModel;
    }
}
