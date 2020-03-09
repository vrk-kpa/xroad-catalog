# Changelog

All notable changes to this project will be documented in this file.

## [0.10.0.1] - 2020-02-14

### Changed

- Updated project version
- Updated dependencies
- Use POST for getWSDL request
- Updated documentation. changed xroad-catalog-lister build user jenkins -> root


## [0.1.3.1] - 2016-06-03

### Added

- MIT licence for Collector
- JCenter repo
- Build script and directory change
- Initial catalog lister WS with services list
- JavaDoc for parameters
- Sonar runner and Jacoco plugins
- common gradle root build

### Changed

- use Java7 syntax
- renamed exception class
- SOAP xml naming memberlist/members -> memberlist/member
- ApplicationTests and data.sql to check that test data is returned from the service
- project dependencies from jar dependencies to module dependencies
- moved example messages to src/main/doc
- updated README

### Fixed

- generic exceptions should never be thrown
- methods should not be empty
- utility classes should not have public constructors
- checkstyle errors
- minor sonar issues
- SSL parameter names in conf files
- handling wsdl as a cdata dom object

### Removed

- Unused imports and some unused fields
- Dead stores
- Unused test implementation ServiceRepository



