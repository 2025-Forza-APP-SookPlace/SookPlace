# 로그아웃 API 및 RestaurantDetailResponse 수정 가이드

## 1. 로그아웃 API 현황

### ❌ 현재 상태: 로그아웃 API가 구현되어 있지 않음

**현재 구현**:
- `UserProfileRepository.logout()` 함수는 **로컬 DB만 삭제**합니다
- 실제 백엔드 API 호출은 없습니다
- `AuthApi` 인터페이스에 로그아웃 엔드포인트가 없습니다

**현재 코드 위치**:
- `app/src/main/java/com/example/sookplace/data/repository/UserProfileRepository.kt:54-56`
```kotlin
suspend fun logout() {
    userDao.clearUserProfile()  // 로컬 DB만 삭제
}
```

### ✅ 로그아웃 API 구현이 필요한 경우

백엔드에서 로그아웃 API를 제공한다면 다음 파일들을 수정해야 합니다:

#### 1.1 AuthApi.kt 수정
**파일 경로**: `app/src/main/java/com/example/sookplace/data/remote/api/AuthApi.kt`

```kotlin
interface AuthApi {
    // ... 기존 코드 ...
    
    //로그아웃
    @POST("auth/logout")  // 또는 @DELETE("auth/logout")
    suspend fun logout(): Response<Unit>
}
```

#### 1.2 UserProfileRepository.kt 수정
**파일 경로**: `app/src/main/java/com/example/sookplace/data/repository/UserProfileRepository.kt`

```kotlin
suspend fun logout() {
    try {
        // 백엔드 API 호출
        api.logout()  // AuthApi에 추가 필요
    } catch (e: Exception) {
        // 에러 처리 (네트워크 오류 등)
        e.printStackTrace()
    } finally {
        // 로컬 데이터 정리 (항상 실행)
        userDao.clearUserProfile()
        tokenManager.clearTokens()  // TokenManager 주입 필요
    }
}
```

#### 1.3 TokenManager 주입 필요
`UserProfileRepository`에 `TokenManager`를 주입해야 합니다:

```kotlin
class UserProfileRepository @Inject constructor(
    private val userDao: UserProfileDao,
    private val api: UserProfileApi,
    private val tokenManager: TokenManager  // 추가
) {
    // ...
}
```

---

## 2. RestaurantDetailResponse 응답 형식 수정

### 현재 응답 형식과 변경할 형식 비교

**현재 형식** (제거해야 할 필드):
- `geo: GeoCoordinates` ❌ 제거 필요
- `partnership: Partnership?` ❌ 제거 필요  
- `distanceMinutesFromCampus: Int` ❌ 제거 필요

**변경할 형식** (사용자가 원하는 형식):
```kotlin
data class RestaurantDetailResponse(
    val id: Int,
    val name: String,
    val category: String,
    val rating: Double,
    val reviewCount: Int,
    val likeCount: Int,
    val thumbnailUrl: String?,
    val images: List<String>,
    val address: String,
    val naverMapUrl: String,
    val openingHours: List<OpeningHour>,
    val phone: String,
    val menus: List<MenuItem>,
    val latestReviews: List<LatestReview>,
    val shareUrl: String
)
```

### 수정해야 할 파일 목록

#### ✅ 1. RestaurantDetailResponse.kt (응답 모델)
**파일 경로**: `app/src/main/java/com/example/sookplace/data/remote/response/RestaurantDetailResponse.kt`

**수정 내용**:
- `geo: GeoCoordinates` 필드 제거
- `partnership: Partnership?` 필드 제거
- `distanceMinutesFromCampus: Int` 필드 제거

**수정 후**:
```kotlin
data class RestaurantDetailResponse(
    val id: Int,
    val name: String,
    val category: String,
    val rating: Double,
    val reviewCount: Int,
    val likeCount: Int,
    val thumbnailUrl: String?,
    val images: List<String>,
    val address: String,
    val naverMapUrl: String,
    val openingHours: List<OpeningHour>,
    val phone: String,
    val menus: List<MenuItem>,
    val latestReviews: List<LatestReview>,
    val shareUrl: String
)
```

#### ✅ 2. RestaurantRepository.kt (더미 데이터 생성 부분)
**파일 경로**: `app/src/main/java/com/example/sookplace/data/repository/RestaurantRepository.kt`

**수정 내용**:
- 더미 데이터 생성 시 `geo`, `partnership`, `distanceMinutesFromCampus` 제거
- 불필요한 import 제거 (`GeoCoordinates`, `Partnership`)

**수정 전** (63-91번 라인):
```kotlin
return RestaurantDetailResponse(
    id = id,
    name = "숙명 한식당",
    category = "한식",
    rating = 4.5,
    reviewCount = 27,
    likeCount = 89,
    thumbnailUrl = "https://picsum.photos/id/102/400/300",
    images = listOf("https://picsum.photos/id/102/800/600", "https://picsum.photos/id/103/800/600"),
    distanceMinutesFromCampus = 5,  // ❌ 제거
    address = "서울 용산구 청파로 47",
    geo = GeoCoordinates(37.545, 126.97),  // ❌ 제거
    naverMapUrl = "https://map.naver.com/",
    partnership = Partnership("숙명 재학생", 20, "2024-12-31"),  // ❌ 제거
    openingHours = listOf(...),
    phone = "02-123-4567",
    menus = listOf(...),
    latestReviews = listOf(...),
    shareUrl = "https://sookplace.app/r/$id"
)
```

**수정 후**:
```kotlin
return RestaurantDetailResponse(
    id = id,
    name = "숙명 한식당",
    category = "한식",
    rating = 4.5,
    reviewCount = 27,
    likeCount = 89,
    thumbnailUrl = "https://picsum.photos/id/102/400/300",
    images = listOf("https://picsum.photos/id/102/800/600", "https://picsum.photos/id/103/800/600"),
    address = "서울 용산구 청파로 47",
    naverMapUrl = "https://map.naver.com/",
    openingHours = listOf(
        OpeningHour("Mon-Fri", "11:00-21:00"),
        OpeningHour("Sat-Sun", "12:00-20:00")
    ),
    phone = "02-123-4567",
    menus = listOf(
        MenuItem("김치찌개", 8000),
        MenuItem("된장찌개", 7500),
        MenuItem("비빔밥", 9000)
    ),
    latestReviews = listOf(
        LatestReview("https://picsum.photos/id/111/200/200", "맛집탐험가송이", "2024-12-14", "김치찌개 진짜 맛있어요!", 4.8, "정말 깔끔하고 맛있어서 자주 가는 곳이에요…")
    ),
    shareUrl = "https://sookplace.app/r/$id"
)
```

**Import 정리** (9-13번 라인):
```kotlin
// 제거할 import
import com.example.sookplace.data.remote.response.GeoCoordinates
import com.example.sookplace.data.remote.response.Partnership

// 유지할 import
import com.example.sookplace.data.remote.response.LatestReview
import com.example.sookplace.data.remote.response.MenuItem
import com.example.sookplace.data.remote.response.OpeningHour
import com.example.sookplace.data.remote.response.RestaurantDetailResponse
```

#### ✅ 3. RestaurantDetailActivity.kt (확인 필요)
**파일 경로**: `app/src/main/java/com/example/sookplace/ui/search/restaurantDetail/RestaurantDetailActivity.kt`

**확인 결과**: 
- ✅ `geo`, `partnership`, `distanceMinutesFromCampus` 필드를 사용하지 않음
- **수정 불필요**

#### ✅ 4. RestaurantDetailViewModel.kt (확인 필요)
**파일 경로**: `app/src/main/java/com/example/sookplace/ui/search/restaurantDetail/RestaurantDetailViewModel.kt`

**확인 결과**:
- ✅ `geo`, `partnership`, `distanceMinutesFromCampus` 필드를 사용하지 않음
- **수정 불필요**

#### ⚠️ 5. GeoCoordinates.kt 및 Partnership.kt (확인 필요)
**파일 경로**: 
- `app/src/main/java/com/example/sookplace/data/remote/response/GeoCoordinates.kt` (있다면)
- `app/src/main/java/com/example/sookplace/data/remote/response/Partnership.kt` (있다면)

**확인 사항**:
- 다른 곳에서 사용되는지 확인 필요
- 사용되지 않는다면 삭제 가능

---

## 📋 수정 체크리스트

### RestaurantDetailResponse 수정
- [ ] `RestaurantDetailResponse.kt`에서 `geo` 필드 제거
- [ ] `RestaurantDetailResponse.kt`에서 `partnership` 필드 제거
- [ ] `RestaurantDetailResponse.kt`에서 `distanceMinutesFromCampus` 필드 제거
- [ ] `RestaurantRepository.kt`의 더미 데이터에서 해당 필드들 제거
- [ ] `RestaurantRepository.kt`에서 불필요한 import 제거 (`GeoCoordinates`, `Partnership`)
- [ ] `GeoCoordinates`, `Partnership` 클래스가 다른 곳에서 사용되는지 확인
- [ ] 사용되지 않는다면 해당 파일 삭제

### 로그아웃 API 구현 (선택사항)
- [ ] 백엔드에 로그아웃 API가 있는지 확인
- [ ] `AuthApi.kt`에 로그아웃 엔드포인트 추가
- [ ] `UserProfileRepository.kt`에 `TokenManager` 주입
- [ ] `UserProfileRepository.logout()`에서 API 호출 추가
- [ ] 에러 처리 추가

---

## ⚠️ 주의사항

1. **더미 데이터 제거**: 백엔드 연동 시 `RestaurantRepository.kt`의 더미 데이터 생성 부분도 제거해야 합니다 (백엔드_연동_수정_가이드.md 참고).

2. **다른 사용처 확인**: `GeoCoordinates`와 `Partnership` 클래스가 다른 곳에서 사용되는지 확인해야 합니다. 예를 들어:
   - 다른 API 응답에서 사용
   - 다른 화면에서 사용
   - 매퍼 함수에서 사용

3. **빌드 오류**: 필드를 제거하면 컴파일 오류가 발생할 수 있으므로, 모든 사용처를 확인하고 수정해야 합니다.

4. **테스트**: 수정 후 실제 백엔드 API와 연동하여 테스트해야 합니다.

---

## 🔍 추가 확인 사항

### GeoCoordinates와 Partnership 사용처 확인

다음 명령어로 사용처를 확인할 수 있습니다:
```bash
# 프로젝트 루트에서 실행
grep -r "GeoCoordinates" app/src/
grep -r "Partnership" app/src/
```

만약 다른 곳에서 사용되지 않는다면, 해당 클래스 파일도 삭제할 수 있습니다.
