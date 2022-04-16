package com.istiak.timezone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "T_USER")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "boolean default false")
    private Boolean sysadmin;

    @JsonIgnore
    @Column(length = 100)
    @NotNull
    @Size(min = 1, max = 100)
    private String password;


    public static User of(UserCreateModel userCreateModel) {
        User user = new User();
        user.setEmail(userCreateModel.getEmail());
        user.setPassword(userCreateModel.getPassword());
        user.setSysadmin(userCreateModel.getSysadmin());

        return user;
    }


}
