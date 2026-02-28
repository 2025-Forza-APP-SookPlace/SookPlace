# SookPlace 로컬 데이터베이스 명세서

## 개요

- **데이터베이스 타입**: Room Database (SQLite)
- **데이터베이스 이름**: `app_database.db`
- **데이터베이스 버전**: 1
- **스키마 내보내기**: 비활성화 (`exportSchema = false`)
- **용도**: 오프라인 캐싱, 북마크 저장, 사용자 프로필 캐싱, 마이 플레이스 저장

---

## 데이터베이스 구조

### 테이블 목록

1. `user_profile` - 사용자 프로필 정보
2. `RestaurantEntity` - 식당 정보 (캐싱용)
3. `PostEntity` - 북마크된 게시글
4. `featured_restaurants` - 오늘의 숙플레이스
5. `PlaceEntity` - 마이 플레이스 (사용자가 핀을 꽂은 식당)

---

## 1. 사용자 프로필 테이블 (user_profile)

### 엔티티: `UserProfileEntity`

**용도**: 
- 로그인한 사용자의 프로필 정보를 로컬에 캐싱
- UI에서 빠르게 프로필 정보 표시
- 로그인 시 저장된 사용자 ID를 유지하여 서버 동기화 시 사용

**테이블 구조**:
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| `id` | String | PRIMARY KEY | 사용자 내부 식별자 (로그인 시 서버에서 받은 ID) |
| `nickname` | String | NOT NULL | 닉네임 |
| `avatarUrl` | String | NOT NULL | 프로필 이미지 URL |
| `level` | Int | NOT NULL | 사용자 레벨 |
| `levelTitle` | String | NOT NULL | 레벨 타이틀 |
| `lastUpdated` | Long | NOT NULL | 마지막 업데이트 시간 (밀리초) |

**DAO 메서드** (`UserProfileDao`):
- `getUserProfile(): Flow<UserProfileEntity?>` - 프로필을 Flow로 조회 (UI 자동 업데이트)
- `getUserProfileOnce(): suspend UserProfileEntity?` - 프로필을 1회 조회
- `upsertUserProfile(profile: UserProfileEntity): suspend Unit` - 프로필 저장/업데이트 (REPLACE 전략)
- `clearUserProfile(): suspend Unit` - 프로필 전체 삭제 (로그아웃 시)

**활용 방식**:
- 로그인 시 서버 응답을 로컬에 저장
- `/me` API 호출로 1시간마다 자동 갱신
- UI는 Flow를 구독하여 실시간 업데이트

---

## 2. 식당 정보 테이블 (RestaurantEntity)

### 엔티티: `RestaurantEntity`

**용도**: 
- 식당 상세 정보를 로컬에 캐싱
- 오프라인에서도 식당 정보 조회 가능

**테이블 구조**:
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| `id` | Int | PRIMARY KEY | 식당 ID |
| `name` | String | NOT NULL | 식당 이름 |
| `category` | String | NOT NULL | 카테고리 |
| `address` | String | NOT NULL | 주소 |
| `menu` | String | NOT NULL | 메뉴 정보 (JSON 문자열로 저장 가능) |
| `avgRating` | Double | NOT NULL | 평균 평점 |
| `likeCount` | Int | NOT NULL | 좋아요 수 |
| `reviewCount` | Int | NOT NULL | 리뷰 수 |
| `imageUrl` | String | NOT NULL | 이미지 URL |
| `isLiked` | Boolean | NOT NULL | 좋아요 여부 |
| `distanceMinutesFromCampus` | Int | NOT NULL | 학교로부터 거리 (분) |
| `naverMapLink` | String | NULLABLE | 네이버 지도 링크 |
| `lastUpdated` | Long | NOT NULL | 마지막 업데이트 시간 |

**DAO 메서드** (`RestaurantDao`):
- `getAllData(): List<RestaurantEntity>` - 모든 식당 정보 조회
- `insert(restaurants: List<RestaurantEntity>): suspend Unit` - 식당 정보 저장/업데이트 (REPLACE 전략)
- `deleteAllData(): Unit` - 모든 식당 정보 삭제

**활용 방식**:
- 식당 상세 조회 시 로컬에 캐싱
- 오프라인 모드에서 캐시된 데이터 사용

---

## 3. 북마크 게시글 테이블 (PostEntity)

### 엔티티: `PostEntity`

**용도**: 
- 사용자가 북마크한 게시글을 로컬에 저장
- 마이 포스트 화면에서 북마크된 게시글 목록 표시

**테이블 구조**:
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| `postId` | String | PRIMARY KEY | 게시글 ID |
| `userId` | String | NOT NULL | 작성자 ID |
| `nickname` | String | NOT NULL | 작성자 닉네임 |
| `profileImageUrl` | String | NULLABLE | 작성자 프로필 이미지 URL |
| `title` | String | NOT NULL | 게시글 제목 |
| `excerpt` | String | NOT NULL | 게시글 요약 |
| `category` | String | NOT NULL | 카테고리 |
| `placeId` | String | NOT NULL | 장소 ID |
| `placeName` | String | NOT NULL | 장소 이름 |
| `rating` | Double | NOT NULL | 평점 |
| `imageUrl` | String | NULLABLE | 대표 이미지 URL |
| `likeCount` | Int | NOT NULL | 좋아요 수 |
| `commentCount` | Int | NOT NULL | 댓글 수 |
| `displayTime` | String | NOT NULL | 표시 시간 문자열 |

**DAO 메서드** (`PostDao`):
- `insertPost(post: PostEntity): suspend Unit` - 북마크 게시글 저장 (REPLACE 전략)
- `deletePost(postId: String): suspend Unit` - 북마크 게시글 삭제
- `getAllBookmarkedPosts(): Flow<List<PostEntity>>` - 모든 북마크 게시글 조회 (Flow)
- `isBookmarked(postId: String): suspend Boolean` - 북마크 여부 확인

**활용 방식**:
- 사용자가 게시글을 북마크하면 로컬에 저장
- 마이 포스트 화면에서 Flow를 구독하여 실시간 목록 표시
- 북마크 해제 시 로컬에서 삭제

---

## 4. 오늘의 숙플레이스 테이블 (featured_restaurants)

### 엔티티: `FeaturedRestaurantEntity`

**용도**: 
- 홈 화면의 "오늘의 숙플레이스" 정보를 로컬에 캐싱
- 서버에서 최신 정보를 받아와 로컬에 저장
- UI는 Flow를 통해 실시간 업데이트

**테이블 구조**:
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| `name` | String | PRIMARY KEY | 식당 이름 (PK로 사용) |
| `address` | String | NOT NULL | 주소 |
| `thumbnailUrl` | String | NOT NULL | 썸네일 이미지 URL |
| `isLiked` | Boolean | NOT NULL | 좋아요 여부 |

**DAO 메서드** (`FeaturedRestaurantDao`):
- `getAllFeatured(): Flow<List<FeaturedRestaurantEntity>>` - 모든 오늘의 숙플레이스 조회 (Flow)
- `insertAll(restaurants: List<FeaturedRestaurantEntity>): suspend Unit` - 오늘의 숙플레이스 저장/업데이트 (REPLACE 전략)
- `deleteAll(): suspend Unit` - 모든 데이터 삭제
- `refreshRestaurants(restaurants: List<FeaturedRestaurantEntity>): suspend Unit` - 트랜잭션으로 안전하게 전체 교체 (삭제 후 삽입)

**활용 방식**:
- 앱 시작 시 또는 주기적으로 서버에서 최신 정보를 가져와 로컬에 저장
- UI는 Flow를 구독하여 실시간 업데이트
- `refreshRestaurants()` 메서드로 트랜잭션을 사용하여 안전하게 전체 교체

---

## 5. 마이 플레이스 테이블 (PlaceEntity)

### 엔티티: `PlaceEntity`

**용도**: 
- 사용자가 핀을 꽂은 식당을 저장
- 마이 플레이스 화면에서 저장된 식당 목록 표시

**테이블 구조**:
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| `restaurantId` | Int | PRIMARY KEY | 식당 ID |
| `name` | String | NOT NULL | 식당 이름 |
| `category` | String | NOT NULL | 카테고리 |
| `thumbnailUrl` | String | NULLABLE | 썸네일 이미지 URL |
| `rating` | Double | NOT NULL | 평점 |
| `address` | String | NOT NULL | 주소 |
| `addedAt` | Long | NOT NULL | 추가된 시간 (밀리초, 기본값: 현재 시간) |

**DAO 메서드** (`PlaceDao`):
- `insertPlace(place: PlaceEntity): suspend Unit` - 마이 플레이스 추가 (REPLACE 전략)
- `deletePlace(restaurantId: Int): suspend Unit` - 마이 플레이스 삭제
- `getAllSavedPlaces(): Flow<List<PlaceEntity>>` - 모든 마이 플레이스 조회 (추가 시간 내림차순, Flow)
- `isPlaceSaved(restaurantId: Int): suspend Boolean` - 저장 여부 확인

**활용 방식**:
- 사용자가 식당에 핀을 꽂으면 로컬에 저장
- 마이 플레이스 화면에서 Flow를 구독하여 실시간 목록 표시
- 핀 제거 시 로컬에서 삭제

---

## 데이터베이스 접근 패턴

### 1. 싱글톤 패턴
- `AppDatabase`는 싱글톤으로 관리되어 앱 전체에서 하나의 인스턴스만 사용
- `@Volatile`과 `synchronized`를 사용하여 스레드 안전성 보장

### 2. Flow 기반 실시간 업데이트
- 대부분의 조회 메서드가 `Flow`를 반환하여 데이터 변경 시 UI 자동 업데이트
- `user_profile`, `PostEntity`, `FeaturedRestaurantEntity`, `PlaceEntity`에서 사용

### 3. Upsert 패턴
- `OnConflictStrategy.REPLACE`를 사용하여 중복 시 자동 업데이트
- 서버에서 받은 최신 데이터로 로컬 캐시 갱신

### 4. 트랜잭션 사용
- `FeaturedRestaurantDao.refreshRestaurants()`에서 `@Transaction` 사용
- 전체 데이터 교체 시 원자성 보장

---

## 데이터 동기화 전략

### 1. 사용자 프로필
- 로그인 시 서버 응답을 로컬에 저장
- 1시간마다 자동으로 서버에서 최신 정보 가져와 갱신 (`refreshIfNeeded()`)
- 로그아웃 시 로컬 데이터 삭제

### 2. 오늘의 숙플레이스
- 앱 시작 시 또는 주기적으로 서버에서 최신 정보 가져와 전체 교체
- `refreshRestaurants()` 메서드로 트랜잭션 처리

### 3. 북마크/마이 플레이스
- 사용자 액션에 따라 즉시 로컬에 저장/삭제
- 서버 동기화는 별도로 처리 (현재 코드에서는 확인되지 않음)

---

## ⚠️ 발견된 문제점 및 수정 내역

### ✅ 1. 사용되지 않는 엔티티/DAO 삭제 완료
**위치**: `CommunityPostEntity.kt`, `CommunityPostDao.kt`

**문제**:
- `CommunityPostEntity`와 `CommunityPostDao`가 정의되어 있지만
- 실제로 Repository나 다른 곳에서 사용되지 않음
- 데이터베이스에도 등록되지 않음

**수정 완료**:
- ✅ `CommunityPostEntity.kt` 파일 삭제
- ✅ `CommunityPostDao.kt` 파일 삭제

### ✅ 2. RestaurantDao의 비동기 처리 문제 수정 완료
**위치**: `RestaurantDao.kt:12, 18`

**문제**:
```kotlin
@Query("SELECT * FROM RestaurantEntity")
fun getAllData() : List<RestaurantEntity>  // ❌ suspend 아님

@Query("DELETE FROM RestaurantEntity")
fun deleteAllData()  // ❌ suspend 아님
```

**설명**:
- Room에서는 메인 스레드에서 직접 데이터베이스 작업을 하면 안 됩니다
- `suspend` 함수나 `Flow`를 사용해야 합니다
- 현재 코드는 메인 스레드에서 호출 시 `IllegalStateException` 발생 가능

**수정 완료**:
```kotlin
@Query("SELECT * FROM RestaurantEntity")
suspend fun getAllData(): List<RestaurantEntity>  // ✅ suspend 추가 완료

@Query("DELETE FROM RestaurantEntity")
suspend fun deleteAllData()  // ✅ suspend 추가 완료
```

### 4. 데이터베이스 버전 관리 부재
**위치**: `AppDatabase.kt:26`

**문제**:
- `version = 1`로 고정되어 있음
- 스키마 변경 시 Migration 처리가 없음
- `exportSchema = false`로 스키마 내보내기가 비활성화되어 있음

**영향**:
- 엔티티 구조 변경 시 앱 재설치 필요
- 프로덕션 환경에서 데이터 손실 가능

**권장 사항**:
- 스키마 변경 시 버전 증가 및 Migration 추가
- 개발 단계에서는 `exportSchema = true`로 설정하여 스키마 파일 생성

### ✅ 3. RestaurantEntity의 기본값 문제 주석 추가 완료
**위치**: `RestaurantEntity.kt`

**문제**:
```kotlin
@PrimaryKey
var id: Int = 0,  // ❌ 기본값이 0
```

**설명**:
- Primary Key가 0이면 실제 식당 ID와 충돌 가능
- Room에서 자동 생성되는 ID를 사용하려면 `@PrimaryKey(autoGenerate = true)` 사용
- 현재는 서버에서 받은 ID를 사용하므로 문제 없을 수 있으나, 기본값 0은 위험

**수정 완료**:
```kotlin
@PrimaryKey
var id: Int = 0, // ✅ 주의: 서버에서 받은 유효한 ID를 사용해야 함. 기본값 0은 실제 식당 ID와 충돌 가능
```

**참고**: 현재 `RestaurantEntity`는 실제로 사용되지 않으므로 큰 문제는 없으나, 향후 사용 시 주의 필요

### 4. 데이터베이스 버전 관리 부재 (참고사항)
**위치**: `AppDatabase.kt:26`

**문제**:
- `version = 1`로 고정되어 있음
- 스키마 변경 시 Migration 처리가 없음
- `exportSchema = false`로 스키마 내보내기가 비활성화되어 있음

**영향**:
- 엔티티 구조 변경 시 앱 재설치 필요
- 프로덕션 환경에서 데이터 손실 가능

**권장 사항**:
- 스키마 변경 시 버전 증가 및 Migration 추가
- 개발 단계에서는 `exportSchema = true`로 설정하여 스키마 파일 생성

---

## 데이터베이스 사용 예시

### 사용자 프로필 조회 및 갱신
```kotlin
// Repository에서
val userProfileFlow: Flow<UserProfileEntity?> = userDao.getUserProfile()

// 1시간마다 자동 갱신
suspend fun refreshIfNeeded() {
    val local = userDao.getUserProfileOnce()
    val now = System.currentTimeMillis()
    val oneHour = 60 * 60 * 1000L
    
    if (local == null || (now - local.lastUpdated) > oneHour) {
        refreshUserProfile()  // 서버에서 가져와 업데이트
    }
}
```

### 북마크 게시글 저장
```kotlin
// Repository에서
suspend fun saveBookmark(post: PostEntity) = postDao.insertPost(post)
suspend fun removeBookmark(postId: String) = postDao.deletePost(postId)
fun getBookmarks(): Flow<List<PostEntity>> = postDao.getAllBookmarkedPosts()
```

### 오늘의 숙플레이스 갱신
```kotlin
// Repository에서
suspend fun refreshFeaturedRestaurants() {
    val response = api.getFeaturedRestaurants()
    val entities = response.featuredRestaurants.map { /* 변환 */ }
    featuredDao.refreshRestaurants(entities)  // 트랜잭션으로 안전하게 교체
}
```

---

## 참고사항

1. **Room 데이터베이스 접근은 항상 백그라운드 스레드에서**
   - `suspend` 함수 또는 `Flow` 사용
   - 메인 스레드에서 직접 호출 금지

2. **Flow를 사용한 실시간 업데이트**
   - UI는 Flow를 구독하여 데이터 변경 시 자동 업데이트
   - `collectAsState()` 또는 `collectAsStateWithLifecycle()` 사용

3. **트랜잭션 사용**
   - 여러 작업을 원자적으로 처리해야 할 때 `@Transaction` 사용
   - `FeaturedRestaurantDao.refreshRestaurants()` 예시 참고

4. **데이터 동기화**
   - 로컬 캐시는 서버 데이터의 스냅샷
   - 중요한 데이터는 서버와 주기적으로 동기화 필요
