# Stroll API 명세서

## 개요

Stroll API는 장소 공유 및 리뷰 서비스를 위한 RESTful API입니다.

## 인증

- JWT 토큰 기반 인증
- 인증이 필요한 API는 `auth.userId` 속성을 통해 사용자 확인
- 401 Unauthorized: 인증 실패
- 403 Forbidden: 권한 없음

---

## 1. 인증 (Auth)

### 1.1 로그인

사용자 로그인 및 JWT 토큰 발급

**Endpoint:** `POST /api/auth/login`

**Request Body:**

```json
{
  "userId": "string",
  "password": "string"
}
```

**Response (성공):**

```json
{
  "token": "string"
}
```

**Response (실패 - 401):**

```json
{
  "message": "아이디 또는 비밀번호가 올바르지 않습니다."
}
```

---

### 1.2 아이디 중복 확인

회원가입 시 아이디 사용 가능 여부 확인

**Endpoint:** `GET /api/auth/check-id/{userId}`

**Path Parameters:**

- `userId` (string): 확인할 사용자 ID

**Response:**

```json
{
  "userId": "string",
  "available": true
}
```

---

### 1.3 회원가입

새로운 사용자 등록

**Endpoint:** `POST /api/auth/register`

**Request Body:**

```json
{
  "userId": "string",
  "password": "string",
  "nickname": "string"
}
```

**Response (성공):**

```json
{
  "message": "Register Done"
}
```

**Response (실패 - 400):**

```json
{
  "message": "Wrong Access"
}
```

---

## 2. 장소 (Place)

### 2.1 장소 목록 조회

조건에 따른 장소 목록 조회 (검색, 필터링, 정렬)

**Endpoint:** `GET /api/places`

**Query Parameters:**

- `address` (string, optional): 주소
- `keywords` (string, optional, default: ""): 검색 키워드
- `order` (string, optional, default: "distance"): 정렬 기준 (distance, star 등)
- `page` (integer, optional, default: 1): 페이지 번호
- `maxDistance` (integer, optional, default: -1): 최대 거리 (미터)
- `minStar` (integer, optional, default: -1): 최소 별점
- `x` (double, optional): X 좌표 (경도)
- `y` (double, optional): Y 좌표 (위도)

**Response:**

```json
{
  "places": [
    {
      "no": 1,
      "title": "string",
      "category": "string",
      "guAddress": "string",
      "afterGuAddress": "string",
      "detailAddress": "string",
      "star": 4.5,
      "distance": 1200.5,
      "wished": false
    }
  ],
  "numOfPages": 10
}
```

---

### 2.2 장소 상세 조회

특정 장소의 상세 정보 조회

**Endpoint:** `GET /api/place/{placeNo}`

**Path Parameters:**

- `placeNo` (integer): 장소 번호

**Response:**

```json
{
  "no": 1,
  "userId": "string",
  "title": "string",
  "category": "string",
  "content": "string",
  "guAddress": "string",
  "afterGuAddress": "string",
  "detailAddress": "string",
  "star": 4.5,
  "wished": true,
  "x": 127.123456,
  "y": 37.123456
}
```

---

### 2.3 장소의 리뷰 목록 조회

특정 장소에 작성된 모든 리뷰 조회

**Endpoint:** `GET /api/place/{placeNo}/reviews`

**Path Parameters:**

- `placeNo` (integer): 장소 번호

**Response:**

```json
[
  {
    "no": 1,
    "userId": "string",
    "userNickname": "string",
    "placeNo": 1,
    "content": "string",
    "star": 5,
    "writtenDate": "2024-01-01T12:00:00"
  }
]
```

---

### 2.4 장소 등록

새로운 장소 등록 (인증 필요)

**Endpoint:** `POST /api/place`

**Request (multipart/form-data):**

- `imgs` (file[], optional): 이미지 파일들
- `placeName` (string): 장소 이름
- `address` (string): 주소
- `detailAddress` (string): 상세 주소
- `content` (string): 내용
- `category` (string): 카테고리

**Response (성공):**

```json
{
  "message": "Place-posting Success",
  "placeNo": 123
}
```

**Response (실패 - 401):**

```json
{
  "message": "로그인이 필요합니다."
}
```

---

### 2.5 장소 삭제

장소 삭제 (인증 필요, 작성자만 가능)

**Endpoint:** `DELETE /api/place/{placeNo}`

**Path Parameters:**

- `placeNo` (integer): 장소 번호

**Response (성공):**

```json
{
  "message": "Delete Done"
}
```

**Response (실패 - 401):**

```json
{
  "message": "로그인이 필요합니다."
}
```

**Response (실패 - 400):**

```json
{
  "message": "잘못된 접근입니다."
}
```

---

### 2.6 이미지 조회

S3에 저장된 이미지 조회

**Endpoint:** `GET /api/image/{image_title}`

**Path Parameters:**

- `image_title` (string): 이미지 파일명

**Response:**

- Content-Type: image/jpeg
- Body: 이미지 바이너리 데이터

---

## 3. 사용자 (User)

### 3.1 찜 목록 조회

사용자의 찜한 장소 목록 조회 (인증 필요)

**Endpoint:** `GET /api/users/{userId}/wishlist`

**Path Parameters:**

- `userId` (string): 사용자 ID

**Response (성공):**

```json
{
  "wishlist": [
    {
      "no": 1,
      "title": "string",
      "category": "string",
      "guAddress": "string",
      "afterGuAddress": "string",
      "detailAddress": "string",
      "star": 4.5,
      "distance": 1200.5,
      "wished": true
    }
  ]
}
```

**Response (실패 - 401):**

```json
{
  "message": "로그인이 필요합니다."
}
```

**Response (실패 - 403):**

```json
{
  "message": "접근 권한이 없습니다."
}
```

---

### 3.2 찜 목록에 추가

장소를 찜 목록에 추가 (인증 필요)

**Endpoint:** `POST /api/users/{userId}/wishlist`

**Path Parameters:**

- `userId` (string): 사용자 ID

**Request Body:**

```json
{
  "placeNo": 123
}
```

**Response (성공):**

```json
{
  "message": "찜 목록에 추가되었습니다."
}
```

**Response (실패 - 400):**

```json
{
  "message": "placeNo가 필요합니다."
}
```

**Response (실패 - 401):**

```json
{
  "message": "로그인이 필요합니다."
}
```

**Response (실패 - 403):**

```json
{
  "message": "접근 권한이 없습니다."
}
```

---

### 3.3 찜 목록에서 삭제

찜 목록에서 장소 삭제 (인증 필요)

**Endpoint:** `DELETE /api/users/{userId}/wishlist/{placeNo}`

**Path Parameters:**

- `userId` (string): 사용자 ID
- `placeNo` (integer): 장소 번호

**Response (성공):**

```json
{
  "message": "찜 목록에서 삭제되었습니다."
}
```

**Response (실패 - 401):**

```json
{
  "message": "로그인이 필요합니다."
}
```

**Response (실패 - 403):**

```json
{
  "message": "접근 권한이 없습니다."
}
```

---

### 3.4 사용자가 작성한 장소 목록 조회

사용자가 등록한 장소 목록 조회 (인증 필요)

**Endpoint:** `GET /api/users/{userId}/places`

**Path Parameters:**

- `userId` (string): 사용자 ID

**Response (성공):**

```json
{
  "places": [
    {
      "no": 1,
      "title": "string",
      "category": "string",
      "guAddress": "string",
      "afterGuAddress": "string",
      "detailAddress": "string",
      "star": 4.5,
      "distance": 1200.5,
      "wished": false
    }
  ]
}
```

**Response (실패 - 401):**

```json
{
  "message": "로그인이 필요합니다."
}
```

**Response (실패 - 403):**

```json
{
  "message": "접근 권한이 없습니다."
}
```

---

### 3.5 사용자가 작성한 리뷰 목록 조회

사용자가 작성한 리뷰 목록 조회 (인증 필요)

**Endpoint:** `GET /api/users/{userId}/reviews`

**Path Parameters:**

- `userId` (string): 사용자 ID

**Response (성공):**

```json
{
  "reviews": [
    {
      "no": 1,
      "userId": "string",
      "userNickname": "string",
      "placeNo": 1,
      "placeTitle": "string",
      "content": "string",
      "star": 5,
      "writtenDate": "2024-01-01T12:00:00"
    }
  ]
}
```

**Response (실패 - 401):**

```json
{
  "message": "로그인이 필요합니다."
}
```

**Response (실패 - 403):**

```json
{
  "message": "접근 권한이 없습니다."
}
```

---

### 3.6 회원 탈퇴

사용자 계정 삭제 (인증 필요)

**Endpoint:** `DELETE /api/users/{userId}`

**Path Parameters:**

- `userId` (string): 사용자 ID

**Request Body:**

```json
{
  "password": "string"
}
```

**Response (성공):**

```json
{
  "message": "회원 탈퇴가 완료되었습니다."
}
```

**Response (실패 - 400):**

```json
{
  "message": "비밀번호가 필요합니다."
}
```

**Response (실패 - 401 - 인증 필요):**

```json
{
  "message": "로그인이 필요합니다."
}
```

**Response (실패 - 401 - 비밀번호 불일치):**

```json
{
  "message": "비밀번호가 올바르지 않습니다."
}
```

**Response (실패 - 403):**

```json
{
  "message": "접근 권한이 없습니다."
}
```

---

## 4. 리뷰 (Review/Reply)

### 4.1 리뷰 작성

장소에 리뷰 작성 (인증 필요)

**Endpoint:** `POST /api/place/{placeNo}/reviews`

**Path Parameters:**

- `placeNo` (integer): 장소 번호

**Request Body:**

```json
{
  "content": "string",
  "star": 5
}
```

**Response (성공):**

```json
{
  "message": "리뷰가 작성되었습니다."
}
```

**Response (실패 - 400):**

```json
{
  "message": "content와 star가 필요합니다."
}
```

**Response (실패 - 401):**

```json
{
  "message": "로그인이 필요합니다."
}
```

---

### 4.2 리뷰 삭제

리뷰 삭제 (인증 필요, 작성자만 가능)

**Endpoint:** `DELETE /api/place/{placeNo}/reviews/{reviewNo}`

**Path Parameters:**

- `placeNo` (integer): 장소 번호
- `reviewNo` (integer): 리뷰 번호

**Response (성공):**

```json
{
  "message": "리뷰가 삭제되었습니다."
}
```

**Response (실패 - 401):**

```json
{
  "message": "로그인이 필요합니다."
}
```

---

## HTTP 상태 코드

- `200 OK`: 요청 성공
- `400 Bad Request`: 잘못된 요청 (필수 파라미터 누락 등)
- `401 Unauthorized`: 인증 실패 또는 인증 필요
- `403 Forbidden`: 권한 없음 (다른 사용자의 자원 접근 시도 등)
- `404 Not Found`: 리소스를 찾을 수 없음

---

## API 그룹 요약

### 인증 API (Auth)

- POST `/api/auth/login` - 로그인
- GET `/api/auth/check-id/{userId}` - 아이디 중복 확인
- POST `/api/auth/register` - 회원가입

### 장소 API (Place)

- GET `/api/places` - 장소 목록 조회
- GET `/api/place/{placeNo}` - 장소 상세 조회
- GET `/api/place/{placeNo}/reviews` - 장소의 리뷰 목록 조회
- POST `/api/place` - 장소 등록 ⚠️ 인증 필요
- DELETE `/api/place/{placeNo}` - 장소 삭제 ⚠️ 인증 필요
- GET `/api/image/{image_title}` - 이미지 조회

### 사용자 API (User)

- GET `/api/users/{userId}/wishlist` - 찜 목록 조회 ⚠️ 인증 필요
- POST `/api/users/{userId}/wishlist` - 찜 목록에 추가 ⚠️ 인증 필요
- DELETE `/api/users/{userId}/wishlist/{placeNo}` - 찜 목록에서 삭제 ⚠️ 인증 필요
- GET `/api/users/{userId}/places` - 사용자가 작성한 장소 목록 조회 ⚠️ 인증 필요
- GET `/api/users/{userId}/reviews` - 사용자가 작성한 리뷰 목록 조회 ⚠️ 인증 필요
- DELETE `/api/users/{userId}` - 회원 탈퇴 ⚠️ 인증 필요

### 리뷰 API (Review)

- POST `/api/place/{placeNo}/reviews` - 리뷰 작성 ⚠️ 인증 필요
- DELETE `/api/place/{placeNo}/reviews/{reviewNo}` - 리뷰 삭제 ⚠️ 인증 필요

---

## 참고 사항

1. **인증 방식**: JWT 토큰 기반 인증을 사용합니다.
2. **응답 형식**: 모든 응답은 JSON 형식입니다 (`application/json;charset=UTF-8`).
3. **에러 처리**: 에러 발생 시 적절한 HTTP 상태 코드와 함께 에러 메시지를 반환합니다.
4. **권한 검증**: 사용자 관련 API는 요청한 사용자와 인증된 사용자가 일치하는지 검증합니다.
5. **이미지 업로드**: 장소 등록 시 이미지는 AWS S3에 저장됩니다.
