package com.istiak.timezone.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "T_TIMEZONE")
public class Timezone {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    private String city;

    @Column(columnDefinition = "integer default 0")
    private Integer hourdiff;

    @Column(columnDefinition = "integer default 0")
    private Integer mindiff;

    @NotNull
    @Column
    private Long userId;

}
