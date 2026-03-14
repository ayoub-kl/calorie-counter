# Gradle Wrapper Status

## Wrapper is **incomplete**

The Gradle wrapper in this project is **not** complete. The following is missing:

| File | Status |
|------|--------|
| `gradle/wrapper/gradle-wrapper.properties` | Present |
| `gradlew.bat` (Windows) | Present |
| `gradlew` (Unix/macOS/Linux) | Present |
| **`gradle/wrapper/gradle-wrapper.jar`** | **Missing** |

Without `gradle-wrapper.jar`, `gradlew` and `gradlew.bat` cannot run. The jar is a small bootstrap that downloads the full Gradle distribution using the URL in `gradle-wrapper.properties`.

## Why the jar is missing

`gradle-wrapper.jar` is a binary file. It **cannot be generated** by editing text files. It must be either:

1. Created by running `gradle wrapper` (or `./gradlew wrapper`) on a machine where Gradle is already installed, or  
2. Copied from another Android/Gradle project that has a working wrapper.

## How to complete the wrapper

**Option A — You have Gradle installed**

From the project root (`calorie_counter`):

```bash
gradle wrapper --gradle-version=8.2
```

This generates `gradle/wrapper/gradle-wrapper.jar` and updates scripts if needed.

**Option B — Use Android Studio (recommended)**

1. In Android Studio: **File → New → New Project**.  
2. Choose **Empty Activity** (or any template), same package name `com.calorieai.app` and location **outside** this repo.  
3. After the project is created, copy the **entire** `gradle/wrapper` folder from the new project (including `gradle-wrapper.jar` and `gradle-wrapper.properties`) into this project’s `gradle/wrapper` folder, overwriting if asked.  
4. Optionally align `gradle-wrapper.properties` (e.g. `distributionUrl`) with this project’s Gradle version (8.2).  
5. You can then delete the temporary new project.

**Option C — Open this project in Android Studio**

Open the `calorie_counter` folder as the project root. If Android Studio detects the missing jar, it may offer to create or fix the wrapper; accept if prompted. If not, use Option B to obtain the jar and copy it in.

## Gradle version

This project is configured for **Gradle 8.2** (see `gradle/wrapper/gradle-wrapper.properties`), which is the minimum required for Android Gradle Plugin 8.2.0.
