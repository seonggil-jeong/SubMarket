package com.submarket.sellerservice.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "sellerInfo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sellerSeq;

    @Column(nullable = false, length = 40, unique = true)
    private String sellerId;

    @Column(nullable = false, length = 200)
    private String sellerPassword;

    @Column(nullable = false, length = 100, unique = true) // 사업자 등록 번호
    private String businessId;

    @Column(nullable = false, length = 40) // 사업자 전화번호
    private String sellerPn;

    @Column(nullable = false, length = 100, unique = true)
    private String sellerEmail;

    @Column(nullable = false, length = 100) // 사업자 주소
    private String sellerAddress;

    @Column(length = 150) // 사업자 주소 상세
    private String sellerAddress2;

    @Column(length = 100) // 사업자 홈페이지 url
    private String sellerHome;

    @Column(length = 30) // 담당자 이름
    private String sellerName;

    @Column(nullable = false, length = 10) // 사업자 활성화 상태 (1 : on , 0 : off)
    private int sellerStatus;

}
