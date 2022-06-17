package com.submarket.sellerservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSellerInfo {

    private int sellerSeq;

    @NotNull(message = "아이디를 입력해 주세요")
    private String sellerId;

    @Size(min = 8, max = 40, message = "비밀번호는 8 ~ 40 글자 사이로 입력해주세요")
    private String sellerPassword;

    @NotNull(message = "사업자 번호를 입력해주세요")
    private String businessId;

    @NotNull(message = "연락처를 입력해주세요")
    private String sellerPn;

    @Email(message = "Email 형식이 아닙니다")
    private String sellerEmail;

    @NotNull(message = "주소를 입력해주세요")
    private String sellerAddress;
    private String sellerAddress2;

    private String sellerHome;

    private String sellerName;

}
