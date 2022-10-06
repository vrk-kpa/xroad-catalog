# xroad-catalog-lister

WebService to produce list of X-Road clients

## Introduction to X-Road Catalog Lister

The purpose of this piece of software is to provide a webservice which lists all the XRoad members and the services they provide together with services descriptions

A sequence diagram illustrating flow between XRoad-Catalog service layer and XRoad-Catalog Lister

![Catalog Service sequence diagram](sequence_diagram_catalog_service.png)


## Build
```sh
../gradlew clean build
```

## Run
```sh
../gradlew bootRun
```

Or

    $ java -jar build/libs/xroad-catalog-lister.jar --spring.config.name=lister,catalogdb


## Test

| Command                                                                                             |           Result            |
|-----------------------------------------------------------------------------------------------------|-----------------------------|
| curl --header "content-type: text/xml" -d @src/main/doc/servicerequest.xml http://localhost:8080/ws |  All services in the system |
| curl http://localhost:8080/ws/services.wsdl                                                         |  Get WSDL                   |

## Main endpoints

### 1. ListMembers

A SOAP endpoint to list all the members the Catalog Collector has stored to the db.
See [List all members](../xroad_catalog_user_guide.md#3231-list-all-members) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 2. GetWsdl

A SOAP endpoint to retrieve a WSDL description for a given service.
See [Retrieve WSDL descriptions](../xroad_catalog_user_guide.md#3232-retrieve-wsdl-descriptions) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 3. GetOpenAPI

A SOAP endpoint to retrieve an OpenAPI description for a given service.
See [Retrieve OPENAPI descriptions](../xroad_catalog_user_guide.md#3233-retrieve-openapi-descriptions) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 4. GetServiceType

A SOAP endpoint for requesting whether a given service is of type SOAP, REST or OPENAPI.
See [Get service type](../xroad_catalog_user_guide.md#3234-get-service-type) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 5. IsProvider

A SOAP endpoint for requesting whether a given member is a provider.
See [Check if member is provicer](../xroad_catalog_user_guide.md#3235-check-if-member-is-provider) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 6. GetOrganizations

A SOAP endpoint for requesting public organization details.
See [List organizations](../xroad_catalog_user_guide.md#3236-list-organizations) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 7. HasOrganizationChanged

A SOAP endpoint for requesting whether given public organization has some of its details changed.
See [List organization changes](../xroad_catalog_user_guide.md#3237-list-organization-changes) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 8. GetCompanies

A SOAP endpoint for requesting private company details.
See [List companies](../xroad_catalog_user_guide.md#3238-list-companies) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 9. HasCompanyChanged

A SOAP endpoint for requesting whether given private company has some of its details changed.
See [List company changes](../xroad_catalog_user_guide.md#3239-list-company-changes) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 10. GetErrors

A SOAP endpoint for requesting a list of errors related to fetching data from different apis and security servers.
See [List errors](../xroad_catalog_user_guide.md#32310-list-errors) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 11. GetServiceStatistics

A REST endpoint for requesting a list of statistics, consisting of numbers of SOAP/REST services over time.
See [List service statistics](../xroad_catalog_user_guide.md#32311-list-service-statistics) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 12. GetServiceStatisticsCSV

A REST endpoint for requesting a list of statistics in CSV format, consisting of numbers of SOAP/REST services over time.
See [List service statistics in CSV format](../xroad_catalog_user_guide.md#32312-list-service-statistics-in-csv-format) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 13. GetListOfServices

A REST endpoint for requesting a list of members and related subsystems, services and security servers over time.
See [List services](../xroad_catalog_user_guide.md#32313-list-services) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 14. GetListOfServicesCSV

A REST endpoint for requesting a list of members and related subsystems, services and security servers in CSV format.
See [List services in CSV format](../xroad_catalog_user_guide.md#32314-list-services-in-csv-format) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 15. GetDistinctServiceStatistics

A REST endpoint for requesting a list of statistics, consisting of numbers of distinct services over time.
See [List distinct service statistics](../xroad_catalog_user_guide.md#32316-list-distinct-service-statistics) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 16. ListErrors

A REST endpoint for listing errors for a given member or subsystem, supports pagination.
See [List errors](../xroad_catalog_user_guide.md#32317-list-errors) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 17. heartbeat

A REST endpoint for requesting the heartbeat of X-Road Catalog.
See [Check heartbeat](../xroad_catalog_user_guide.md#32315-check-heartbeat) in the [User Guide](../xroad_catalog_user_guide.md#license)


### 18. listSecurityServers

A REST endpoint for listing security servers and related information.
See [List security servers](../xroad_catalog_user_guide.md#32318-list-security-servers) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 18. listDescriptors

A REST endpoint for listing subsystems.
See [List descriptors](../xroad_catalog_user_guide.md#32319-list-descriptors) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 19. getOrganization

A REST endpoint for listing organization/company data.
See [Get organization](../xroad_catalog_user_guide.md#32320-get-organization) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 20. getOrganizationChanges

A REST endpoint for requesting whether given organization/company has some of its details changed.
See [Get organization changes](../xroad_catalog_user_guide.md#32321-get-organization-changes) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 21. getEndpoints

A REST endpoint for requesting a list of endpoints for a given REST (also OPENAPI) service or services (when multiple versions exist).
See [Get endpoints](../xroad_catalog_user_guide.md#32322-get-endpoints) in the [User Guide](../xroad_catalog_user_guide.md#license)

### 22. getRest

A REST endpoint for requesting a list of endpoints for a given REST service (without OPENAPI description) or services (when multiple versions exist).
See [Get Rest](../xroad_catalog_user_guide.md#32323-get-rest) in the [User Guide](../xroad_catalog_user_guide.md#license)

## Build RPM Packages on Non-RedHat Platform
 
    $ ../gradlew clean build
    $ docker build -t lister-rpm packages/xroad-catalog-lister/docker
    $ docker run -v $PWD/..:/workspace lister-rpm



