# Changelog

All notable changes to this project will be documented in this file.

## [3.0.7] - 2023-11-17

# Changed
- Refactor ListMembers endpoint to have extra field "serviceType"

## [3.0.6] - 2023-11-09

# Changed
- Refactor fetch of companies

## [3.0.5] - 2023-11-07

# Changed
- Pull latest version from NIIS develop branch

## [3.0.4] - 2023-02-13

# Changed
- Bugfixes, updates to Systemd service spec files

## [3.0.3] - 2023-02-05

# Changed
- Bugfixes, dependency update

## [3.0.2] - 2023-01-28

# Changed
- Updated user guide
- Updated REST methods in ServiceController and OrganizationController to return HTTP200 with empty content instead of HTTP204

## [3.0.1] - 2023-01-26

# Changed
- Updated the method how CATALOG_PROFILE is provided and used in Dockerfile and Systemd service creation

## [3.0.0] - 2023-01-15

# Changed
- Changed implementation to allow usage of different Spring profiles so that the implementation would be different
- Updated dependencies
- Updated documentation
- 
## [2.1.3] - 2022-11-17

# Changed
- fixed getServiceStatistics and getDistinctServiceStatistics and getListOfServices so that the number of services changes in time as it should
- 
## [2.1.2] - 2022-11-15

# Changed
- fixed getServiceStatistics and getDistinctServiceStatistics so that the number of services changes in time as it should

## [2.1.1] - 2022-10-19

# Added
- added unit tests to improve test coverage

## [2.1.0] - 2022-10-05

# Added
- added new REST endpoint for listing REST endpoints for basic REST(without OPENAPI) service
- added new REST endpoint for listing REST endpoints for REST(also OPENAPI) service

# Changed
- Updated related documentation and schematics
- 
## [2.0.1] - 2022-09-20

# Changed
- fix of major and critical Sonar issues
- 
## [2.0.0] - 2022-08-02

# Changed
- major refacto of REST apis and SOAP endpoints to have start and end date in parameters instead of just one date(since)
- cleanup of all related code

# Added
- improvement of test coverage by adding new unit tests

## [1.4.1] - 2022-03-18

# Changed
- minor updates to listDescriptors and getOrganizations endpoints

## [1.4.0] - 2022-02-08

# Added
- added new REST endpoint for listing organization/company information
- added new REST endpoint for listing organization/company information changes

## [1.3.2] - 2021-12-15

# Changed
- update listErrors endpoint to allow null values in parameters

## [1.3.1] - 2021-12-06

# Changed
- increased max Java heap size
- set the collector to fetch only at certain time interval during the night

## [1.3.0] - 2021-10-27

# Added
- added new endpoint for listing security servers
- added new endpoint for listing descriptors (data about subsystems)

# Changed
- updated listErrors endpoint to support requests for organizations not only subsystems and added support for pagination

## [1.2.2] - 2021-09-22

### Changed
- updated heartbeat endpoint to return also information about last collection dates

## [1.2.1] - 2021-09-03

### Added
- new endpoint for listing errors for a given subsystem

## [1.2.0] - 2021-08-26

### Added
- new endpoint for distinct services statistics

## [1.1.3] - 2021-07-23

### Added
- added user guide

### Changed
- reduced logging on INFO level

## [1.1.2] - 2021-02-22

### Changed
- fixed GetService in CatalogService to also work when service_version is not null but empty

## [1.1.0] - 2021-02-09

### Changed
- fixed XRoad-Catalog Collector properties files access rights
- updated license in file headers
- substituted IsSoapService and IsRestService with GetServiceType
- refacto of GetServiceStatistics and GetServiceStatisticsCSV related to having REST services instead of OTHER services
- updated documentation

## [1.0.11] - 2020-12-07

### Changed
- fixed GetErrorLog response to have information about security server, xRoadInstance etc

## [1.0.9] - 2020-11-24

### Changed
- new field to GetListOfServices response to show XRoadInstance

## [1.0.7] - 2020-11-23

### Changed
- Changed username of xroad-catalog-collector and xroad-catalog-lister services

## [1.0.4] - 2020-11-23

### Added
- SpringDoc OpenAPI UI support to generate and serve OpenAPI documentation
- new endpoint to show heartbeat of X-Road-Catalog Lister
- bash script to automate update of project version

### Changed
- new field to GetListOfServices response to indicate if subsystem is active
- new field to GetListOfServices response to indicate if service is active
- new field to GetListOfServices response to indicate if member is provider
- new field to GetServiceStatistics response to show number of services, which are neither REST or Soap
- refacto error_log to have additional fields like xRoadInstance, memberClass, memberCode etc.
- update changelogs and documentation

## [1.0.3] - 2020-10-29

### Changed
- update changelogs and build version
- Unignore fetchCompanies integration test due to service unavailable

## [1.0.2] - 2020-10-29

### Changed
- update changelogs and build version
- Ignore fetchCompanies integration test due to service unavailable

## [1.0.1] - 2020-10-25

### Added
- OpenAPI description for REST endpoints

### Changed
- new endpoint for statistics
- refactored getServiceStatistics endpoint from POST to GET
- refactored getServiceStatisticsCSV endpoint from POST to GET
- refactored getListOfServices endpoint from POST to GET
- refactored getListOfServicesCSV endpoint from POST to GET
- update changelogs and documentation

## [1.0.0] - 2020-10-06

### Changed
- new endpoint for statistics
- new endpoint for statistics in CSV format
- new endpoint for members with related subsystems, services and security servers
- new endpoint for members with related subsystems, services and security servers in CSV format
- update license and file headers
- update changelogs and documentation

## [0.13.6] - 2020-08-27

### Changed
- new endpoint for errors
- update changelogs and documentation

## [0.13.5] - 2020-08-19

### Changed
- update version
- update changelogs and documentation

## [0.13.3] - 2020-08-11

### Changed
- update version
- Fix GetService returning only active services
- Fix fetch of rest services and serviceVersion NULL issue

## [0.13.1] - 2020-07-14

### Changed
- update version
- Change amount of max heap size in gradle.properties

## [0.13.0] - 2020-07-13

### Changed
- update version
- Solve memory issues by lazy fetching

## [0.12.9] - 2020-07-08

### Changed
- update version
- Replace Unicode control characters from GetOrganizations in order to avoid issues when converting to XML

## [0.12.7] - 2020-07-06

### Changed
- update version
- Refacto of GetCompanies

## [0.11.9] - 2020-06-18

### Changed
- update version
- Fix GetService returning duplicates

## [0.11.4] - 2020-05-13

### Changed
- update version

### Added
- added input validation to HasOrganizationChanged and HasCompanyChanged endpoints

## [0.11.3] - 2020-05-07

### Changed
- update documentation

## [0.11.3] - 2020-05-06

### Added
- small refacto and example request files
- some more tests for the new endpoints
- unit tests for new repositories and catalog services

## [0.11.3] - 2020-05-05

### Added
- jaxb conversions for new endpoints
- add new service endpoint
- new service endpoint for getting companies

## [0.11.3] - 2020-05-04

### Changed
- update version

## [0.11.2] - 2020-04-30

### Changed
- update version

## [0.11.1] - 2020-04-24

### Changed
- update documentation
- change file name

## [0.11.1] - 2020-04-22

### Added
- unit tests for new endpoints

## [0.11.1] - 2020-04-19

### Changed
- new endpoints HasOrganizationChanged

## [0.11.1] - 2020-04-17

### Changed
- finalize organization jaxb conversions
- new endpoint and related jaxb conversions
- finalize collection of organizational data to db

## [0.11.1] - 2020-04-15

### Changed
- refacto CatalogService, FetchOrganizationUtil and sql script

## [0.11.1] - 2020-04-14

### Added
- logic for fetching and saving organizations

## [0.11.1] - 2020-04-10

### Added
- new entities and repositories for fetching organizations

## [0.11.1] - 2020-04-06

### Changed
- update documentation

## [0.11.1] - 2020-04-03

### Changed
- update version
- add unit tests for new interfaces to persistence and lister 

### Added
- fix dependency version
- substitute try-catch in unit tests with junit5 feature

## [0.11.0] - 2020-04-02

### Added
- add new endpoint IsProvider and change names of IsRestProvider and IsSoapProvider

## [0.11.0] - 2020-03-31

### Added
- add subsystemcode to getService query to avoid duplicates
- add exception when service not found
- add missing openapi part from jaxbconverter

## [0.11.0] - 2020-03-25

### Changed
- refacto wsdl

## [0.11.0] - 2020-03-23

### Changed
- update documentation

## [0.11.0] - 2020-03-20

### Changed
- remove extra field from wsdl
- refacto IsSoapProvider and IsRestProvider

## [0.11.0] - 2020-03-19

### Changed
- refacto getOpenAPI

## [0.10.0] - 2020-03-12

### Added
- add new fields to ServiceEndpoint
- add getOpenApi metaservice to lister

## [0.10.0] - 2020-03-11

### Added
- create getOpenApi metaservice support in lister

## [0.10.0] - 2020-03-05

### Added
- add changelogs

### Changed
- update project version
- revert dependencies update

## [0.10.0] - 2020-02-14

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



