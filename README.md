![image](https://github.com/user-attachments/assets/fe286e13-818c-46aa-9589-7292bd78718b)# ✈️ MomenTrip - Personalized Travel Planner

**MomenTrip** is a personalized travel planning Android application built with **Jetpack Compose**, **Kotlin**, and powered by **Kakao Map API** and **KTO TourAPI**. It helps users explore curated recommendations for attractions, food, and accommodations tailored to their preferences.

<p align="left">
  <a href="https://developer.android.com/jetpack/compose">
    <img src="https://img.shields.io/badge/Jetpack%20Compose-1.7.2-brightgreen" />
  </a>
  <a href="https://kotlinlang.org/">
    <img src="https://img.shields.io/badge/Kotlin-1.9.23-blueviolet" />
  </a>
  <a href="https://developer.android.com/studio/releases/gradle-plugin#updating-gradle">
    <img src="https://img.shields.io/badge/AGP-8.9.0-orange" />
  </a>
  <a href="https://gitmoji.dev">
    <img src="https://img.shields.io/badge/gitmoji-%20😎%20🎒-FFDD67.svg" alt="Gitmoji">
  </a>
</p>

---

## 🧭 Features

- 🔍 **Personalized Recommendations**: 
  - Suggests tourist spots, restaurants, and hotels based on user preferences (e.g. themes, keywords, food).
- 🗺️ **Kakao Map Integration**:
  - Interactive maps with custom markers and animated routes.
- 📆 **Schedule Builder**:
  - Organize trips by day, add/edit/delete activities with ease.
- 📸 **Modern UI**:
  - Fully implemented with **Jetpack Compose**, supports dark/light mode.
- 📝 **Checklist and Memo Support**:
  - Attach to-dos and personal notes to your schedule.
- 🚀 **Offline-first Design**:
  - Local persistence and preloading of data using Room or shared preferences (optional).

---

## 🧱 Tech Stack

| Layer           | Library / Tool                      |
|----------------|-------------------------------------|
| Language        | Kotlin 1.9.23                       |
| UI              | Jetpack Compose 1.7.2               |
| Architecture    | MVVM, ViewModel, StateFlow          |
| Backend API     | KoreaTourAPI (공공데이터)           |
| Maps            | Kakao Map SDK                       |
| Animation       | Compose Animation, Orbital          |
| Image Loading   | Coil                                 |
| Dependency Mgmt | Gradle 8.4                           |
| Auth (optional) | Firebase Authentication             |

---

## 🖼️ Screenshots

| Feed | Detail | Map + Schedule |
|------|--------|----------------|
|![image](https://github.com/user-attachments/assets/6f9458d5-4579-47d2-8934-c8d97464949d)
|

---

## ⚙️ Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/momentrip.git
