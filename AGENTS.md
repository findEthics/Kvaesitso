# Kvaesitso

Android/Kotlin is a multi-module Gradle project. `docs/` is a separate npm/VitePress project; do not run npm from the repository root.

## Commands

- Build the local app variant: `./gradlew :app:app:assembleDefaultDebug`
- Build the CI nightly variant: `./gradlew assembleDefaultNightly` (requires CI signing secrets and `VERSION_CODE_OVERRIDE` for a signed release-like APK).
- Generate API docs: `./gradlew dokkaGenerateHtml`
- Build docs: `cd docs && npm ci && npm run docs:build`
- Docs deployment first generates Dokka output, then copies `build/dokka/html` to the ignored `docs/public/reference` directory.

## Structure and constraints

- `app/app` is the application entry; `LauncherApplication` starts Koin. Register a new Koin module there or it will not load.
- `app/ui` is the Compose UI module. `services/` exposes higher-level APIs; `data/` provides lower-level implementations, generally behind `core/base` interfaces.
- App variants combine `default`/`fdroid` flavors with `debug`/`release`/`nightly` build types. Never add signing material to the repository.
- CI uses JDK 17; Android modules target JVM 11. `compileSdk` is 37, `minSdk` 26, and `targetSdk` 36.
- `data/database` uses Room with KSP; keep its versioned `schemas/` output aligned with database changes.
- Keep `MathParser.org-mXparser` at 4.4.2: newer 5.x versions are not GPL-compatible.
