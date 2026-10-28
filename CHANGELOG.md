# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.4] - 2026-10-28

### Added

- Added the "com.android.car.documentsui" package name to be used in the activity launcher too, since in AAOS 15 this is used for the Files app.
- Added the "com.android.documentsui.files.LauncherActivity" and "com.android.documentsui.ViewDownloadsActivity" activity (class)names to be used in the activity launcher too (seen in AAOS 15).

### Fixed

- The activity launching method that worked in AAOS 12, didn't work in AAOS 15. The latter requires that we specify the display for the new activity, and it must be the default display and not the virtual display of FilesLauncher.
- There was a bug: if getLaunchIntentForPackage() returned an Intent, we only tried to start its activity and didn't try the one from our PACKAGES array. Now we first try the activity from its return value and if didn't succeed, then we try the other activities in our list.
- Fixed a deprecation warning about setHeaderAction().

### Changed

- Updated dependencies to latest versions.
- Added a default app/proguard-rules.pro, because one of the dependency upgrades now requires it (previously I could build release APKs without it).
- Increased Java source and target compatibility to 17 from 1.8.
- Dependency version strings are now in gradle/libs.versions.toml.
- We now only display the collected error messages if no activity start succeeded.

## [1.0.3] - 2026-08-25

### Fixed

- .gitignore was buggy: the `gradle` directory and `gradlew*` scripts must be in the repo
- app's launcher icon (SVG) had many unnecessary groups, it's cleaner now

## [1.0.2] - 2024-11-21

### Changed

- The "Files" app in the Volvo EX30's 1.4.4 software update no longer has the com.android.documentsui.LauncherActivity activity exported, thus it cannot be launched by 3rd party apps. But the com.android.documentsui.files.FilesActivity activity still is (exported) and can be (launched by 3rd party apps) so I've added it to the list of activities to try, when the user presses the button.
- Made a couple of Android Studio suggested adjustments (e.g. removed jcenter() from build.gradle, upgraded com.android.tools.build:gradle dep from 8.5.1 to 8.5.2).

## [1.0.1] - 2024-08-10

### Added

- detailed error messages are displayed on a new screen (since toasts and notifications cannot contain longer texts)

### Changed

- all kinds of unexpected exceptions are caught while calling Android APIs during the effort to launch the "Files" app
- these error messages are displayed to the user

## [1.0.0] - 2024-08-01

### Added

- Initial release (feature complete and mostly stable)

[1.0.4]: https://github.com/muzso/fileslauncher/compare/1.0.3...1.0.4
[1.0.3]: https://github.com/muzso/fileslauncher/compare/1.0.2...1.0.3
[1.0.2]: https://github.com/muzso/fileslauncher/compare/1.0.1...1.0.2
[1.0.1]: https://github.com/muzso/fileslauncher/compare/1.0.0...1.0.1
[1.0.0]: https://github.com/muzso/fileslauncher/releases/tag/1.0.0
