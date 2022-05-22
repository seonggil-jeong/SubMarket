package com.submarket.userservice.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subInfo")
@JsonIgnoreProperties({"user"})
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private UserEntity user;
}
