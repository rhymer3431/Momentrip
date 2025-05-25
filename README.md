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

## 🖼️ 앱 화면 예시

| 홈 피드 | 상세화면 | 지도 + 시트 UI |
|---------|----------|----------------|
| ![](https://user-images.githubusercontent.com/24540801/156146601-3aeeb8b6-44ec-406b-a75c-0c8f8f9a0c12.png) | *(추가 예정)* | *(추가 예정)* |

---

## 📌 개발 과정

### 2025년 5월 25일
- `FeedScreen`에 카드 클릭 시 팝업 애니메이션 적용
- `PlaceDetailScreen` 내부 통합 및 전환 애니메이션 구현
- `ScheduleScreen`, `CalendarScreen`, `ChecklistScreen` UI 완성 및 구조 정리
- 전체 UI/UX 통합 및 경로 구조 `screen/도메인/파일` 방식으로 정비

### 2025년 5월 17일
- `RecommendService`에서 추천 유사도 계산을 `async-awaitAll`로 병렬 처리하여 성능 향상
- Word2Vec 기반 벡터 유사도 계산 개선

### 2025년 5월 15일
- `TourAPIService`에서 `detailInfo1` → `detailIntro1`으로 API 호출 변경
- `Place` 모델에 `intro` 기반 필드(`infoCenter`, `roomType` 등) 추가

### 2025년 5월 10일
- `Schedule.kt`에 `CheckItem` 리스트 추가
- `ScheduleCreationScreen`, `ChecklistScreen` 등 일정 생성 및 편집 UI 구현

### 2025년 5월 5일
- `ScheduleListScreen` 구현 및 기존 `UserViewModel`에서 `loadSchedules` 제거

### 2025년 4월 15일 ~ 17일
- 사용자 선호도 기반 추천 시스템 초기 설계
- `Question.kt`, `RecommendService.kt` 등 사용자 취향 분석 구조 구성

### 2025년 3월 24일
- 프로젝트 초기 커밋

