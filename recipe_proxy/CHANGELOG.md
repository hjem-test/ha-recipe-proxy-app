# Changelog

All notable changes to this project will be documented in this file.

## [1.0.7] - 2026-10-17

### Fixed
- Updating failed with "base name (${BUILD_FROM}) should not be blank": newer Supervisor versions no longer pass `BUILD_FROM`, so the Dockerfile now defaults to Home Assistant's base image for the architecture being built

## [1.0.6] - 2026-10-17

### Fixed
- The recipe app could stay on Home Assistant's loading screen, or keep running an old version. Its service worker cannot work through the proxy (the rewritten pages and scripts never match their hashes), so `ngsw-worker.js` is now answered with Angular's safety worker, which removes the service worker and its caches

## [1.0.3] - 2025-12-14

### Changed
- Removed redundant panel_iframe setting for cleaner ingress integration

## [1.0.0] - 2025-11-22

### Added
- Initial release of Recipe Proxy add-on
- Proxy support for local recipe applications
- Configurable server URL
- Host pass-through option
- Real IP pass-through option
- Home Assistant ingress integration
- Support for aarch64, amd64, armhf, armv7, and i386 architectures
