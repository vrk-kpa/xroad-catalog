# xroad-catalog-lister

WebService to produce list of xroad clients

## Introduction to X-Road Catalog Collector

The purpose of this piece of software is to provide a webservice which lists all the XRoad members and the services they provide together with services descriptions

The main endpoints this software provides: 
* ListMembers - endpoint to list all the members the Catalog Collector has stored to the db
* GetWsdl - endpoint to retrieve a WSDL description for a given service
* GetOpenAPI - endpoint to retrieve an OpenAPI description for a given service
* IsSoapProvider - endpoint for requesting whether a given service is a SOAP service
* IsRestProvider - endpoint for requesting whether a given service is a REST service

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

| Command                                                                                |           Result            |
|----------------------------------------------------------------------------------------|-----------------------------|
| curl --header "content-type: text/xml" -d @src/main/doc/servicerequest.xml http://localhost:8080/ws |  All services in the system |
| curl http://localhost:8080/ws/services.wsdl                                            |  Get WSDL                   |


## Build RPM Packages on Non-RedHat Platform
 
    $ ../gradlew clean build
    $ docker build -t lister-rpm packages/xroad-catalog-lister/docker
    $ docker run -v $PWD/..:/workspace lister-rpm



