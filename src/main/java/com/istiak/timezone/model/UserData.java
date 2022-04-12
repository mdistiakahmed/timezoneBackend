package com.istiak.timezone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private String username;
    private Boolean sysadmin;

    public static UserData of(User user) {
        UserData userData = new UserData();
        userData.setUsername(user.getEmail());
        userData.setSysadmin(user.getSysadmin());

        return userData;
    }
}
