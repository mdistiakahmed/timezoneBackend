package com.istiak.timezone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateModel {
    private String email;
    private String password;
    private Boolean sysadmin;
}
