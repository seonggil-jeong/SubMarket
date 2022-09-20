package com.submarket.userservice.jpa.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userInfo")
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userSeq;

    @Column(nullable = false, length = 40, unique = true)
    private String userId;

    @Column(nullable = false, length = 200, unique = true)
    private String userPassword;

    @Column(nullable = false, length = 40)
    private String userName;

    @Column(nullable = false, length = 60, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String userAge;

    @Column(nullable = false, length = 30)
    private String userPn;

    @Column(nullable = false)
    private int userStatus;

    @Column(length = 70)
    private String userAddress;

    @Column(length = 80)
    private String userAddress2;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<SubEntity> subEntityList;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<LikeEntity> likeEntityList;
}
