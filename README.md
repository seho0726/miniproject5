## 📚 걷기가 서재 - “작가의 산책” 
<br>

- 걷기가 서재의 “작가의 산책” 서비스는 누구나 작가가 되어 자유롭게 글을 집필하고 공개할 수 있는 창작 플랫폼 입니다.
- 작가의 산책의 특징은 작가의 감성과 이야기가 그대로 표지에 닿도록 설계된, 표지 제작 기능 기획자에게 가장 가까운 창작자의 시선을 제공합니다.
- 해당 플랫폼에서 사용자는 누구나 자신의 도서를 등록하고, 자유롭게 다른 작가들의 도서를 조회, 구매할 수 있습니다.

<br>

>
> Aivle School 4차 미니 프로젝트로,
> Spring Boot 기반 온라인 도서 서비스 백엔드 프로젝트입니다.
> Book / User / Order / Comment 등의 도메인으로 구성되어 있으며,
> JWT 기반 인증·인가를 제공합니다.

<br>

---

<br>

### 🚀 프로젝트 구조 (src/main/java)

```

com.aivle._th_miniProject
│  Application.java
│
├─book
│  ├─controller
│  │      BookController.java
│  ├─dto
│  │      BookCreateRequest.java
│  │      BookCreateResponse.java
│  │      BookDetailResponse.java
│  │      BookUpdateRequest.java
│  │      CoverUpdateRequest.java
│  │      CoverUpdateResponse.java
│  │      EntireBookResponse.java
│  │      StockUpdateRequest.java
│  │      StockUpdateResponse.java
│  ├─entity
│  │      Book.java
│  │      Category.java
│  ├─repository
│  │      BookRepository.java
│  └─service
│         BookService.java
│
├─comment
│  ├─controller
│  │      CommentController.java
│  ├─dto
│  │      CommentCreateRequest.java
│  │      CommentCreateResponse.java
│  ├─entity
│  │      Comment.java
│  ├─repository
│  │      CommentRepository.java
│  └─service
│         CommentService.java
│
├─order
│  ├─controller
│  │      OrderController.java
│  ├─dto
│  │      OrderCreateRequest.java
│  │      OrderResponse.java
│  ├─entity
│  │      Order.java
│  │      OrderItem.java
│  ├─repository
│  │      OrderRepository.java
│  │      OrderItemRepository.java
│  └─service
│         OrderService.java
│
├─cartItem
│  ├─controller
│  │      CartController.java
│  ├─dto
│  │      UpdateQuantityRequest.java
│  │      CartItemAddRequest.java
│  │      CartItemResponse.java
│  │      CartAllResponse.java
│  ├─entity
│  │      CartItem.java
│  ├─repository
│  │      CartItemRepository.java
│  └─service
│         CartService.java
│
├─user
│  ├─User.java
│  ├─UserController.java
│  ├─UserRepository.java
│  ├─UserService.java
│  ├─dtos
│  │      LoginRequest.java
│  │      LoginResponse.java
│  │      RefreshTokenRequest.java
│  │      SignupRequest.java
│  │      TokenResponse.java
│  │      UserResponse.java
│  └─jwt
│         JwtFilter.java
│         JwtUtil.java
│         SecurityConfig.java
│         SecurityUtil.java
│
└─config
          WebConfig.java

````

<br>

---

<br>

### 🔐 인증 / 인가 (JWT 기반)

- 로그인 시 `accessToken`, `refreshToken` 발급
- User.role = `NORMAL`, `ADMIN` 으로 구분
- ADMIN 전용 API 보호, 사용자 식별로 개별 API 보호
- Spring Security + JWT Filter 구성

`SecurityConfig.java`에 모든 인가 정책이 정의되어 있습니다.

<br>

---

<br>

## 📘 BOOK 도메인

### 📌 주요 기능
| 기능 | HTTP Method | Endpoint | 설명 |
|------|-------------|----------|------|
| 신규 도서 등록 | POST | `/book` | Book 생성 |
| 도서 전체 조회 | GET | `/book/all` | 카테고리 포함 전체 목록 조회 |
| 도서 상세 조회 | GET | `/book/{bookId}` | 개별 도서 상세 조회 |
| 도서 수정 | PUT | `/book/{bookId}` | 제목/저자/가격 업데이트 |
| 도서 삭제 | DELETE | `/book/{bookId}` | 도서 삭제 |
| AI 표지 재생성 | PATCH | `/book/{bookId}` | AI 도서표지 이미지 수정 |
| 재고 수정 | PUT | `/book/{bookId}/stock` | ADMIN 전용 도서 재고 업데이트 |


### 📂 구성 클래스
- Controller: `BookController`
- Service: `BookService`
- Repository: `BookRepository`
- Entity: `Book`, `Category`
- DTO: BookCreateRequest, BookDetailResponse, StockUpdateResponse, ...
<br>

---

<br>

## 💬 COMMENT 도메인

### 📌 주요 기능

| 기능    | Method | Endpoint            | 설명              |
| ----- | ------ | ------------------- | --------------- |
| 댓글 작성 | POST   | `/comment`          | 특정 도서에 대한 댓글 작성 |
| 댓글 삭제 | DELETE | `/comment/{bookId}` | 특정 댓글 삭제       |


### 📂 구성 클래스

* Controller: `CommentController`
* Service: `CommentService`
* Repository: `CommentRepository`, `OrderItemRepository`
* Entity: `Comment`
* DTO: `CommentCreateRequest`, `CommentCreateResponse`

<br>

---

<br>

## 📲 ORDER 도메인

### 📌 주요 기능

| 기능        | Method | Endpoint                   | 설명               |
| --------- | ------ | -------------------------- | ---------------- |
| 주문 생성     | POST   | `/orders`                  | 다중 Book 포함 주문 생성 |
| 주문 상세 조회  | GET    | `/orders/{orderId}`        | 특정 주문 정보 조회   |
| 주문 취소     | PUT    | `/orders/{orderId}/cancel` | 주문 상태 = CANCELED |
| 주문 결제완료 처리 | POST | `/orders/{orderId}/pay` | 주문 상태 = PAID |



### 📂 구성 클래스

* Controller: `OrderController`
* Service: `OrderService`
* Repository: `OrderRepository`, `OrderItemRepository`
* Entity: `Order`, `OrderItem`
* DTO: `OrderCreateRequest`, `OrderResponse`

<br>

---

<br>

## 🛒 CART 도메인

### 📌 주요 기능

| 기능        | Method | Endpoint                   | 설명               |
| --------- | ------ | -------------------------- | ---------------- |
| 장바구니 담기  | POST   | `/cart`        | 다중 Book 장바구니에 포함 |
| 장바구니 조회  | GET    | `/cart`        | 장바구니 상세 정보 조회   |
| 장바구니 수량 변경  | PATCH  | `/cart/{cartItemId}` | 장바구니 Item 수량 지정 |
| 장바구니 항목 삭제  | DELETE  | `/cart/{cartItemId}` | 장바구니 내 단일 Item 삭제 |
| 장바구니 전체 삭제  | DELETE    | `/cart`        | 장바구니 Item 전체 삭제  |


### 📂 구성 클래스

* Controller: `CartController`
* Service: `CartService`
* Repository: `CartItemRepository`
* Entity: `CartItem`
* DTO: `UpdateQuantityRequest`, `CartItemAddRequest`, `CartItemResponse`, ...


<br>

---

<br>

## 👤 USER 도메인

### 📌 주요 기능

| 기능      | Method | Endpoint        | 설명 |
| ------- | ------ | --------------- | -- |
| 회원가입    | POST   | `/user/signup`  |    |
| 로그인     | POST   | `/user/login`   |    |
| 로그아웃    | POST  | `/user/logout`   |    |
| 토큰 재발급  | POST   | `/auth/refresh` |  AccessToken 재발급  |
| 내 도서 조회 | GET | `/user/book/{userId}` | 본인 도서 목록 |
| 주문 내역 조회 | GET | `/user/order/{userId}` | 본인 주문 목록 |

### 📂 구성 클래스

* Controller: `UserController`
* Service: `UserService`
* Repository: `UserRepository`
* Entity: `User`
* DTO: `SignupRequest`, `LoginResponse`, `TokenResponse`
* JWT 패키지: `JwtUtil`, `JwtFilter`, `SecurityConfig`, `SecurityUtil`

<br>

---

<br>

### ⚙ Spring Config

`WebConfig.java` : CORS, 인코딩 설정 등 프로젝트 전역 설정 제공

<br>

---

<br>

## 🛠 기술 스택

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* H2
* Gradle

<br>

---

<br>

### 🧪 실행 방법

환경 설정 파일: `src/main/resources/application.yaml`

```
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:~/project
    username: sa
    password: ${PASSWORD}

  h2:
    console:
      enabled: true
      path: /h2-console

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true

jwt:
  secret: ${JWT_SECRET_KEY}
  access-expiration-ms: 900000
  refresh-expiration-ms: 604800000
```
- 필요한 항목 설정 후, 동작 가능합니다.

<br>

---

<br>

### 📎 ERD

<img width="1310" height="1080" alt="MiniProject4" src="https://github.com/user-attachments/assets/ea6594b0-d120-42eb-9dcd-f48e6ffb6bbc" />


<br>

---

<br>

# ✔ 향후 개선 예정

* Payment 도메인 추가
* 예외 공통 처리(ErrorResponse 통일)
* 관리자 기능 확장(Admin Dashboard API)
