BUILD & EXECUTION RULES

The agent must NEVER execute build or runtime commands.

Forbidden commands:
- gradlew
- gradlew assembleDebug
- gradlew build
- gradlew test
- adb commands
- emulator commands
- shell scripts that run builds

The agent must ONLY modify source files.

All builds, tests, and runtime execution are performed manually by the developer.