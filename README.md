# ✈️ MomenTrip - 나만의 여행 플래너

**MomenTrip**은 Jetpack Compose 기반으로 제작된 맞춤형 여행 일정 관리 앱입니다.  
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

## ⚙️ 실행 방법

1. 저장소 클론

   ```bash
   git clone https://github.com/your-username/momentrip.git
