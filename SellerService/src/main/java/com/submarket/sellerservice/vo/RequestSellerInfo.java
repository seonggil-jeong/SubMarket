package com.submarket.sellerservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "판매자 정보 관련 Request")
public class RequestSellerInfo {

    @Schema(description = "판매자 번호")
    private int sellerSeq;

    @Schema(description = "판매자 아이디", required = true, example = "sellerId")
    @NotNull(message = "아이디를 입력해 주세요")
    private String sellerId;

    @Schema(description = "판매자 비밀번호", required = true, example = "sellerPassword")
    @Size(min = 8, max = 40, message = "비밀번호는 8 ~ 40 글자 사이로 입력해주세요")
    private String sellerPassword;

    @Schema(description = "판매자 사업자 번호", required = true, example = "12345678A")
    @NotNull(message = "사업자 번호를 입력해주세요")
    private String businessId;

    @Schema(description = "판매 담당자 전화번호", required = true, example = "010-1234-1234")
    @NotNull(message = "연락처를 입력해주세요")
    private String sellerPn;

    @Schema(description = "판매자 이메일 정보", required = true, example = "sellerEmail@email.com")
    @Email(message = "Email 형식이 아닙니다")
    private String sellerEmail;

    @Schema(description = "판매자 주소 정보", required = true, example = "sellerAddress")
    @NotNull(message = "주소를 입력해주세요")
    private String sellerAddress;

    @Schema(description = "판매자 상세 주소", required = false, example = "sellerAddress2")
    private String sellerAddress2;

    @Schema(description = "판매자 홈페이지 URL", example = "submarket.com")
    private String sellerHome;

    @Schema(description = "담당자 이름", required = true, example = "sellerName")
    private String sellerName;

}
