# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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

[1.0.2]: https://github.com/muzso/fileslauncher/compare/1.0.1...1.0.2
[1.0.1]: https://github.com/muzso/fileslauncher/compare/1.0.0...1.0.1
[1.0.0]: https://github.com/muzso/fileslauncher/releases/tag/1.0.0
