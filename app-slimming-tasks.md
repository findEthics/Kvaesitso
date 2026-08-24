# App Slimming Tasks

## Context

- Repository: Kvaesitso Android launcher fork.
- Goal: reduce APK size and maintenance cost using YAGNI; remove only features not used by the owner.
- Already removed: Wikipedia search and plugin/integration system, commit `78efd7ba8`.
- Release APK baseline: ~13.4 MB. About 57% is dex, 22% resources, 19% translations; native libraries are negligible.
- The bundled font remains intentionally unchanged. See the deferred font opportunity below.
- Verify each implementation task with:
  `ANDROID_HOME=~/Library/Android/sdk ./gradlew :app:app:assembleDefaultRelease`
- Verify the APK with:
  `~/Library/Android/sdk/build-tools/37.0.0/apksigner verify --verbose app/app/build/outputs/apk/default/release/app-default-release.apk`
- Implement each task as a separate commit where practical.

## Common Search-Feature Touchpoints

For each search feature removal, inspect and update:

- `settings.gradle.kts` and `app/app/build.gradle.kts` module includes/dependencies.
- `core/base/.../search/SearchFilters.kt` category fields and aggregate list.
- `app/ui/.../launcher/search/filters/` filter UI and serialization extensions.
- `services/search/.../SearchService.kt` and `Module.kt` service wiring.
- `core/preferences/.../search/`, `LauncherSettingsData.kt`, `LauncherDataStore.kt`, and preferences `Module.kt`.
- `app/ui/.../settings/search/` and feature-specific settings screens.
- `app/ui/.../launcher/search/<feature>/` result UI.
- Matching strings in `core/i18n/src/main/res/values*/strings.xml`.
- Add a preference migration when removing persisted settings; follow `Migration8.kt`.

## Task A: [x] Package English Only

Expected saving: approximately 2 MB.

- Add an English-only resource configuration in `app/app/build.gradle.kts`.
- Keep translation source files in the repository; only exclude them from the APK.
- Confirm locale configuration generation and English runtime behavior.

## Task B: [x] Remove Websites Search

Expected result: smaller dex and removal of `jsoup` and palette dependencies.

- Delete `data/websites`.
- Remove its Gradle module include and app dependency.
- Remove website search result UI, settings UI, preferences, service wiring, and filter category.
- Remove obsolete website strings and add any required preference migration.

## Task C: [x] Remove Places/Locations Search

Expected result: smaller dex and removal of OSM/address-formatting dependencies.

- Delete `data/locations` and `libs/address-formatter`.
- Remove their Gradle module includes and app dependencies.
- Remove location search UI, `MapTiles.kt`, OSM/location settings, preferences, service wiring, and filter category.
- Remove obsolete location strings and add any required preference migration.
- This removes Jackson, YAML, Mustache, and `osm-opening-hours` transitively used by the feature.

## Task D: [x] Remove Cloud File Providers

Keep local and Android Storage Access Framework file search. Do not remove the Notes widget.

- First confirm whether `services/accounts` is used only by Nextcloud/ownCloud. Keep it if other features require it.
- Remove `libs/nextcloud`, `libs/owncloud`, and `libs/webdav` only if no remaining feature needs them.
- Remove their Gradle includes and dependencies from `data/files`.
- Delete cloud provider implementations while retaining local, media, and SAF providers.
- Remove Nextcloud/ownCloud settings, account UI, preferences, service wiring, and obsolete strings.
- Confirm the Notes widget remains unchanged and functional.

Notes-widget safety: `data/widgets` has no cloud/files/accounts dependency. Notes use Android SAF `ContentResolver` APIs and persist a `content://` URI, so local and third-party document providers remain supported.

## Task E: [x] Keep Only Google Sans (Rounded)

Expected APK saving: zero while the current font file is retained. This is a picker simplification only.

- In `data/themes/.../typography/TypographyRepository.kt`, keep the `Google Sans (Rounded)` preset and make it the default.
- Optionally remove generic/device/serif/monospace choices from the typography picker.
- Keep `core/base/src/main/res/font/google_sans_flex.ttf`.
- Rounded is the same file as plain Google Sans with variable-font axis `ROND=100`; deleting presets does not delete font bytes.
- Add a theme migration only if existing saved typography selections must be purged rather than falling back.

## Task F: [x] Remove Dead Feed and Smartspacer

Both are currently disabled by `FeatureFlags` and hidden from users. Smartspacer is not wired into the clock render path.

- Remove `services/feed`, its Gradle include/dependency, feed settings/gesture wiring, and `FeatureFlags.feed`.
- Remove the Smartspacer SDK dependency, `SmartspacerPartProvider`, Smartspacer settings/integration UI, related preference, and `FeatureFlags.smartspacerIntegration`.
- Confirm no enabled user-facing path depends on either feature.

Feed is an optional swipe-in panel hosting an external launcher-overlay feed. Smartspacer is an optional external app integration for at-a-glance cards under the clock. Removing either has no current user impact.

## Task G: [x] Remove Debug Tools

Expected result: remove release-visible diagnostics while retaining automatic crash capture and its notification detail screen.

- Remove the Debug settings entry, Debug/Log/String Normalizer screens, navigation routes, and crash-report list screen.
- Remove log export, heap dumps, database cleanup, and forced icon-pack reinstallation; then remove their dead APIs.
- Keep `core:crashreporter`, its manifest provider, `CrashReportRoute`, and crash-report detail screen.
- Keep debug-build StrictMode initialization; it is separate from the Settings feature.
- Remove obsolete Debug strings and docs that advertise Settings > Debug or log export.
- Confirm Debug is absent in release and a crash notification still opens its report.

## Task H: [x] Remove Backup and Restore

Expected result: remove custom `.kvaesitso` archive import/export and its archive-only code.

- Delete `services/backup`; remove its Gradle include/dependencies and Koin module registration.
- Remove `Backupable`, archive participants/registrations, archive serialization, and `BackupRestoreDao`.
- Remove backup file-search handling, the Backup settings route/UI, icon, strings, and docs.
- Remove now-unused Gradle dependencies from widgets, custom attributes, and searchable data modules.
- Do not add a preference or Room migration; no persisted setting or database schema changes.
- Keep Android platform Auto Backup unless explicitly requested otherwise; it is separate from custom archive import/export.

## Deferred Opportunity: Trim the Font

Do not change this now. If further size reduction is needed, use offline font tooling to instance/subset `google_sans_flex.ttf` into a Rounded-only font:

- Pin `ROND=100`.
- Drop unused variable axes and narrow the supported weight range.
- Replace the bundled font only after checking typography rendering and licensing.
- This may save several MB but removes runtime font-axis flexibility, so it is deferred rather than part of Task E.

## Suggested Order

1. Task A: English-only resources.
2. Task E: typography picker simplification.
3. Task F: dead Feed/Smartspacer cleanup.
4. Task G: Debug tools.
5. Task H: Backup and restore.
6. Task B: Websites search.
7. Task C: Places/Locations search.
8. Task D: Cloud providers, after the accounts dependency check.
