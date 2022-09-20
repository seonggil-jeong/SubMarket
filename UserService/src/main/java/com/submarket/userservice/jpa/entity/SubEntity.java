package com.submarket.userservice.jpa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subInfo")
@ToString(exclude = "user")
@Builder
public class SubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subSeq;

    @Column(nullable = false)
    private int itemSeq;

    @Column
    private String subDate;

    @Column(nullable = false)
    private int subCount;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private UserEntity user;
}
