# GithubUserBrowser
An  application built that fetches and displays public GitHub users using the GitHub API.

## Overview

## Screenshots
<details>
<summary>here</summary>
<table>
<tr>
<th><img alt="home_scren.png" src="ss%2Fhome_scren.png" width="300"/></th>
<th><img alt="splash_light.png" src="ss%2Fsplash_light.png" width="300"/></th>
<th><img alt="dashboard_1.png" src="ss%2Fdashboard_1.png" width="300"/></th>
<th><img alt="dashboard_2.png" src="ss%2Fdashboard_2.png" width="300"/></th></tr>

<tr>
<th>Home Screen</th>
<th>Splash Screen</th>
<th>DashBoard</th>
<th>DashBoard</th>
</tr>

<tr>
<th><img alt="item_list1.png" src="ss%2Fitem_list1.png" width="300"/></th>
<th><img alt="product_detail1.png" src="ss%2Fproduct_detail1.png" width="300"/></th>
<th><img alt="delete_item1.png" src="ss%2Fdelete_item1.png" width="300"/></th>
<th><img alt="delete_item2.png" src="ss%2Fdelete_item2.png" width="300"/></th></tr> 

<tr>
<th>Item List</th>
<th>Product Details</th>
<th>Delete Item</th>
<th>Delete Item</th>
</tr>

<tr>
<th><img alt="edit_item.png" src="ss%2Fedit_item.png" width="300"/></th>
<th><img alt="edit2.png" src="ss%2Fedit2.png" width="300"/></th>
<th><img alt="edit3.png" src="ss%2Fedit3.png" width="300"/></th>
<th><img alt="edit4.png" src="ss%2Fedit4.png" width="300"/></th>
</tr>

<tr>
<th>Edit Item</th>
<th>Edit Item</th>
<th>Edit Item</th>
<th>Edit Item</th>
</tr> 

<tr>
<th><img alt="addItem1.png" src="ss%2FaddItem1.png" width="300"/></th>
<th><img alt="addItem1.png" src="ss%2FaddItem1.png" width="300"/></th>
<th><img alt="addItem2.png" src="ss%2FaddItem2.png" width="300"/></th>
<th><img alt="add_item3.png" src="ss%2Fadd_item3.png" width="300"/></th></tr>

<tr>
<th>Add Item</th>
<th>Add Item</th>
<th>Add Item</th>
<th>Add Item</th>
</tr>

<tr>
<th><img alt="report1.png" src="ss%2Freport1.png" width="300"/></th>
<th><img alt="offline.png" src="ss%2Foffline.png" width="300"/></th>
<th><img alt="add_item_err.png" src="ss%2Fadd_item_err.png" width="300"/></th>
<th><img alt="splash_dark.png" src="ss%2Fsplash_dark.png" width="300"/></th></tr>

<tr>
<th>Report Screen</th>
<th>OffLine Mode</th>
<th>Input Validation</th>
<th>Dark Mode</th>
</tr>

<tr>
<th><img alt="dashboard_dark1.png" src="ss%2Fdashboard_dark1.png" width="300"/></th>
<th><img alt="item_list_dark.png" src="ss%2Fitem_list_dark.png" width="300"/></th>
<th><img alt="add_item_dark.png" src="ss%2Fadd_item_dark.png" width="300"/></th>
<th><img alt="reports_dark.png" src="ss%2Freports_dark.png" width="300"/></th></tr>

<tr>
<th>Dark Mode</th>
<th>Dark Mode</th>
<th>Dark Mode</th>
<th>Dark Mode</th>
</tr>

</table>

</details>


## How to build and run the app
- clone the repo
- Prerequisites: Android Studio (latest stable), JDK 17, Android SDK 36.
- Open the project in Android Studio and let Gradle sync.
- Run on a device/emulator via the Run button.
- Optional CLI:
  - Build Debug: `./gradlew assembleDebug`

## Architecture overview
- Clean Architecture + MVVM with three layers:
  - Presentation: Jetpack Compose screens, ViewModels (StateFlow), Navigation3 for navigation, Material3 components, pull-to-refresh.
  - Domain: Use cases (`GetUsers`, `GetUserDetail`, `SearchUsers`, `ClearCache`), domain models (`User`, `UserDetail`), repository interface.
  - Data: Retrofit + OkHttp + Kotlinx Serialization (remote), Room (local, offline-first), DataStore (theme persistence), DTOs and mappers, repository implementation.
- Concurrency: Kotlin Coroutines + Flow.
- DI: Hilt with scoped bindings for API, DB, Repository, and ViewModels.
- Pagination: Cursor based using GitHub `since` with load more near list end.
- Search: GitHub search API with offline fallback (filters cached users when offline or rate limited).

## Key design decisions and trade-offs
- Navigation3 for a Compose first, type safe, backstack driven API (trade-off: newer API surface).
- Offline-first: Room caches network responses; UI remains usable offline; search falls back to cached filtering.
- State management: StateFlow + sealed UI states (loading, success, error, empty, pagination loading).
- Error handling: HTTP 403 (rate limit) mapped to a friendly message with cache fallback.
- Theming: Dark mode toggle persisted with DataStore (simple boolean preference).
- Simplicity: Single module app and minimal animations to prioritize architecture clarity.

## Known limitations / improvements
- Add unit and UI tests (ViewModels, use cases, repository, and key UI flows).
