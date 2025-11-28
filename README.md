**🎬 MovieFlix**

A modern Android movie browsing app powered by Clean Architecture & Jetpack Compose.

**📸 Preview**

<table>
    <tr>
        <td align="center"><b>Movie List – Light</b></td>
        <td align="center"><b>Movie List – Dark</b></td>
    </tr>
    <tr>
        <td><img src="https://github.com/panmarg/movieFlix/blob/main/screenshots/Light%20Theme%20List.png" width="320" /></td>
        <td><img src="https://github.com/panmarg/movieFlix/blob/main/screenshots/Dark%20Theme%20List.png" width="320" /></td>
    </tr>
</table>

<table>
    <tr>
        <td align="center"><b>Movie Details – Light</b></td>
        <td align="center"><b>Movie Details – Dark</b></td>
    </tr>
    <tr>
        <td><img src="https://github.com/panmarg/movieFlix/blob/main/screenshots/Light%20Theme%20Details%20No%20Comments.png" width="320" /></td>
        <td><img src="https://github.com/panmarg/movieFlix/blob/main/screenshots/Dark%20Theme%20Details%20No%20Comments.png" width="320" /></td>
    </tr>
</table>

<table>
    <tr>
        <td align="center"><b>Comments Section – Light</b></td>
        <td align="center"><b>Comments Section – Dark</b></td>
    </tr>
    <tr>
        <td><img src="https://github.com/panmarg/movieFlix/blob/main/screenshots/Light%20Theme%20Comments.png" width="320" /></td>
        <td><img src="https://github.com/panmarg/movieFlix/blob/main/screenshots/Dark%20Theme%20Comments.png" width="320" /></td>
    </tr>
</table>


**✨ Features**

✔ Browse popular movies
✔ View detailed movie information
✔ Cast, reviews, similar movies
✔ Add/remove favorites
✔ Offline-first with local caching
✔ Native share support
✔ Light/Dark theme support
✔ Smooth image loading & caching

**🏛 Architecture**

MovieFlix follows Clean Architecture with a modular multi-module setup:

:app
:presentation
:domain
:data

**:app**

Application entry point

Navigation setup

Dependency injection configuration (Koin)

**:presentation**

Jetpack Compose UI

ViewModels

UI state + effects

**:domain**

Pure Kotlin module

Business models

Use cases

Repository interfaces

**:data**

Repository implementations

Room database + DAOs

Ktor API calls

DTOs & mappers

