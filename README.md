# Quote Creator

A lightweight native Android app built with Kotlin and Jetpack Compose for creating quote images.

## Features (MVP)
- Input quote and author
- Simple preview of the formatted quote
- Basic UI with Material 3 styling
- Project uses a minimal dependency set for fast startup and low memory usage.

## Project structure
```
app/
  src/main/java/com/example/quoteapp/MainActivity.kt   # UI implementation
  src/main/res/values/strings.xml
  src/main/res/values/colors.xml
  src/main/res/values/themes.xml
  src/main/res/mipmap-anydpi-v26/ic_launcher.xml
  src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml
  AndroidManifest.xml
build.gradle.kts  # module build script
```
Root files:
- `settings.gradle.kts`
- `build.gradle.kts`
- `gradle/wrapper/gradle-wrapper.properties`

## Building
1. Install **Android Studio** (recommended) or the Android SDK with command‑line tools.
2. Open the project folder (`quote app`) as an Android project.
3. Let Android Studio download the Gradle wrapper and dependencies.
4. Run **Run → app** or execute `./gradlew assembleDebug` from the project root.

## Next steps
- Add aspect‑ratio selector and canvas size handling.
- Implement background selection (solid, gradient, image).
- Add export to PNG/JPEG using MediaStore.
- Populate template library and offline quote collection.
- Persist editor state with DataStore and add history/favorites.

Feel free to extend the UI and functionality according to the PRD.
