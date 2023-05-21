package com.classpick.springbootproject.entity;

import com.nimbusds.openid.connect.sdk.claims.Gender;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class User{

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private Long googleId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

}
