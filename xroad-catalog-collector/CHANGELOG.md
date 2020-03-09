# Changelog

All notable changes to this project will be documented in this file.

## [0.10.0.1] - 2020-02-14

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







