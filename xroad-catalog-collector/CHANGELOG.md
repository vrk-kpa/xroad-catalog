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

## [2.1.3] - 2022-11-17

# Changed
- fixed getServiceStatistics and getDistinctServiceStatistics and getListOfServices so that the number of services changes in time as it should

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

## [2.0.1] - 2022-09-20

# Changed
- fix of major and critical Sonar issues

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
- bash script to automate update of project version

### Changed
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

## [0.11.9] - 2020-06-18

### Changed
- update version
- Fix GetService returning duplicates

## [0.11.8] - 2020-06-11

### Changed
- update version

## [0.11.7] - 2020-06-09

### Changed
- update version
- Add serviceVersion as input parameter to IsSoapService and IsRestService

## [0.11.6] - 2020-06-03

### Changed
- update version
- add multiple languages support to GetOrganizations and GetCompanies

## [0.11.5] - 2020-05-20

### Changed
- update version
- fix NPE for hasCompanyChanged and hasOrganizationChanged

## [0.11.4] - 2020-05-13

### Changed
- update version

## [0.11.3] - 2020-05-07

### Changed
- log errors in OrganizationUtil
- update documentation

## [0.11.3] - 2020-05-06

### Changed
- small refacto and example request files 
- unit tests for new repositories and catalog services

## [0.11.3] - 2020-05-05

### Changed
- create unit test for new actor and small refacto
- fetch companies on certain time set with parameters

## [0.11.2] - 2020-05-04

### Changed
- update version
- refacto ListMethodsActor
- save company details to db

## [0.11.2] - 2020-05-01

### Added
- save company and related details to db

## [0.11.1] - 2020-04-30

### Changed
- do not fetch organizations per each customer

## [0.11.1] - 2020-04-23

### Added
- unit test for new endpoints

## [0.11.1] - 2020-04-21

### Changed
- remove hard-coded url

## [0.11.1] - 2020-04-17

### Changed
- finalize collection of organizational data to db

## [0.11.1] - 2020-04-15

### Changed
- refacto CatalogService, FetchOrganizationUtil and sql script

## [0.11.1] - 2020-04-14

### Added
- save organizationAddress related data to db
- logic for fetching and saving organizations

## [0.11.1] - 2020-04-10

### Added
- new entities and repositories for fetching organizations

## [0.11.0] - 2020-03-26

### Changed
- fix host name issue

## [0.11.0] - 2020-03-23

### Changed
- update documentation

## [0.11.0] - 2020-03-20

### Changed
refacto IsSoapProvider, IsRestProvider and getOpenAPI
refacto ListMethodsActor to also receive REST services

## [0.10.0] - 2020-03-17

### Changed
- update project version
- revert dependencies update

### Added
- add changelogs
- create getOpenApi metaservice support

## [0.10.0] - 2020-02-14

### Added

- ClientListUtil class for ListClientsActor to handle XRoad v622 response correctly

### Changed

- Update ListClientsActor to handle XRoad v622 response correctly
- Update related unit tests
- Use POST for getWSDL request
- Add timeouts to XroadClient
- Update project version
- Update metaservices wsdl. Use WSDL defined in metaservices protocol documentation
- Update dependencies
- update documentation. change xroad-catalog-lister build user jenkins -> root

## [0.9.1.1] - 2017-07-05

### Changed

- Increase catalog collector package version


## [0.9.0.1] - 2017-02-08

### Added

- JavaDoc for parameters
- SSL SocketFactory for request context
- common gradle root build
- out/ and .gradle  to .gitignore
- pool SupervisorStrategies
- dead letters logging
- read and connect timeout to RestOperations

### Changed

- project dependencies from jar dependencies to module dependencies
- changed list-clients.xsd so that generated jaxb entities (ListClients) are XmlRootElement, 
  and xml will be validated when unmarshalling. This adds detection of such errors when listClients returns 
  non-valid message (eg. configuration outdated) and prevents problem with bad changed-timestamps. added tests.
- improved logging  
- moved example messages to src/main/doc
- Read ssl system properties from conf file
- actorSelection is no longer used for fetching actor-actor ActorRefs
- longer timeout in ApplicationConfiguration is ok since we just want to prevent complete lockups (and not hide timeouts from xroad)

### Fixed

- SSL parameter names in conf files
- fix for a task by adding if clause to FetchWsdlActor buildUri method
- incorrect ListMethods query
