# Chip-8 Kotlin Multiplatform

![kotlin-version](https://img.shields.io/badge/kotlin-2.4.20-blue?logo=kotlin)

Kotlin Multiplatform fork of https://github.com/cbeust/chip-8
([Chip-8](http://www.cs.columbia.edu/~sedwards/classes/2016/4840-spring/designs/Chip8.pdf) emulator).  This project is also being used to try out Swift Export.

Current clients
* Android (Jetpack Compose)
* Wear (Compose for Wear)
* iOS (SwiftUI)
* Desktop (Compose for Desktop)
* Web (Wasm based Compose for Web)

The Android, Desktop and Web clients share a single Compose Multiplatform UI (`shared-ui`),
with a bundled rom library, a phosphor CRT style display, and pause/restart/speed controls.

Chip-8 machines have a 16 key hex keypad. Each bundled rom knows which of those keys it
actually uses, so the clients show meaningful controls rather than raw hex digits:

* **Keyboard** (desktop/web): the arrow keys and <kbd>Space</kbd> are mapped to the selected
  game's controls, and the hex keys <kbd>0</kbd>-<kbd>9</kbd> / <kbd>A</kbd>-<kbd>F</kbd>
  always map straight through (so Space Invaders still plays on <kbd>4</kbd> <kbd>5</kbd> <kbd>6</kbd>).
* **Touch**: on-screen buttons for the current game, or the full 4x4 hex keypad via the toggle
  in the app bar.

![Screenshots](/art/screenshots.png?raw=true)

![Screenshot_20230121_130752](https://user-images.githubusercontent.com/6302/213868342-cbca8ad6-38a1-4297-9dbc-ddcdbba513c3.png)


![F8epdW5WIAAenPD](https://github.com/joreilly/chip-8/assets/6302/26313c65-acf1-42c7-8af4-34415cae201b)



## Full set of Kotlin Multiplatform/Compose/SwiftUI samples

*  PeopleInSpace (https://github.com/joreilly/PeopleInSpace)
*  GalwayBus (https://github.com/joreilly/GalwayBus)
*  Confetti (https://github.com/joreilly/Confetti)
*  BikeShare (https://github.com/joreilly/BikeShare)
*  FantasyPremierLeague (https://github.com/joreilly/FantasyPremierLeague)
*  ClimateTrace (https://github.com/joreilly/ClimateTraceKMP)
*  GeminiKMP (https://github.com/joreilly/GeminiKMP)
*  MortyComposeKMM (https://github.com/joreilly/MortyComposeKMM)
*  StarWars (https://github.com/joreilly/StarWars)
*  WordMasterKMP (https://github.com/joreilly/WordMasterKMP)
*  Chip-8 (https://github.com/joreilly/chip-8)
*  FirebaseAILogicKMPSample (https://github.com/joreilly/FirebaseAILogicKMPSample)
