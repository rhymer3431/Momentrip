# ✈️ Momentrip - 나만의 여행 플래너

**Momentrip**은 Jetpack Compose 기반으로 제작된 맞춤형 여행 일정 관리 앱입니다.  
사용자의 취향과 지역을 바탕으로 관광지, 음식점, 숙소를 추천하고,  
직관적인 UI를 통해 여행 일정을 만들고 시각적으로 확인할 수 있습니다.

<p align="left">
  <img src="https://img.shields.io/badge/Jetpack%20Compose-1.7.2-brightgreen" />
  <img src="https://img.shields.io/badge/Kotlin-1.9.23-blueviolet" />
  <img src="https://img.shields.io/badge/Gradle-8.4-yellow" />
  <img src="https://img.shields.io/badge/AGP-8.9.0-orange" />
  <img src="https://img.shields.io/badge/gitmoji-%20😎%20🎒-FFDD67.svg" />
</p>

---

## 🌟 주요 기능

- 🔍 **맞춤형 장소 추천**
  - 사용자 선호도 기반 관광지, 음식점, 숙소 추천
- 🗺️ **카카오맵 연동**
  - 마커 및 경로 애니메이션을 포함한 지도 기반 UI
- 🗓️ **일정 생성 및 편집**
  - 날짜별 여행 일정 추가/삭제 및 시간 순 정렬
- 🎨 **모던 UI 디자인**
  - Jetpack Compose 기반 반응형 레이아웃 및 다크모드 대응
- 📝 **체크리스트 및 메모**
  - 여행 준비물 및 일정별 메모 기능 포함

---

## 🧱 사용 기술 스택

| 영역            | 기술 / 라이브러리                       |
|-----------------|------------------------------------------|
| 언어            | Kotlin 1.9.23                            |
| UI 프레임워크   | Jetpack Compose 1.7.2                    |
| 아키텍처        | MVVM + StateFlow                         |
| 지도            | Kakao Map SDK                            |
| API             | TourAPI (한국관광공사)                   |
| 애니메이션      | Orbital, Compose Animation               |
| 이미지 로딩     | Coil                                     |
| 의존성 관리     | Gradle 8.4, AGP 8.9.0                     |
| 상태 관리       | ViewModel, StateFlow                     |
| 기타            | Firebase 인증(선택), TFLite(예정)        |

---

## 👥 팀원 소개

| 이름     | 학번       | 역할        |
|----------|------------|-------------|
| 김진하   | 202135751  | 백엔드       |
| 윤재상   | 202130808  | 백엔드       |
| 노유정   | 202235036  | 프론트엔드   |
| 박찬우   | 202135774  | 프론트엔드   |
| 조민주   | 202235126  | 프론트엔드   |

- **백엔드**는 TourAPI 연동, 사용자 추천 알고리즘, 일정 저장 및 데이터 흐름을 담당했습니다.
- **프론트엔드**는 Jetpack Compose 기반 UI 구성, 지도 연동, 애니메이션 구현을 맡았습니다.

---

## 📌 개발 과정

### 2025년 6월 1일
- `PostCreateScreen`에 장소 선택 UI, 태그 입력, 설명 필드 구현
- `AiLoadingScreen`에 Lottie 애니메이션 적용 및 텍스트 구성

### 2025년 5월 31일
- `FeedScreen`에 검색 기능 추가 (`query`, `searchResult` 상태 관리)
- `PostCard`에 대표 이미지, 장소명, 좋아요 버튼 구성

### 2025년 5월 28일
- `ActivitySelectScreen` 구현
- `Place` 모델에 `DetailIntroItem` 필드 통합

### 2025년 5월 26일
- `ScheduleOverviewScreen`에서 지도 구성 분리 (`ScheduleMapView`)
- 카드, 시트, 버튼 레이아웃 수정

### 2025년 5월 25일
- `FeedScreen` 카드 클릭 시 팝업 전환 애니메이션 구현
- `PlaceDetailScreen`에 BottomSheet 구조 적용
- `ScheduleScreen`, `CalendarScreen`, `ChecklistScreen` 구조 정비
- 전체 UI 디렉터리 구조 `screen/도메인/파일` 형식 정리

### 2025년 5월 20일
- `ScheduleListScreen`에서 `TripOverviewScreen` 전환 구현

### 2025년 5월 17일
- `RecommendService`에서 유사도 계산 병렬 처리 (`async-awaitAll`)
- Word2Vec 벡터 연산 최적화

### 2025년 5월 16일
- DateRangePicker 형태의 달력 UI 구현

### 2025년 5월 15일
- `TourAPIService`에서 `detailIntro1` 필드 사용
- `Place`에 `infoCenter`, `roomType` 등 필드 추가

### 2025년 5월 12일
- `FeedScreen`에서 `PlaceDetailScreen` 팝업 애니메이션 구현
- `Orbital`, `updateTransition` 적용

### 2025년 5월 10일
- `Schedule.kt`에 `CheckItem` 리스트 필드 추가
- `ScheduleCreationScreen`, `ChecklistScreen` 구현

### 2025년 5월 7일
- 일정 클릭 시 `TripOverviewScreen` 전환 기능 구현

### 2025년 5월 5일
- `ScheduleListScreen` 구현
- `UserViewModel`에서 `loadSchedules` 제거

### 2025년 4월 15~17일
- 사용자 선호 기반 추천 구조 설계 (`Question`, `RecommendService` 등)

### 2025년 3월 24일
- 프로젝트 초기 커밋

