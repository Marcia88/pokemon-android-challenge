# Pokemon Android Challenge

Minimal README for the Android project in this workspace.

## Project

This is an Android application (Kotlin, Jetpack Compose) for the Pokemon Android Challenge. 
The project uses Gradle (Kotlin DSL) and the included Gradle wrapper. 
Instrumented UI tests live under `app/src/androidTest` and unit tests under `app/src/test`.

## Requirements

- JDK 11 or newer (match the project's Gradle/JDK settings)
- Android SDK and command-line tools
- Android Studio (recommended) or use the Gradle wrapper on the command line
- An Android device or emulator for instrumented tests (androidTest)

Make sure `local.properties` contains a correct `sdk.dir` pointing to your Android SDK.

## Quick start

Open the project in Android Studio (recommended) or use the Gradle wrapper from the repository root.

Build the project (debug):

```bash
./gradlew assembleDebug
```

Run unit tests:

```bash
# runs JVM unit tests
./gradlew test
```

Run instrumented tests (requires a connected device or emulator):

```bash
# runs all connected instrumented tests
./gradlew connectedAndroidTest
```

Run a single instrumentation test class (example uses `PokemonCardTest`):

```bash
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.pokemonapplication.presentation.ui.pokemon_card.PokemonCardTest
```

Note: Compose UI tests in `androidTest` (using `createComposeRule`) need an emulator or device and the appropriate Android Test Runner.

## Common commands

Clean and rebuild:

```bash
./gradlew clean assembleDebug
```

Install APK to a connected device (debug):

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Troubleshooting

- "SDK not found" or build errors: ensure `local.properties` contains `sdk.dir=/path/to/Android/Sdk`.
- Instrumented tests failing to run: make sure an emulator is running or a device is connected and authorized. You can use `adb devices` to verify.
- If Gradle fails with memory or daemon issues, try `./gradlew --no-daemon --max-workers=1 clean build`.

## Notes

- This README is intentionally minimal. If you'd like, I can add CI instructions (GitHub Actions), badges, or expand developer docs with architecture details.

## License / Contact

Project created for the Pokemon Android Challenge. For questions or improvements, open an issue or PR in the repository.

