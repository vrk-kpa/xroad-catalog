# Changelog

All notable changes to this project will be documented in this file.

## 3.0.7 - 2023-11-17

- Refactor ListMembers endpoint to have extra field "serviceType"

## 3.0.6 - 2023-11-09

- Refactor fetch of companies

## 3.0.5 - 2023-11-07

- Pull latest version from NIIS develop branch

## 3.0.4 - 2023-02-13

- Bugfixes, updates to systemd service specs

## 3.0.3 - 2023-02-05

- Bugfixes, dependency update

## 3.0.2 - 2023-01-28

- Updated user guide
- Updated REST methods in ServiceController and OrganizationController to return HTTP200 with empty content instead of HTTP204

## 3.0.1 - 2023-01-26

- Updated the method how CATALOG_PROFILE is provided and used in Dockerfile and Systemd service creation
- 
## 3.0.0 - 2023-01-15
- Changed implementation to allow usage of different Spring profiles so that the implementation would be different
- Updated dependencies
- Updated documentation
- 
## 2.1.3 - 2022-11-17
- fixed getServiceStatistics and getDistinctServiceStatistics and getListOfServices so that the number of services changes in time as it should
- 
## 2.1.2 - 2022-11-15
- fixed getServiceStatistics and getDistinctServiceStatistics so that the number of services changes in time as it should

## 2.1.1 - 2022-10-19
- added unit tests to improve test coverage

## 2.1.0 - 2022-10-05
- added new REST endpoint for listing REST endpoints for basic REST(without OPENAPI) service
- added new REST endpoint for listing REST endpoints for REST(also OPENAPI) service
- Updated related documentation and schematics
- 
## 2.0.1 - 2022-09-20
- fix of major and critical Sonar issues

## 2.0.0 - 2022-08-02
- major refacto of REST apis and SOAP endpoints to have start and end date in parameters instead of just one date(since)
- cleanup of all related code
- improvement of test coverage by adding new unit tests

## 1.4.1 - 2022-03-18
- minor updates to listDescriptors and getOrganizations endpoints

## 1.4.0 - 2022-02-08
- added new REST endpoint for listing organization/company information
- added new REST endpoint for listing organization/company information changes

## 1.3.2 - 2021-12-15
- update listErrors endpoint to allow null values in parameters

## 1.3.1 - 2021-12-06
- increased max Java heap size
- set the collector to fetch only at certain time interval during the night

## 1.3.0 - 2021-10-27
- added new endpoint for listing security servers
- added new endpoint for listing descriptors (data about subsystems)
- updated listErrors endpoint to support requests for organizations not only subsystems and added support for pagination

## 1.2.2 - 2021-09-22
- updated heartbeat endpoint to return also information about last collection dates

## 1.2.1 - 2021-09-03
- added new endpoint for listing errors for a given subsystem

## 1.2.0 - 2021-08-26
- added new endpoint for distinct services statistics

## 1.1.3 - 2021-07-23
- added user guide and reduced logging on INFO level

## 1.1.2 - 2021-02-22
- fixed GetService in CatalogService to also work when service_version is not null but empty

## 1.1.0 - 2021-02-09
- fixed XRoad-Catalog Collector properties files access rights
- updated license in file headers
- substituted IsSoapService and IsRestService with GetServiceType
- refacto of GetServiceStatistics and GetServiceStatisticsCSV related to having REST services instead of OTHER services
- updated documentation

## 1.0.11 - 2020-12-07
- fixed GetErrorLog response to have information about security server, xRoadInstance etc

## 1.0.9 - 2020-11-24
- new field to GetListOfServices response to show XRoadInstance

## 1.0.7 - 2020-11-23
- Changed username of xroad-catalog-collector and xroad-catalog-lister services

## 1.0.4 - 2020-11-23
- New method to check if db is working
- Add bash script to automate update of project version
- refacto GetListOfServices to have new fields to indicate if subsystem is active, service is active and member is provider
- refacto GetServiceStatistics to have a new field to show number of services, which are neither REST or Soap
- refacto error_log to have additional fields like xRoadInstance, memberClass, memberCode etc.
- Update changelogs and build version

## 1.0.3 - 2020-10-29
- Update changelogs and build version
- Unignore fetchCompanies integration test due to service unavailable

## 1.0.2 - 2020-10-29
- Update changelogs and build version
- Ignore fetchCompanies integration test due to service unavailable

## 1.0.1 - 2020-10-25
- OpenAPI description for REST endpoints
- new endpoint for statistics
- refactored getServiceStatistics endpoint from POST to GET
- refactored getServiceStatisticsCSV endpoint from POST to GET
- refactored getListOfServices endpoint from POST to GET
- refactored getListOfServicesCSV endpoint from POST to GET
- update changelogs and documentation

## 1.0.0 - 2020-10-06
- new endpoint for statistics
- new endpoint for statistics in CSV format
- new endpoint for members with related subsystems, services and security servers
- new endpoint for members with related subsystems, services and security servers in CSV format
- update license and file headers
- update changelogs and documentation

## 0.13.6 - 2020-08-27
- new endpoint for errors
- update changelogs and documentation

## 0.13.5 - 2020-08-19
- update version
- update changelogs and documentation

## 0.13.3 - 2020-08-11
- update version
- Fix GetService returning only active services
- Fix fetch of rest services and serviceVersion NULL issue

## 0.13.0 - 2020-07-13
- update version
- Solve memory issues by lazy fetching

## 0.12.7 - 2020-07-06
- update version
- Refacto of GetCompanies

## 0.11.9 - 2020-06-18
- update version
- Fix GetService returning duplicates
- Fix IsSoapService and IsRestService returning duplicates

## 0.11.8 - 2020-06-11
- update version

## 0.11.7 - 2020-06-09
- update version
- Add serviceVersion as input parameter to IsSoapService and IsRestService

## 0.11.6 - 2020-06-03
- update version
- add multiple languages support to GetOrganizations and GetCompanies

## 0.11.5 - 2020-05-20
- update version

## 0.11.4 - 2020-05-13
- update version
- update changelogs

## 0.11.4 - 2020-05-13
- update version
- update changelogs

## 0.11.3 - 2020-05-07
- update documentation
- removed time from date checks in tests to fix issues related to timezones

## 0.11.3 - 2020-05-06
- unit tests for new repositories and catalog services

## 0.11.3 - 2020-05-05
- test data and a few tests
- create unit test for new actor and small refacto

## 0.11.3 - 2020-05-05
- save company details to db

## 0.11.2 - 2020-05-01
- save company and related details to db
- company related entities method to catalogService
- new entities and repositories
- create new db tables

## 0.11.1 - 2020-04-24
- update documentation related to new services

## 0.11.1 - 2020-04-23
- unit tests for new endpoints

## 0.11.1 - 2020-04-20
- refacto db tables creation so that tables would be created every time if they do not exist

## 0.11.1 - 2020-04-19
- new endpoints HasOrganizationChanged

## 0.11.1 - 2020-04-17
- finalize organization jaxb conversions 
- new endpoint and related jaxb conversions
- finalize collection of organizational data to db

## 0.11.1 - 2020-04-15
- refacto CatalogService, FetchOrganizationUtil and sql script

## 0.11.1 - 2020-04-14
- save organizationAddress related data to db
- logic for fetching and saving organizations

## 0.11.1 - 2020-04-10
- new entities and repositories for fetching organizations

## 0.11.1 - 2020-04-03
- add unit tests for new interfaces to persistence and lister

## 0.11.0 - 2020-04-02
- add new endpoint IsProvider and change names of IsRestProvider and IsSoapProvider

## 0.11.0 - 2020-03-31
- add subsystemcode to getService query to avoid duplicates

## 0.11.0 - 2020-03-26
- update serviceRepo to return only non-removed service

## 0.11.0 - 2020-03-23
- update documentation

## 0.11.0 - 2020-03-20
- refacto IsSoapProvider and IsRestProvider

## 0.10.0 - 2020-03-11
- create getOpenApi metaservice support

## 0.10.0 - 2020-03-05
- revert dependencies update
- add changelogs

## 0.10.0 - 2020-02-13
- update dependencies

## 0.10.0 - 2019-11-29
- document examples using gradlew

## 0.10.0 - 2018-01-23
- Update project version

## 0.9.1 - 2018-01-18
- use POST for getWSDL request. Refactoring

## 0.9.1 - 2017-02-06
- actorSelection is no longer used for fetching actor-actor ActorRefs

## 0.9.1 - 2016-06-03
- fix, handling wsdl as a cdata dom object

## 0.9.1 - 2016-05-26
- Fix for SSL parameter names in conf files

## 0.9.1 - 2016-05-23
- made common gradle root build. changed project dependencies from jar dependencies to module dependencies

## 0.9.1 - 2016-05-17
- Javadocs completed for parameters
- Javadoc parameter fixes
- Sonar runner and jacoco plugins

## 0.9.1 - 2016-05-13
- Fix for too deeply nested control flow
- Checkstyle fixes
- Fix for 'Trying to make little less complex'
- Removed empty private constructor
- javadocs
- Fix for 'Unused private fields should be removed'
- Fix for 'Dead stores should be removed'
- Fix for 'Utility classes should not have public constr… …
- Fix for sonar issues
- Fix for 'Methods should not be empty'

## 0.9.1 - 2016-05-12
- Removing unused code parts
- Some more unused fields removed

## 0.9.1 - 2016-05-09
- jcenter repo added
- License for persistence

## 0.9.1 - 2016-04-28
- Removed unused imports

## 0.9.1 - 2016-04-14
- collectordb.properties -> catalogdb.properties
- Config file ranames and reorganization

## 0.9.1 - 2016-04-13
- Style fixes, unsed imports etc.

## 0.9.1 - 2016-04-04
- fix saving of wsdl when service version is null. made service.service_version nullable in db. configured jpa entity columns as non-nullable when they should be so that saving null values fails in tests
- ListMembers now lists also deleted items, removed wsdl data and data hash from soap interface, made wsdl fetching from db uniform (always fetch wsdl)
  
## 0.9.1 - 2016-03-31
- improved logging

## 0.9.1 - 2016-03-30
- fix for db scripts
- quicksave

## 0.9.1 - 2016-03-22
- Fix for db init files

## 0.9.1 - 2016-03-21
- fix broken test
- fixed bug in wsdl saving
- changed saveWsdl() contract, add wsdl saving

## 0.9.1 - 2016-03-16
- Database init and creation

## 0.9.1 - 2016-03-14
- switched from Date -> LocalDateTime
- refactored tests
- use lombok equals and hashcode for consistency
- removed duplicate code
- bit of documentation, after reviewing explain plans
- refactor, use subsystemId for saveServices the same way as for saveWsdl
- made getAllMembers NOT fetch wsdls
- branch merge from master and fix conflicts
- fix indexes, removed data_hash. add test that will populate test data to database

## 0.9.1 - 2016-03-11
- disabled @EqualsAndHashCode annotations - possibly not needed - and caused a few problems
- added save wsdl
- Save members, subsystems and services and unit testing
- added saveServices(), subsystem.getActiveServices/getAllServices

## 0.9.1 - 2016-03-10
- added getWsdl() and more tests
- added test for save member -> saves subsystems correctly, fixed bugs related to that, cleanup
- member.getAllSubsystems() and member.getActiveSubsystems()
- all current tests pass

## 0.9.1 - 2016-03-09
- added findAllMembers and findAllActiveMembers sort of methods that take into account whether member is removed or not. Made tests pass
- added missing source file

## 0.9.1 - 2016-03-08
- one more working test case. Marking members / subsystems is functional
- cleanup TODOs and otherwise
- added new timestamp field "fetched", renamed "updated" -> "changed", some cleanup
- made status fields Embeddable
- work on merge save members

## 0.9.1 - 2016-02-17
- Renamed package lister -> persistence in xroad-catalog-persistence

## 0.9.1 - 2016-02-16
- Spring boot body for xroad catalog, akka setup, persistence

## 0.9.1 - 2016-02-04
- added findAll(updatedAfter), tests, database modifications (indexes etc)

## 0.9.1 - 2016-02-01
- Create README.md
- PVAYLADEV-312 added integration testing mechanism
- PVAYLADEV-312 added CatalogService and JPA magic for eager loading entity graph

## 0.9.1 - 2016-01-29
- removed IDEA file
- fix .gitignore
- added spring boot project with beginning of Spring Data JPA implementation, which connects to local postgresql
