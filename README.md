#  Sub Market (e-commerce)

### MSA기반의 구독 상품을 판매 및 구매할 수 있는 E-commerce 서비스입니다.

## Description

> 2022.05 - 2022.06

### 적용 기술

<div>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white" />
<img src="https://img.shields.io/badge/Flask-000000?style=for-the-badge&logo=Flask&logoColor=white" />
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white" /><br/>
<img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white" />
<img src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=MongoDB&logoColor=white" /><br/>
<img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=RabbitMQ&logoColor=white" />
<img src="https://img.shields.io/badge/Apache Kafka-231F20?style=for-the-badge&logo=Apache Kafka&logoColor=white" /><br/>
<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white"/>
<img src="https://img.shields.io/badge/Microsoft Azure-0078D4?style=for-the-badge&logo=Microsoft Azure&logoColor=white"/>
<img src="https://img.shields.io/badge/GCP-4285F4?style=for-the-badge&logo=Google Cloud&logoColor=white"/><br/>
</div>

> _**Other : JWT, JPA, Load Balancer(Eureka), Gateway(Spring Cloud Gateway), REST-API, JSP, Spring Cloud Config**_
<br/>
<br/>

https://user-images.githubusercontent.com/63443366/186517339-aa4913a6-1abe-4379-97b9-59f6e19896d8.mp4

<br/>

## Summary

### API Swagger
UserService : http://34.64.214.27:12000/swagger-ui
</br>
SellerService : http://34.64.214.27:13000/swagger-ui
</br>
OrderService : http://34.64.214.27:11000/swagger-ui
</br>
ItemService : http://34.64.214.27:10000/swagger-ui
</br>

### Zipkin
http://34.64.214.27:9411

![Summary](https://user-images.githubusercontent.com/63443366/175762264-d15dfadd-e097-4dd9-8294-c86e65a15691.png)

* **USER**
    * 구독 상품 구매

* **SELLER**
    * 상품 판매, 매출 정보 확인 및 사용자 주문 확인

* **Background**
    * 매달 사용자 구독을 갱신하는 기능
    * 판매자의 월별 매출 정보를 매월 말 저장
    * EDA (Event-driven architecture) with Kafka

### Kafka

![Kafka](https://user-images.githubusercontent.com/63443366/175763046-816a1b35-9df7-4249-965b-fa7ce7f31ccc.png)

* **상품 구독**
    * 사용자가 상품 구독 시 상품 수량 감소 및 주문 생성
    * https://github.com/JeongSeonggil/SubMarketWithGit/issues/129

* **상품 구독 취소**
    * 사용자가 상품 구독을 취소하면 수량 증가
    * https://github.com/JeongSeonggil/SubMarketWithGit/issues/132

* **매출 정보 분석 및 적재**
    * 매월 말 판매자 서비스에 월 별 판매량을 적재 및 분석 시 사용
    * https://github.com/JeongSeonggil/SubMarketWithGit/issues/137

* **사업자 탈퇴 시 판매 중인 상품 비활성화**
    * 판매자가 탈퇴한다면 판매자가 판매중이였던 상품 비활성화
    * https://github.com/JeongSeonggil/SubMarketWithGit/issues/141

## ERD
![image](https://user-images.githubusercontent.com/63443366/184500117-3e225474-5937-4399-8c10-ab70110219e3.png)
