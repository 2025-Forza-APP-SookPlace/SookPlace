# SookPlace 백엔드 API 명세서

## 기본 정보
- **Base URL**: `https://pseudofeverish-nonsympathizingly-cecily.ngrok-free.dev/`
- **인증 방식**: Bearer Token (JWT)
- **Content-Type**: `application/json` (일부 API는 `multipart/form-data`)

---

## 1. 인증 API (AuthApi)

### 1.1 회원가입
- **Method**: `POST`
- **Endpoint**: `/auth/signup`
- **인증 필요**: ❌
- **Request Body**:
```json
{
  "userId": "string",
  "nickname": "string",
  "email": "string",
  "password": "string"
}
```
- **Response**: `UserSignupResponse`
```json
{
  "id": "string",
  "userId": "string",
  "email": "string",
  "createdAt": "string"
}
```

### 1.2 로그인
- **Method**: `POST`
- **Endpoint**: `/auth/login`
- **인증 필요**: ❌
- **Request Body**:
```json
{
  "userId": "string",
  "password": "string"
}
```
- **Response**: `LoginResponse`
```json
{
  "accessToken": "string",
  "refreshToken": "string",
  "expiresIn": 3600,
  "user": {
    "id": "string",
    "userId": "string",
    "email": "string",
    "lastLoginAt": "string"
  }
}
```

---

## 2. 사용자 프로필 API (UserProfileApi)

### 2.1 내 프로필 조회
- **Method**: `GET`
- **Endpoint**: `/me`
- **인증 필요**: ✅
- **Query Parameters**: 없음
- **Response**: `UserProfileResponse`
```json
{
  "nickname": "string",
  "level": 1,
  "levelTitle": "string",
  "avatarUrl": "string"
}
```

---

## 3. 커뮤니티 API (CommunityApi)

### 3.1 피드 조회
- **Method**: `GET`
- **Endpoint**: `/posts`
- **인증 필요**: ✅
- **Query Parameters**:
  - `category` (String, optional): 카테고리 필터
  - `sort` (String, optional): 정렬 기준 (예: "createdAt,desc")
  - `page` (Int, required): 페이지 번호
  - `size` (Int, optional, default: 10): 페이지 크기
- **Response**: `CommunityResponse`
```json
{
  "content": [
    {
      "postId": "string",
      "author": {
        "userId": "string",
        "nickname": "string",
        "profileImageUrl": "string"
      },
      "category": "string",
      "title": "string",
      "excerpt": "string",
      "place": {
        "placeId": "string",
        "name": "string"
      },
      "rating": 4.5,
      "imageUrl": "string",
      "likeCount": 10,
      "commentCount": 5,
      "isBookmarked": true,
      "createdAt": "string",
      "displayTime": "string",
      "likedByMe": false
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 100,
  "totalPages": 10,
  "hasNext": true
}
```

### 3.2 게시글 단건 조회
- **Method**: `GET`
- **Endpoint**: `/posts/{postId}`
- **인증 필요**: ✅
- **Path Parameters**:
  - `postId` (String): 게시글 ID
- **Response**: `PostDetailResponse`
```json
{
  "postId": "string",
  "author": {
    "userId": "string",
    "nickname": "string",
    "profileImageUrl": "string"
  },
  "category": "string",
  "title": "string",
  "content": "string",
  "place": {
    "placeId": "string",
    "name": "string"
  },
  "partnership": false,
  "rating": 4.5,
  "images": ["string"],
  "likeCount": 10,
  "commentCount": 5,
  "isBookmarked": true,
  "createdAt": "string",
  "displayTime": "string",
  "comments": [
    {
      "commentId": "string",
      "author": {
        "userId": "string",
        "nickname": "string",
        "profileImageUrl": "string"
      },
      "content": "string",
      "createdAt": "string",
      "displayTime": "string"
    }
  ],
  "likedByMe": false
}
```

### 3.3 게시글 좋아요 생성/삭제
- **Method**: `POST`
- **Endpoint**: `/posts/{postId}/likes`
- **인증 필요**: ✅
- **Path Parameters**:
  - `postId` (String): 게시글 ID
- **Response**: `LikeResponse`
```json
{
  "postId": "string",
  "liked": true,
  "likeCount": 11
}
```

### 3.4 댓글 작성
- **Method**: `POST`
- **Endpoint**: `/posts/{postId}/comments`
- **인증 필요**: ✅
- **Path Parameters**:
  - `postId` (String): 게시글 ID
- **Request Body**:
```json
{
  "content": "string"
}
```
- **Response**: `WriteCommentResponse`
```json
{
  "commentId": "string",
  "postId": "string",
  "author": {
    "userId": "string",
    "nickname": "string",
    "profileImageUrl": "string"
  },
  "content": "string",
  "createdAt": "string",
  "displayTime": "string",
  "isOwner": true,
  "postCommentCount": 6
}
```

### 3.5 댓글 삭제
- **Method**: `DELETE`
- **Endpoint**: `/comments/{commentId}`
- **인증 필요**: ✅
- **Path Parameters**:
  - `commentId` (String): 댓글 ID
- **Response**: `204 No Content`

### 3.6 게시글 작성
- **Method**: `POST`
- **Endpoint**: `/posts`
- **인증 필요**: ✅
- **Content-Type**: `multipart/form-data`
- **Request Parts**:
  - `title` (RequestBody): 제목
  - `rating` (RequestBody): 평점
  - `content` (RequestBody): 내용
  - `restaurantId` (RequestBody): 식당 ID
  - `images` (List<MultipartBody.Part>, optional): 이미지 파일들
- **Response**: `PostWriteResponse`
```json
{
  "postId": "string",
  "title": "string",
  "contentPreview": "string",
  "rating": 4.5,
  "images": [
    {
      "thumbUrl": "string",
      "url": "string"
    }
  ],
  "restaurant": {
    "id": 1,
    "name": "string",
    "category": "string"
  },
  "author": {
    "id": "string",
    "nickname": "string",
    "avatarUrl": "string"
  },
  "likeCount": 0,
  "commentCount": 0,
  "createdAt": "string",
  "isLiked": false,
  "nextAction": {
    "detailUrl": "string"
  }
}
```

### 3.7 게시글 수정
- **Method**: `PATCH`
- **Endpoint**: `/posts/{postId}`
- **인증 필요**: ✅
- **Content-Type**: `multipart/form-data`
- **Path Parameters**:
  - `postId` (String): 게시글 ID
- **Request Parts**:
  - `title` (RequestBody): 제목
  - `content` (RequestBody): 내용
  - `rating` (RequestBody): 평점
  - `placeId` (RequestBody): 장소 ID
  - `removeImageIds` (List<String>, optional): 삭제할 이미지 ID 리스트
  - `images` (List<MultipartBody.Part>, optional): 새로 추가할 이미지 파일들
- **Response**: `PostWriteResponse` (3.6과 동일)

### 3.8 게시글 삭제
- **Method**: `DELETE`
- **Endpoint**: `/posts/{postId}`
- **인증 필요**: ✅
- **Path Parameters**:
  - `postId` (String): 게시글 ID
- **Response**: `204 No Content`

---

## 4. 식당 API (RestaurantApi)

### 4.1 오늘의 숙플레이스 조회 (홈 화면)
- **Method**: `GET`
- **Endpoint**: `/restaurants/featured`
- **인증 필요**: ✅
- **Response**: `FeaturedRestaurantsResponse`
```json
{
  "featuredRestaurants": [
    {
      "name": "string",
      "address": "string",
      "thumbnailUrl": "string",
      "isLiked": false
    }
  ]
}
```

### 4.2 식당 상세 조회
- **Method**: `GET`
- **Endpoint**: `/restaurants/{restaurantId}`
- **인증 필요**: ✅
- **Path Parameters**:
  - `restaurantId` (Int): 식당 ID
- **Response**: `RestaurantDetailResponse`
```json
{
  "id": 1,
  "name": "string",
  "category": "string",
  "rating": 4.5,
  "reviewCount": 100,
  "likeCount": 50,
  "thumbnailUrl": "string",
  "images": ["string"],
  "distanceMinutesFromCampus": 10,
  "address": "string",
  "geo": {
    "lat": 37.5665,
    "lon": 126.9780
  },
  "naverMapUrl": "string",
  "partnership": {
    "target": "string",
    "discountPercent": 10,
    "validUntil": "string"
  },
  "openingHours": [
    {
      "day": "Mon-Fri",
      "hours": "09:00-22:00"
    }
  ],
  "phone": "string",
  "menus": [
    {
      "name": "string",
      "price": 10000
    }
  ],
  "latestReviews": [
    {
      "imageUrl": "string",
      "authorNickname": "string",
      "createdAt": "string",
      "title": "string",
      "rating": 4.5,
      "contentSnippet": "string"
    }
  ],
  "shareUrl": "string"
}
```

---

## 5. 검색 API (SearchApi)

### 5.1 식당 검색
- **Method**: `GET`
- **Endpoint**: `/restaurants`
- **인증 필요**: ✅
- **Query Parameters**:
  - `keyword` (String, required): 검색 키워드
  - `category` (String, optional): 카테고리 필터
  - `page` (Int, required): 페이지 번호
  - `size` (Int, required): 페이지 크기
  - `sort` (String, optional): 정렬 기준
- **Response**: `SearchResponse`
```json
{
  "content": [
    {
      "id": 1,
      "name": "string",
      "address": "string",
      "rating": 4.5,
      "likeCount": 10,
      "thumbnailUrl": "string"
    }
  ],
  "totalElements": 100,
  "totalPages": 10,
  "page": 0,
  "size": 10
}
```

### 5.2 식당 정렬 조회
- **Method**: `GET`
- **Endpoint**: `/restaurants`
- **인증 필요**: ✅
- **Query Parameters**:
  - `category` (String, optional): 카테고리 필터
  - `sort` (String, optional): 정렬 기준
  - `dir` (String, optional): 정렬 방향
  - `page` (Int, required): 페이지 번호
  - `size` (Int, required): 페이지 크기
- **Response**: `SortResponse`
```json
{
  "totalElements": 100,
  "totalPages": 10,
  "currentPage": 0,
  "restaurants": [
    {
      "id": 1,
      "name": "string",
      "category": "string",
      "thumbnailUrl": "string",
      "rating": 4.5,
      "likeCount": 10,
      "isLiked": false,
      "distanceMinutesFromCampus": 10,
      "locationName": "string",
      "shareUrl": "string"
    }
  ]
}
```

---

## 6. 룰렛 API (RouletteSpinApi)

### 6.1 룰렛 돌리기
- **Method**: `POST`
- **Endpoint**: `/roulette/spin`
- **인증 필요**: ✅
- **Request Body**:
```json
{
  "mode": "string",
  "excludeIds": [1, 2, 3]
}
```
- **Response**: `RouletteSpinResponse`
```json
{
  "type": "restaurant",
  "restaurant": {
    "id": 1,
    "name": "string",
    "category": "string",
    "thumbnailUrl": "string",
    "rating": 4.5,
    "distanceMinutesFromCampus": 10
  },
  "category": {
    "key": "string",
    "name": "string"
  },
  "nextAction": {
    "detailUrl": "string",
    "searchUrl": "string"
  }
}
```

---

## ⚠️ 발견된 문제점

### 1. 게시글 삭제 API 엔드포인트 오류
**위치**: `app/src/main/java/com/example/sookplace/data/remote/api/CommunityApi.kt:83`

**문제**:
```kotlin
@DELETE("/posts/{postId}")  // ❌ 앞에 슬래시가 있음
```

**설명**: 
- Base URL이 이미 슬래시로 끝나므로 (`https://...ngrok-free.dev/`)
- 엔드포인트 앞에 슬래시를 추가하면 `//posts/{postId}`가 되어 잘못된 URL이 생성됩니다.
- 다른 모든 API 엔드포인트들은 슬래시 없이 정의되어 있습니다.

**수정 방법**:
```kotlin
@DELETE("posts/{postId}")  // ✅ 슬래시 제거
```

이 문제로 인해 게시글 삭제 API 호출 시 연결 오류가 발생할 수 있습니다.

---

## 참고사항

1. **인증**: 대부분의 API는 Bearer Token 인증이 필요합니다. `AuthInterceptor`가 자동으로 토큰을 헤더에 추가합니다.

2. **에러 처리**: 
   - 로그인 API는 `Response<LoginResponse>`를 반환하여 HTTP 상태 코드를 확인할 수 있습니다.
   - 일부 API는 `Response<T>`를 반환하여 에러 처리를 할 수 있습니다.

3. **타임아웃**: 
   - 연결 타임아웃: 30초
   - 읽기 타임아웃: 30초

4. **로깅**: `HttpLoggingInterceptor`가 활성화되어 있어 모든 요청/응답이 로그에 기록됩니다.
