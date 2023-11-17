# X-Road Catalog User Guide
Version: 4.2.1
Doc. ID: UG-XRDCAT

---

## Version history <!-- omit in toc -->
| Date       | Version | Description                                                                    | Author           |
|------------|---------|--------------------------------------------------------------------------------|------------------|
| 21.07.2021 | 1.0.0   | Initial draft                                                                  | Bert Viikmäe     |
| 21.07.2021 | 1.0.1   | Add installation section                                                       | Bert Viikmäe     |
| 22.07.2021 | 1.0.2   | Add X-Road Catalog Collector section                                           | Bert Viikmäe     |
| 23.07.2021 | 1.0.3   | Add X-Road Catalog Lister section                                              | Bert Viikmäe     |
| 23.07.2021 | 1.0.4   | Add X-Road Catalog Persistence section                                         | Bert Viikmäe     |
| 25.08.2021 | 1.0.5   | Add list distinct services endpoint description                                | Bert Viikmäe     |
| 02.09.2021 | 1.0.6   | Add list errors endpoint description                                           | Bert Viikmäe     |
| 22.09.2021 | 1.0.7   | Update heartbeat endpoint description                                          | Bert Viikmäe     |
| 26.10.2021 | 1.0.8   | Update listErrors endpoint description                                         | Bert Viikmäe     |
| 27.10.2021 | 1.1.0   | Add listSecurityServers and listDescriptors endpoint descriptions              | Bert Viikmäe     |
| 15.12.2021 | 1.1.1   | Update listErrors endpoint description                                         | Bert Viikmäe     |
| 08.02.2022 | 1.2.0   | Add getOrganization and getOrganizationChanges endpoint descriptions           | Bert Viikmäe     |
| 29.07.2022 | 2.0.0   | Substitute since with start and end date parameter and update related chapters | Bert Viikmäe     |
| 04.10.2022 | 2.1.0   | Add getRest and getEndpoints descriptions                                      | Bert Viikmäe     |
| 15.01.2023 | 3.0.0   | Restructure of the document                                                    | Bert Viikmäe     |
| 22.03.2023 | 4.0.0   | Split document into X-Road Catalog Installation Guide and User Guide           | Petteri Kivimäki |
| 16.08.2023 | 4.1.0   | Update Catalog Lister port number from `8080` to `8070`                        | Petteri Kivimäki |
| 09.09.2023 | 4.2.0   | Update REST endpoint descriptions                                              | Petteri Kivimäki |
| 17.11.2023 | 4.2.1   | Update response of ListMembers and service types for GetServiceType            | Bert Viikmäe     |

## Table of Contents <!-- omit in toc -->

<!-- toc -->
<!-- vim-markdown-toc GFM -->

* [License](#license)
* [1. Introduction](#1-introduction)
  * [1.1 Target Audience](#11-target-audience)
* [2. X-Road Catalog Collector](#2-x-road-catalog-collector)
* [3. X-Road Catalog Lister](#3-x-road-catalog-lister)
    * [3.1 SOAP endpoints](#31-soap-endpoints)
        * [3.1.1 List all members](#311-list-all-members)
        * [3.1.2 Retrieve WSDL descriptions](#312-retrieve-wsdl-descriptions)   
        * [3.1.3 Retrieve OPENAPI descriptions](#313-retrieve-openapi-descriptions)
        * [3.1.4 Get service type](#314-get-service-type) 
        * [3.1.5 Check if member is provider](#315-check-if-member-is-provider)  
        * [3.1.6 List errors](#316-list-errors)
        * [3.1.7 List organizations](#317-list-organizations)  
        * [3.1.8 List organization changes](#318-list-organization-changes)
        * [3.1.9 List companies](#319-list-companies) 
        * [3.1.10 List company changes](#3110-list-company-changes)
    * [3.2 REST endpoints](#32-rest-endpoints)          
        * [3.2.1 List service statistics](#321-list-service-statistics) 
        * [3.2.2 List service statistics in CSV format](#322-list-service-statistics-in-csv-format)
        * [3.2.3 List services](#323-list-services)  
        * [3.2.4 List services in CSV format](#324-list-services-in-csv-format)  
        * [3.2.5 Check heartbeat](#325-check-heartbeat)  
        * [3.2.6 List distinct service statistics](#326-list-distinct-service-statistics)  
        * [3.2.7 List errors](#327-list-errors) 
        * [3.2.8 List Security Servers](#328-list-security-servers) 
        * [3.2.9 List descriptors](#329-list-descriptors) 
        * [3.2.10 Get endpoints](#3210-get-endpoints)
        * [3.2.11 Get Rest](#3211-get-rest)
        * [3.2.12 Get Organization](#3212-get-organization) 
        * [3.2.13 Get Organization changes](#3213-get-organization-changes)
        * [3.2.14 Check organization heartbeat](#3214-check-organization-heartbeat)
* [4. X-Road Catalog Persistence](#4-x-road-catalog-persistence)
                     
<!-- vim-markdown-toc -->
<!-- tocstop -->

## License

This document is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License. To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.

## 1. Introduction

X-Road Catalog is an [X-Road](https://github.com/nordic-institute/X-Road/) extension that collects information on 
members, subsystems and services from an X-Road ecosystem and provides a REST and SOAP interfaces to access the data.

X-Road Catalog consists of three modules:

- X-Road Catalog Collector
    * Collects information from the X-Road ecosystem and stores it to a database.
    * Optionally, can collect information from external APIs too, e.g., a national business registry.
- X-Road Catalog Lister
    * Provides REST and SOAP interfaces offering information collected by the collector.
    * Can be used as an X-Road service (X-Road headers are in place).
- X-Road Catalog Persistence
    * Library used to persist and read persisted data.
    * Used by the X-Road Catalog Collector and X-Road Catalog Lister modules.

### 1.1 Target Audience

The intended audience of this user guide are X-Road Operators responsible for managing and configuring the X-Road Central 
Server and related services. The document is intended for readers with a good knowledge of Linux server management, 
computer networks, and the X-Road principles.

## 2. X-Road Catalog Collector

The purpose of this module is to collect members, subsystems and services from the X-Road ecosystem and store them to the PostgreSQL database. 

More information about the [X-Road Catalog Collector](../xroad-catalog-collector/README.md) module.

## 3. X-Road Catalog Lister

The purpose of this module is to provide a web service which lists all the X-Road members and the services they provide together with service descriptions.

More information about the [X-Road Catalog Lister](../xroad-catalog-lister/README.md) module.

### 3.1 SOAP endpoints

The main SOAP endpoints the module  provides with the `default` [profile](../BUILD.md#profiles): 

* `ListMembers` - get a list all the members the Catalog Collector has stored to the db.
* `GetWsdl` - retrieve a WSDL description for a given service.
* `GetOpenAPI` - retrieve an OpenAPI description for a given service.
* `GetServiceType` - retrieve the service type (`SOAP`, `REST` or `OPENAPI3`) for a given service.
* `IsProvider` - check is a given member a service provider.
* `GetErrors` - get a list of errors related to fetching data from different apis and Security Servers.

In addition, some more SOAP endpoints are provided when the `fi` [profile](../BUILD.md#profiles) is active:

* `GetOrganizations` - retrieve public organization details.
* `HasOrganizationChanged` - check have the details of a given public organisation changed.
* `GetCompanies` - retrieve private company details.
* `HasCompanyChanged` - check have the details of a given private company changed.

### 3.1.1 List all members

In order to list all members and related subsystems and services, a request in XML format has to be sent to the respective SOAP endpoint:

```bash
curl -k -d @servicerequest.xml --header "Content-Type: text/xml" -X POST http://<SERVER_ADDRESS>:8070/ws/ListMembers
```

**Note!** Replace the `SERVER_ADDRESS` placeholder with the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Contents of the example `servicerequest.xml` file:
```xml
<soapenv:Envelope 
xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
xmlns:xro="http://x-road.eu/xsd/xroad.xsd" 
xmlns:iden="http://x-road.eu/xsd/identifiers" 
xmlns:xrcl="http://xroad.vrk.fi/xroad-catalog-lister">
   <soapenv:Header>
      <xro:protocolVersion>4.x</xro:protocolVersion>
      <xro:id>ID11234</xro:id>
      <xro:userId>EE1234567890</xro:userId>
      <xro:client iden:objectType="MEMBER">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
      </xro:client>
      <xro:service iden:objectType="SERVICE">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
         <iden:subsystemCode>SS1</iden:subsystemCode>
         <iden:serviceCode>ListMembers</iden:serviceCode>
         <iden:serviceVersion>v1</iden:serviceVersion>
      </xro:service>
   </soapenv:Header>
   <soapenv:Body>
      <xrcl:ListMembers>
         <xrcl:startDateTime>2020-01-01</xrcl:startDateTime>
         <xrcl:endDateTime>2022-01-01</xrcl:endDateTime>
      </xrcl:ListMembers>
   </soapenv:Body>
</soapenv:Envelope>
```

Contents of the XML response of the request
```xml
<?xml version="1.0" encoding="UTF-8"?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
  <SOAP-ENV:Header>
    <xro:protocolVersion xmlns:xro="http://x-road.eu/xsd/xroad.xsd">4.x</xro:protocolVersion>
    <xro:id xmlns:xro="http://x-road.eu/xsd/xroad.xsd">ID11234</xro:id>
    <xro:userId xmlns:xro="http://x-road.eu/xsd/xroad.xsd">EE1234567890</xro:userId>
    <xro:client xmlns:xro="http://x-road.eu/xsd/xroad.xsd" xmlns:iden="http://x-road.eu/xsd/identifiers" iden:objectType="MEMBER">
      <iden:xRoadInstance>FI</iden:xRoadInstance>
      <iden:memberClass>GOV</iden:memberClass>
      <iden:memberCode>1710128-9</iden:memberCode>
    </xro:client>
    <xro:service xmlns:xro="http://x-road.eu/xsd/xroad.xsd" xmlns:iden="http://x-road.eu/xsd/identifiers" iden:objectType="SERVICE">
      <iden:xRoadInstance>FI</iden:xRoadInstance>
      <iden:memberClass>GOV</iden:memberClass>
      <iden:memberCode>1710128-9</iden:memberCode>
      <iden:subsystemCode>SS1</iden:subsystemCode>
      <iden:serviceCode>ListMembers</iden:serviceCode>
      <iden:serviceVersion>v1</iden:serviceVersion>
    </xro:service>
  </SOAP-ENV:Header>
  <SOAP-ENV:Body>
    <ns2:ListMembersResponse xmlns:ns2="http://xroad.vrk.fi/xroad-catalog-lister">
      <ns2:memberList>
        <ns2:member>
          <ns2:xRoadInstance>DEV</ns2:xRoadInstance>
          <ns2:memberClass>GOV</ns2:memberClass>
          <ns2:memberCode>1234</ns2:memberCode>
          <ns2:name>ACME</ns2:name>
          <ns2:subsystems>
            <ns2:subsystem>
              <ns2:subsystemCode>MANAGEMENT</ns2:subsystemCode>
              <ns2:services>
                <ns2:service>
                  <ns2:serviceCode>clientReg</ns2:serviceCode>
                  <ns2:serviceType>SOAP</ns2:serviceType>
                  <ns2:wsdl>
                    <ns2:externalId>1584692751893_da8be621-5d6b-4920-91c9-d8c359dddbad</ns2:externalId>
                    <ns2:created>2020-03-20T10:25:51.892+02:00</ns2:created>
                    <ns2:changed>2020-03-20T10:25:51.892+02:00</ns2:changed>
                    <ns2:fetched>2020-03-20T12:31:09.188+02:00</ns2:fetched>
                  </ns2:wsdl>
                  <ns2:created>2020-03-20T10:25:51.632+02:00</ns2:created>
                  <ns2:changed>2020-03-20T10:25:51.632+02:00</ns2:changed>
                  <ns2:fetched>2020-03-20T12:31:07.223+02:00</ns2:fetched>
                </ns2:service>
                <ns2:service>
                  <ns2:serviceCode>respa.tampere.fi</ns2:serviceCode>
                  <ns2:serviceType>REST</ns2:serviceType>
                  <ns2:created>2020-03-20T10:25:51.632+02:00</ns2:created>
                  <ns2:changed>2020-03-20T10:25:51.632+02:00</ns2:changed>
                  <ns2:fetched>2020-03-20T12:31:07.223+02:00</ns2:fetched>
                </ns2:service>
                <ns2:service>
                  <ns2:serviceCode>authCertDeletion</ns2:serviceCode>
                  <ns2:serviceType>SOAP</ns2:serviceType>
                  <ns2:wsdl>
                    <ns2:externalId>1584692751942_ab002cbd-bbbd-43c7-a311-b0dc5adf3af1</ns2:externalId>
                    <ns2:created>2020-03-20T10:25:51.936+02:00</ns2:created>
                    <ns2:changed>2020-03-20T10:25:51.936+02:00</ns2:changed>
                    <ns2:fetched>2020-03-20T12:31:09.009+02:00</ns2:fetched>
                  </ns2:wsdl>
                  <ns2:created>2020-03-20T10:25:51.632+02:00</ns2:created>
                  <ns2:changed>2020-03-20T10:25:51.632+02:00</ns2:changed>
                  <ns2:fetched>2020-03-20T12:31:07.223+02:00</ns2:fetched>
                </ns2:service>
                <ns2:service>
                  <ns2:serviceCode>clientDeletion</ns2:serviceCode>
                  <ns2:serviceType>SOAP</ns2:serviceType>
                  <ns2:wsdl>
                    <ns2:externalId>1584692751908_5bdde30d-3a5f-42c0-9f45-d884f5810996</ns2:externalId>
                    <ns2:created>2020-03-20T10:25:51.906+02:00</ns2:created>
                    <ns2:changed>2020-03-20T10:25:51.906+02:00</ns2:changed>
                    <ns2:fetched>2020-03-20T12:31:07.383+02:00</ns2:fetched>
                  </ns2:wsdl>
                  <ns2:created>2020-03-20T10:25:51.632+02:00</ns2:created>
                  <ns2:changed>2020-03-20T10:25:51.632+02:00</ns2:changed>
                  <ns2:fetched>2020-03-20T12:31:07.223+02:00</ns2:fetched>
                </ns2:service>
                <ns2:service>
                  <ns2:serviceCode>ownerChange</ns2:serviceCode>
                  <ns2:serviceType>SOAP</ns2:serviceType>
                  <ns2:wsdl>
                    <ns2:externalId>1584692751888_07141c5a-bfe0-4c84-b621-e5e4a9db01fa</ns2:externalId>
                    <ns2:created>2020-03-20T10:25:51.884+02:00</ns2:created>
                    <ns2:changed>2020-03-20T10:25:51.884+02:00</ns2:changed>
                    <ns2:fetched>2020-03-20T12:31:07.479+02:00</ns2:fetched>
                  </ns2:wsdl>
                  <ns2:created>2020-03-20T10:25:51.632+02:00</ns2:created>
                  <ns2:changed>2020-03-20T10:25:51.632+02:00</ns2:changed>
                  <ns2:fetched>2020-03-20T12:31:07.223+02:00</ns2:fetched>
                </ns2:service>
                <ns2:service>
                  <ns2:serviceCode>PetStoreNew</ns2:serviceCode>
                  <ns2:serviceType>REST</ns2:serviceType>
                  <ns2:created>2020-03-20T10:25:51.632+02:00</ns2:created>
                  <ns2:changed>2020-03-20T10:25:51.632+02:00</ns2:changed>
                  <ns2:fetched>2020-03-20T12:31:07.223+02:00</ns2:fetched>
                </ns2:service>
              </ns2:services>
              <ns2:created>2020-03-20T10:25:51.055+02:00</ns2:created>
              <ns2:changed>2020-03-20T10:25:51.055+02:00</ns2:changed>
              <ns2:fetched>2020-03-20T12:31:01.394+02:00</ns2:fetched>
            </ns2:subsystem>
            <ns2:subsystem>
              <ns2:subsystemCode>TEST</ns2:subsystemCode>
              <ns2:services />
              <ns2:created>2020-03-20T10:25:51.055+02:00</ns2:created>
              <ns2:changed>2020-03-20T10:25:51.055+02:00</ns2:changed>
              <ns2:fetched>2020-03-20T12:31:01.394+02:00</ns2:fetched>
            </ns2:subsystem>
          </ns2:subsystems>
          <ns2:created>2020-03-20T10:25:51.055+02:00</ns2:created>
          <ns2:changed>2020-03-20T10:25:51.055+02:00</ns2:changed>
          <ns2:fetched>2020-03-20T12:31:01.394+02:00</ns2:fetched>
        </ns2:member>
      </ns2:memberList>
    </ns2:ListMembersResponse>
  </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

The XML response has a `<SOAP-ENV:Body>` element with the following structure:

* `ListMembersResponse`
  * `memberList`
    * `member`
      * `subsystems`
        * `subsystem`
          * `subsystemCode`
          * `services`
            * `service`
            * `serviceCode`
            * `serviceType`
            * `wsdl` (if the given service description is a WSDL description)
              * `externalId`

In addition, each subsystem, service and wsdl contains also fields `created`, `changed`, `fetched` and `removed`, 
reflecting the creation, change, fetch and removal (when a subsystem/service/wsdl was fetched by X-Road Catalog Collector to the DB) dates.

### 3.1.2 Retrieve WSDL descriptions

In order to retrieve a WSDL service description, a request in XML format has to be sent to the respective SOAP endpoint:

```bash
curl -k -d @wsdlrequest.xml --header "Content-Type: text/xml" -X POST http://<SERVER_ADDRESS>:8070/ws/GetWsdl
```

**Note!** Replace the `SERVER_ADDRESS` placeholder with the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Contents of the example `wsdlrequest.xml` file:
```xml
<soapenv:Envelope 
xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
xmlns:xro="http://x-road.eu/xsd/xroad.xsd" 
xmlns:iden="http://x-road.eu/xsd/identifiers" 
xmlns:xrcl="http://xroad.vrk.fi/xroad-catalog-lister">
   <soapenv:Header>
      <xro:protocolVersion>4.x</xro:protocolVersion>
      <xro:id>ID11234</xro:id>
      <xro:userId>EE1234567890</xro:userId>
      <xro:client iden:objectType="MEMBER">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
      </xro:client>
      <xro:service iden:objectType="SERVICE">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
         <iden:subsystemCode>SS1</iden:subsystemCode>
         <iden:serviceCode>ListMembers</iden:serviceCode>
         <iden:serviceVersion>v1</iden:serviceVersion>
      </xro:service>
   </soapenv:Header>
   <soapenv:Body>
      <xrcl:GetWsdl>
         <xrcl:externalId>1584692751908_5bdde30d-3a5f-42c0-9f45-d884f5810996</xrcl:externalId>
      </xrcl:GetWsdl>
   </soapenv:Body>
</soapenv:Envelope>
```

In the request, the `externalId` field identifies the WSDL to be retrieved.

The response of the given request is in XML format, containing the WSDL service description.

### 3.1.3 Retrieve OPENAPI descriptions

In order to retrieve an OPENAPI service descriptions, a request in XML format has to be sent to the respective SOAP endpoint:

```bash
curl -k -d @openapirequest.xml --header "Content-Type: text/xml" -X POST http://<SERVER_ADDRESS>:8070/ws/GetOpenAPI
```

**Note!** Replace the `SERVER_ADDRESS` placeholder with the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Contents of the example `openapirequest.xml` file:
```xml
<soapenv:Envelope 
xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
xmlns:xro="http://x-road.eu/xsd/xroad.xsd" 
xmlns:iden="http://x-road.eu/xsd/identifiers" 
xmlns:xrcl="http://xroad.vrk.fi/xroad-catalog-lister">
   <soapenv:Header>
      <xro:protocolVersion>4.x</xro:protocolVersion>
      <xro:id>ID11234</xro:id>
      <xro:userId>EE1234567890</xro:userId>
      <xro:client iden:objectType="MEMBER">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
      </xro:client>
      <xro:service iden:objectType="SERVICE">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
         <iden:subsystemCode>SS1</iden:subsystemCode>
         <iden:serviceCode>ListMembers</iden:serviceCode>
         <iden:serviceVersion>v1</iden:serviceVersion>
      </xro:service>
   </soapenv:Header>
   <soapenv:Body>
      <xrcl:GetOpenAPI>
         <xrcl:externalId>1584692752414_504b3ad4-eca3-4b96-8b21-71209225cfc8</xrcl:externalId>
      </xrcl:GetOpenAPI>
   </soapenv:Body>
</soapenv:Envelope>
```

In the request, the `externalId` field identifies the OPENAPI to be retrieved.

The response of the given request is in XML format, containing the OPENAPI service description.

### 3.1.4 Get service type

In order to retrieve service type information, a request in XML format has to be sent to the respective SOAP endpoint:

```bash
curl -k -d @GetServiceTypeRequest.xml --header "Content-Type: text/xml" -X POST http://<SERVER_ADDRESS>:8070/ws/GetServiceType
```

**Note!** Replace the `SERVER_ADDRESS` placeholder with the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Contents of the example `GetServiceTypeRequest.xml` file:
```xml
<soapenv:Envelope 
xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
xmlns:xro="http://x-road.eu/xsd/xroad.xsd" 
xmlns:iden="http://x-road.eu/xsd/identifiers" 
xmlns:xrcl="http://xroad.vrk.fi/xroad-catalog-lister">
   <soapenv:Header>
      <xro:protocolVersion>4.x</xro:protocolVersion>
      <xro:id>ID11234</xro:id>
      <xro:userId>EE1234567890</xro:userId>
      <xro:client iden:objectType="MEMBER">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
      </xro:client>
      <xro:service iden:objectType="SERVICE">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
         <iden:subsystemCode>SS1</iden:subsystemCode>
         <iden:serviceCode>ListMembers</iden:serviceCode>
         <iden:serviceVersion>v1</iden:serviceVersion>
      </xro:service>
   </soapenv:Header>
   <soapenv:Body>
      <xrcl:GetServiceType>
         <xrcl:xRoadInstance>DEV</xrcl:xRoadInstance>
         <xrcl:memberClass>GOV</xrcl:memberClass>
         <xrcl:memberCode>1234</xrcl:memberCode>
         <xrcl:serviceCode>authCertDeletion</xrcl:serviceCode>
         <xrcl:subsystemCode>MANAGEMENT</xrcl:subsystemCode>
         <xrcl:serviceVersion>v1</xrcl:serviceVersion>
      </xrcl:GetServiceType>
   </soapenv:Body>
</soapenv:Envelope>
```

The following request fields need to be filled:

* `xRoadInstance` - X-Road Instance name, e.g. `DEV`.
* `memberClass` - member class, e.g., `GOV`.
* `memberCode` -  member code, e.g., `1234`.
* `serviceCode` - service code, e.g., `authCertDeletion`-
* `subsystemCode` - subsystem code, e.g., `MANAGEMENT`.
* `serviceVersion` - service version, e.g., `v1`.

The XML response has a `<SOAP-ENV:Body>` element with the following structure:

* `GetServiceTypeResponse`
  * `type` (values: `REST`/`OPENAPI3`/`WSDL`/`UNKNOWN`)
                   
### 3.1.5 Check if member is provider

In order to check if a given X-Road member (Security Server) is a provider, a request in XML format has to be sent to the respective SOAP endpoint:

```bash
curl -k -d @IsProviderRequest.xml --header "Content-Type: text/xml" -X POST http://<SERVER_ADDRESS>:8070/ws/IsProvider
```

**Note!** Replace the `SERVER_ADDRESS` placeholder with the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Contents of the example `IsProviderRequest.xml` file:
```xml
<soapenv:Envelope 
xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
xmlns:xro="http://x-road.eu/xsd/xroad.xsd" 
xmlns:iden="http://x-road.eu/xsd/identifiers" 
xmlns:xrcl="http://xroad.vrk.fi/xroad-catalog-lister">
   <soapenv:Header>
      <xro:protocolVersion>4.x</xro:protocolVersion>
      <xro:id>ID11234</xro:id>
      <xro:userId>EE1234567890</xro:userId>
      <xro:client iden:objectType="MEMBER">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
      </xro:client>
      <xro:service iden:objectType="SERVICE">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
         <iden:subsystemCode>SS1</iden:subsystemCode>
         <iden:serviceCode>ListMembers</iden:serviceCode>
         <iden:serviceVersion>v1</iden:serviceVersion>
      </xro:service>
   </soapenv:Header>
   <soapenv:Body>
      <xrcl:IsProvider>
         <xrcl:xRoadInstance>DEV</xrcl:xRoadInstance>
         <xrcl:memberClass>GOV</xrcl:memberClass>
         <xrcl:memberCode>1234</xrcl:memberCode>
      </xrcl:IsProvider>
   </soapenv:Body>
</soapenv:Envelope>
```

The following request fields need to be filled:

* `xRoadInstance` - X-Road Instance name, e.g., `DEV`.
* `memberClass` - member class, e.g., `GOV`.
* `memberCode` -  member code, e.g., `1234`.

The XML response has a `<SOAP-ENV:Body>` element with the following structure:

* `IsProviderResponse`
  * `provider` (values: `true`/`false`)

### 3.1.6 List errors

In order to fetch information about errors in the X-Road Catalog, a request in XML format has to be sent to the respective SOAP endpoint:

```bash
curl -k -d @GetErrorsRequest.xml --header "Content-Type: text/xml" -X POST http://<SERVER_ADDRESS>:8070/ws/GetErrors
```

**Note!** Replace the `SERVER_ADDRESS` placeholder with the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Contents of the example `GetErrorsRequest.xml` file:
```xml
<soapenv:Envelope
        xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
        xmlns:xro="http://x-road.eu/xsd/xroad.xsd"
        xmlns:iden="http://x-road.eu/xsd/identifiers"
        xmlns:xrcl="http://xroad.vrk.fi/xroad-catalog-lister">
    <soapenv:Header>
        <xro:protocolVersion>4.x</xro:protocolVersion>
        <xro:id>ID11234</xro:id>
        <xro:userId>EE1234567890</xro:userId>
        <xro:client iden:objectType="MEMBER">
            <iden:xRoadInstance>FI</iden:xRoadInstance>
            <iden:memberClass>GOV</iden:memberClass>
            <iden:memberCode>1710128-9</iden:memberCode>
        </xro:client>
        <xro:service iden:objectType="SERVICE">
            <iden:xRoadInstance>FI</iden:xRoadInstance>
            <iden:memberClass>GOV</iden:memberClass>
            <iden:memberCode>1710128-9</iden:memberCode>
            <iden:subsystemCode>SS1</iden:subsystemCode>
            <iden:serviceCode>ListMembers</iden:serviceCode>
            <iden:serviceVersion>v1</iden:serviceVersion>
        </xro:service>
    </soapenv:Header>
    <soapenv:Body>
        <xrcl:GetErrors>
            <xrcl:startDateTime>2020-01-01</xrcl:startDateTime>
            <xrcl:endDateTime>2022-01-01</xrcl:endDateTime>
        </xrcl:GetErrors>
    </soapenv:Body>
</soapenv:Envelope>
```
The following request fields need to be filled:

* `startDateTime` - date after which to list errors, e.g., `2020-01-01`.
* `endDateTime` - date before which to list errors, e.g., `2022-01-01`.

The XML response has a `<SOAP-ENV:Body>` element with the following structure:

* `GetErrorsResponse`
    * `errorLogList`
        * `errorLog`
            * `message`
            * `code`
            * `created`

### 3.1.7 List organizations

**Note!** Requires the `fi` [profile](../BUILD.md#profiles).

In order to fetch information about organizations, a request in XML format has to be sent to the respective SOAP endpoint:

```bash
curl -k -d @GetOrganizationsRequest.xml --header "Content-Type: text/xml" -X POST http://<SERVER_ADDRESS>:8070/ws/GetOrganizations
```

**Note!** Replace the `SERVER_ADDRESS` placeholder with the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Contents of the example `GetOrganizationsRequest.xml` file:
```xml
<soapenv:Envelope
        xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
        xmlns:xro="http://x-road.eu/xsd/xroad.xsd"
        xmlns:iden="http://x-road.eu/xsd/identifiers"
        xmlns:xrcl="http://xroad.vrk.fi/xroad-catalog-lister">
   <soapenv:Header>
      <xro:protocolVersion>4.x</xro:protocolVersion>
      <xro:id>ID11234</xro:id>
      <xro:userId>EE1234567890</xro:userId>
      <xro:client iden:objectType="MEMBER">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
      </xro:client>
      <xro:service iden:objectType="SERVICE">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
         <iden:subsystemCode>SS1</iden:subsystemCode>
         <iden:serviceCode>ListMembers</iden:serviceCode>
         <iden:serviceVersion>v1</iden:serviceVersion>
      </xro:service>
   </soapenv:Header>
   <soapenv:Body>
      <xrcl:GetOrganizations>
         <xrcl:businessCode>0181367-9</xrcl:businessCode>
      </xrcl:GetOrganizations>
   </soapenv:Body>
</soapenv:Envelope>
```

The following request fields need to be filled:

* `businessCode` - business code of the searchable organization, e.g., `0181367-9`.

The XML response has a `<SOAP-ENV:Body>` element with the following structure:

* `GetOrganizationsResponse`
  * `organizationList`
    * `organization`
        * `organizationType`
        * `publishingStatus`
        * `businessCode`
        * `guid`
        * `organizationNames`
	      * `organizationName`
            * `language`
        	* `type`
        	* `value`
        	* `created`
        	* `changed`
        	* `fetched`
        	* `removed`
        * `organizationDescriptions`
          * `organizationDescription`
            * `language`
            * `type`
            * `value`
            * `created`
            * `changed`
            * `fetched`
            * `removed`
        * `emails`
          * `email`
            * `language`
        	* `type`
        	* `value`
        	* `created`
        	* `changed`
        	* `fetched`
        	* `removed`
        * `phoneNumbers`
	      * `phoneNumber`
       	    * `language`
            * `additionalInformation`
            * `serviceChargeType`
            * `chargeDescription`
            * `prefixNumber`
            * `number`
            * `isFinnishServiceNumber`
        	* `created`
        	* `changed`
        	* `fetched`
        	* `removed`
        * `webPages`
	      * `webPage`
            * `language`
            * `url`
            * `value`
        	* `created`
        	* `changed`
        	* `fetched`
        	* `removed`
        * `addresses`
	      * `address`
            * `country`
            * `type`
            * `subType`
            * `streetAddresses`
              * `streetAddress`
                * `postalCode`
                * `latitude`
                * `longitude`
                * `coordinateState`
                * `streets`
                  * `street`
        		  * `language`
                  * `value`
        		  * `created`
        		  * `changed`
        		  * `fetched`
        		  * `removed`
              * `postOffices`
               	  * `streetAddressPostOffice`
        		    * `language`
                	* `value`
        			* `created`
        			* `changed`
        			* `fetched`
        			* `removed`
              * `municipalities`
                * `streetAddressMunicipality`
                  * `code`
                  * `streetAddressMunicipalityNames`
                  * `streetAddressMunicipalityName`
        		  * `language`
                  * `value`
        		  * `created`
        		  * `changed`
        		  * `fetched`
        		  * `removed`
        		* `created`
        	    * `changed`
        	    * `fetched`
        	    * `removed`
		      * `additionalInformation`
        		  * `streetAddressAdditionalInformation`
               	    * `language`
                	* `value`
        			* `created`
        			* `changed`
        			* `fetched`
        			* `removed`
        	* `created`
        	* `changed`
        	* `fetched`
        	* `removed`
        * `created`
        * `changed`
        * `fetched`
        * `removed`
        
In addition, most sections also contain fields `created`, `changed`, `fetched` and `removed`, reflecting the creation, 
change, fetch and removal (when the respective data was fetched by X-Road Catalog Collector to the DB) dates.

### 3.1.8 List organization changes

**Note!** Requires the `fi` [profile](../BUILD.md#profiles).

In order to fetch information about changed organization fields, a request in XML format has to be sent to the respective SOAP endpoint:

```bash
curl -k -d @HasOrganizationChangedRequest.xml --header "Content-Type: text/xml" -X POST http://<SERVER_ADDRESS>:8070/ws/HasOrganizationChanged
```

**Note!** Replace the `SERVER_ADDRESS` placeholder with the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Contents of the example `HasOrganizationChangedRequest.xml` file:
```xml
<soapenv:Envelope
        xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
        xmlns:xro="http://x-road.eu/xsd/xroad.xsd"
        xmlns:iden="http://x-road.eu/xsd/identifiers"
        xmlns:xrcl="http://xroad.vrk.fi/xroad-catalog-lister">
   <soapenv:Header>
      <xro:protocolVersion>4.x</xro:protocolVersion>
      <xro:id>ID11234</xro:id>
      <xro:userId>EE1234567890</xro:userId>
      <xro:client iden:objectType="MEMBER">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
      </xro:client>
      <xro:service iden:objectType="SERVICE">
         <iden:xRoadInstance>FI</iden:xRoadInstance>
         <iden:memberClass>GOV</iden:memberClass>
         <iden:memberCode>1710128-9</iden:memberCode>
         <iden:subsystemCode>SS1</iden:subsystemCode>
         <iden:serviceCode>ListMembers</iden:serviceCode>
         <iden:serviceVersion>v1</iden:serviceVersion>
      </xro:service>
   </soapenv:Header>
   <soapenv:Body>
      <xrcl:HasOrganizationChanged>
         <xrcl:guid>e6b33f11-bb47-496e-98c5-6a736dae6014</xrcl:guid>
         <xrcl:startDateTime>2020-01-01</xrcl:startDateTime>
         <xrcl:endDateTime>2022-01-01</xrcl:endDateTime>
      </xrcl:HasOrganizationChanged>
   </soapenv:Body>
</soapenv:Envelope>
```

The following request fields need to be filled:
- `guid` - unique id of the searchable organization, e.g., `e6b33f11-bb47-496e-98c5-6a736dae6014`.
- `startDateTime` - date to check against after which an organization may have changed its field values, e.g., `2020-01-01`.
- `endDateTime` - date to check against before which an organization may have changed its field values, e.g., `2022-01-01`.
 
The XML response has a `<SOAP-ENV:Body>` element with the following structure:

* `HasOrganizationChangedResponse`
  * `changed` (values: `true`/`false`)
  * `changedValueList`
    * `changedValue`
      * `name` (values: e.g., `OrganizationName`, `Email`, `Address`, etc.)

### 3.1.9 List companies

**Note!** Requires the `fi` [profile](../BUILD.md#profiles).

In order to fetch information about companies, a request in XML format has to be sent to the respective SOAP endpoint:

```bash
curl -k -d @GetCompaniesRequest.xml --header "Content-Type: text/xml" -X POST http://<SERVER_ADDRESS>:8070/ws/GetCompanies
```

**Note!** Replace the `SERVER_ADDRESS` placeholder with the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Contents of the example `GetCompaniesRequest.xml` file:
```xml
<soapenv:Envelope
        xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
        xmlns:xro="http://x-road.eu/xsd/xroad.xsd"
        xmlns:iden="http://x-road.eu/xsd/identifiers"
        xmlns:xrcl="http://xroad.vrk.fi/xroad-catalog-lister">
    <soapenv:Header>
        <xro:protocolVersion>4.x</xro:protocolVersion>
        <xro:id>ID11234</xro:id>
        <xro:userId>EE1234567890</xro:userId>
        <xro:client iden:objectType="MEMBER">
            <iden:xRoadInstance>FI</iden:xRoadInstance>
            <iden:memberClass>GOV</iden:memberClass>
            <iden:memberCode>1710128-9</iden:memberCode>
        </xro:client>
        <xro:service iden:objectType="SERVICE">
            <iden:xRoadInstance>FI</iden:xRoadInstance>
            <iden:memberClass>GOV</iden:memberClass>
            <iden:memberCode>1710128-9</iden:memberCode>
            <iden:subsystemCode>SS1</iden:subsystemCode>
            <iden:serviceCode>ListMembers</iden:serviceCode>
            <iden:serviceVersion>v1</iden:serviceVersion>
        </xro:service>
    </soapenv:Header>
    <soapenv:Body>
        <xrcl:GetCompanies>
            <xrcl:businessId>1710128-9</xrcl:businessId>
        </xrcl:GetCompanies>
    </soapenv:Body>
</soapenv:Envelope>
```

The following request fields need to be filled:
- `businessId` - business code of the searchable organization, e.g., `1710128-9`.

The XML response has a `<SOAP-ENV:Body>` element with the following structure:

* `GetCompaniesResponse`
  * `companyList`
    * `company`
      * `companyForm`
      * `detailsUri`
      * `businessId`
      * `name`
      * `registrationDate`
      * `businessAddresses`
        * `businessAddress`
          * `source`
          * `version`
          * `careOf`
          * `street`
          * `postCode`
          * `city`
          * `language`
          * `type`
          * `country`
          * `registrationDate`
          * `created`
          * `changed`
          * `fetched`
          * `removed`
      * `businessAuxiliaryNames`
        * `businessAuxiliaryName`
          * `source`
          * `ordering`
          * `version`
          * `name`
          * `language`
          * `registrationDate`
          * `created`
          * `changed`
          * `fetched`
          * `removed`
      * `businessIdChanges`
        * `businessIdChange`
          * `source`
          * `description`
          * `reason`
          * `changeDate`
          * `change`
          * `oldBusinessId`
          * `newBusinessId`
          * `language`
          * `created`
          * `changed`
          * `fetched`
          * `removed`
      * `businessLines`
        * `businessLine`
          * `source`
          * `ordering`
          * `version`
          * `name`
          * `language`
          * `registrationDate`
          * `created`
          * `changed`
          * `fetched`
          * `removed`
      * `businessNames`
        * `businessName`
          * `companyId`
          * `source`
          * `ordering`
          * `version`
          * `name`
          * `language`
          * `registrationDate`
          * `endDate`
          * `created`
          * `changed`
          * `fetched`
          * `removed`         
      * `companyForms`
        * `companyForm`
          * `source`
          * `version`
          * `name`
          * `language`
          * `type`
          * `registrationDate`
          * `created`
          * `changed`
          * `fetched`
          * `removed`
      * `contactDetails`
        * `contactDetail`
          * `source`
          * `version`
          * `language`
          * `value`
          * `type`
          * `registrationDate`
          * `created`
          * `changed`
          * `fetched`
      * `languages`
        * `language`
          * `source`
          * `version`
          * `name`
          * `language`
          * `registrationDate`
          * `created`
          * `changed`
          * `fetched`
      * `liquidations`
        * `liquidation`
          * `companyId`
          * `source`
          * `version`
          * `name`
          * `language`
          * `registrationDate`
          * `endDate`
          * `created`
          * `changed`
          * `fetched`
          * `removed`
      * `registeredEntries`
        * `registeredEntry`
          * `companyId`
          * `description`
          * `status`
          * `register`
          * `language`
          * `authority`
          * `registrationDate`
          * `endDate`
          * `created`
          * `changed`
          * `fetched`
          * `removed` 
      * `registeredOffices`
        * `registeredOffice`
          * `companyId`
          * `source`
          * `ordering`
          * `version`
          * `name`
          * `language`
          * `registrationDate`
          * `endDate`
          * `created`
          * `changed`
          * `fetched`
          * `removed`
                             
### 3.1.10 List company changes

**Note!** Requires the `fi` [profile](../BUILD.md#profiles).

In order to fetch information about changed company fields, a request in XML format has to be sent to the respective SOAP endpoint:

```bash
curl -k -d @HasCompanyChangedRequest.xml --header "Content-Type: text/xml" -X POST http://<SERVER_ADDRESS>:8070/ws/HasCompanyChanged
```

**Note!** Replace the `SERVER_ADDRESS` placeholder with the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Contents of the example `HasCompanyChangedRequest.xml` file:
```xml
<soapenv:Envelope
        xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
        xmlns:xro="http://x-road.eu/xsd/xroad.xsd"
        xmlns:iden="http://x-road.eu/xsd/identifiers"
        xmlns:xrcl="http://xroad.vrk.fi/xroad-catalog-lister">
    <soapenv:Header>
        <xro:protocolVersion>4.x</xro:protocolVersion>
        <xro:id>ID11234</xro:id>
        <xro:userId>EE1234567890</xro:userId>
        <xro:client iden:objectType="MEMBER">
            <iden:xRoadInstance>FI</iden:xRoadInstance>
            <iden:memberClass>GOV</iden:memberClass>
            <iden:memberCode>1710128-9</iden:memberCode>
        </xro:client>
        <xro:service iden:objectType="SERVICE">
            <iden:xRoadInstance>FI</iden:xRoadInstance>
            <iden:memberClass>GOV</iden:memberClass>
            <iden:memberCode>1710128-9</iden:memberCode>
            <iden:subsystemCode>SS1</iden:subsystemCode>
            <iden:serviceCode>ListMembers</iden:serviceCode>
            <iden:serviceVersion>v1</iden:serviceVersion>
        </xro:service>
    </soapenv:Header>
    <soapenv:Body>
        <xrcl:HasCompanyChanged>
            <xrcl:businessId>1710128-9</xrcl:businessId>
            <xrcl:startDateTime>2020-01-01</xrcl:startDateTime>
            <xrcl:endDateTime>2022-01-01</xrcl:endDateTime>
        </xrcl:HasCompanyChanged>
    </soapenv:Body>
</soapenv:Envelope>
```

The following request fields need to be filled:
- `businessId` - business id of the searchable company, e.g., `1710128-9`.
- `startDateTime` - date to check against after which a company may have changed its field values, e.g., `2020-01-01`.
- `endDateTime` - date to check against before which a company may have changed its field values, e.g., `2022-01-01`.

The XML response has a `<SOAP-ENV:Body>` element with the following structure:

* `HasCompanyChangedResponse`
  * `changed` (values: `true`/`false`)
  * `changedValueList`
    * `changedValue`
      * `name` (values: e.g., `Company`, `ContactDetail`, etc.)

## 3.2 REST endpoints

The main endpoints provided by the default [profile](../BUILD.md#profiles):

* `getServiceStatistics` - request a list of statistics, consisting of numbers of SOAP/REST services over time.
* `getServiceStatisticsCSV` - request a list of statistics in CSV format, consisting of numbers of SOAP/REST services over time.
* `getListOfServices` - request a list of members and related subsystems, services and Security Servers over time.
* `getListOfServicesCSV` - request a list of members and related subsystems, services and Security Servers in CSV format.
* `getDistinctServiceStatistics` - request a list of statistics, consisting of numbers of distinct services over time.
* `listErrors` - list errors for a given member or subsystem, supports pagination.
* `heartbeat` - request the heartbeat of X-Road Catalog.
* `listSecurityServers` - list Security Servers and related information.
* `listDescriptors` - list subsystems.
* `getRest` - request a list of endpoints for a REST type of service.
* `getEndpoints` - request a list of endpoints for a `REST` or `OPENAPI3` type of service.

In addition, some more REST endpoints are provided when the `fi` [profile](../BUILD.md#profiles) is active:

* `getOrganization` - list organization/company data.
* `getOrganizationChanges` - request whether given organization/company has some of its details changed.
* `organizationHeartbeat` - request the heartbeat of organization and companies of X-Road Catalog.


### 3.2.1 List service statistics

In order to fetch information about service statistics in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

```bash
curl "http://<SERVER_ADDRESS>:8070/api/getServiceStatistics?startDate=<START_DATE>&endDate=<END_DATE>" -H "Content-Type: application/json"
```

The required request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.
* `START_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.
* `END_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.

Response in JSON:
```json
{
   "serviceStatisticsList":[
      {
         "created": "2022-07-01T00:00:00",
         "numberOfSoapServices":0,
         "numberOfRestServices":0,
         "numberOfOpenApiServices":0
      },
      {
         "created": "2022-07-02T00:00:00",
         "numberOfSoapServices":0,
         "numberOfRestServices":0,
         "numberOfOpenApiServices":0
      }
   ]
}
```

The response has the following fields:

* `serviceStatisticsList`
    * `created`
    * `numberOfSoapServices`
    * `numberOfRestServices`
    * `numberOfOpenApiServices`

### 3.2.2 List service statistics in CSV format

In order to fetch information about service statistics in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

```bash
curl "http://<SERVER_ADDRESS>:8070/api/getServiceStatisticsCSV?startDate=<START_DATE>&endDate=<END_DATE>" -H "Content-Type: text/csv" --output service_statistics.csv
```

The required request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.
* `START_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.
* `END_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.

Response is a file `service_statistics.csv` with the following content:

```csv
Date,Number of REST services,Number of SOAP services,Number of OpenApi services
2022-07-01T00:00,0,0,0,0
2022-07-02T00:00,0,0,0,0
```

### 3.2.3 List services

In order to fetch information about services in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

```bash
curl "http://<SERVER_ADDRESS>:8070/api/getListOfServices?startDate=<START_DATE>&endDate=<END_DATE>" -H "Content-Type: application/json"
```

The required request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.
* `START_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.
* `END_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.

Response in JSON:

```json
{
   "memberData":[
      {
         "date": "2021-08-24T00:00:00",
         "memberDataList":[
            {
               "created": "2021-08-24T16:20:26",
               "memberClass":"COM",
               "memberCode":"222",
               "name":"ACME",
               "provider":false,
               "subsystemList":[
                  {
                     "created": "2022-02-03T14:10:25",
                     "subsystemCode":"FRUIT",
                     "active":true,
                     "serviceList":[
                        
                     ]
                  }
               ],
               "xroadInstance":"DEV"
            },
            {
               "created": "2021-08-24T16:20:26",
               "memberClass":"COM",
               "memberCode":"12345",
               "name":"Company",
               "provider":false,
               "subsystemList":[
                  
               ],
               "xroadInstance":"DEV"
            }
         ]
      },
      {
         "date": "2021-08-25T00:00:00",
         "memberDataList":[
            {
               "created": "2021-08-24T16:20:26",
               "memberClass":"COM",
               "memberCode":"222",
               "name":"ACME",
               "provider":false,
               "subsystemList":[
                  {
                     "created": "2021-02-03T14:10:25",
                     "subsystemCode":"FRUIT",
                     "active":true,
                     "serviceList":[
                        
                     ]
                  }
               ],
               "xroadInstance":"DEV"
            },
            {
               "created": "2021-08-24T16:20:26",
               "memberClass":"COM",
               "memberCode":"12345",
               "name":"Company",
               "provider":false,
               "subsystemList":[
                  
               ],
               "xroadInstance":"DEV"
            }
         ]
      }
   ],
   "securityServerData":[
      {
         "serverCode":"SS1",
         "address":"SS1",
         "memberClass":"GOV",
         "memberCode":"1234",
         "xroadInstance":"DEV"
      },
      {
         "serverCode":"ss4",
         "address":"ss4",
         "memberClass":"GOV",
         "memberCode":"1234",
         "xroadInstance":"DEV"
      }
   ]
}
```

The response has the following fields:

* `memberData`
    * `date`
    * `memberDataList`
        * `created`
        * `memberClass`
        * `memberCode`
        * `name`
        * `subsystemList`
            * `created`
            * `subsystemCode`
            * `active` (values: `true`/`false`)
            * `serviceList`
                * `created`
                * `serviceCode`
                * `serviceVersion`
                * `active` (values: `true`/`false`)
        * `xroadInstance`
* `securityServerData`
    * `serverCode`
    * `address`
    * `memberClass`
    * `memberCode`

### 3.2.4 List services in CSV format

In order to fetch information about services in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

```bash
curl "http://<SERVER_ADDRESS>:8070/api/getListOfServicesCSV?startDate=<START_DATE>&endDate=<END_DATE>" -H "Content-Type: text/csv" --output list_of_services.csv
```

The required request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.
* `START_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.
* `END_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.

Response is a file `list_of_services.csv` with the following content:

```csv
Date,XRoad instance,Member class,Member code,Member name,Member created,Subsystem code,Subsystem created,Subsystem active,Service code,Service version,Service created,Service active
2021-08-24T00:00,,,,,,,,,,,,
"",DEV,COM,222,ACME,2021-08-24T16:20:26.830,FRUIT,2022-02-03T14:10:25.712,true,,,,
"",DEV,COM,12345,Company,2021-08-24T16:20:26.830,,,,,,,
2021-08-25T00:00,,,,,,,,,,,,
"",DEV,COM,222,ACME,2021-08-24T16:20:26.830,FRUIT,2022-02-03T14:10:25.712,true,,,,
"",DEV,COM,12345,Company,2021-08-24T16:20:26.830,,,,,,,
"",Security server (SS) info:,,,,,,,,,,,
instance,member class,member code,server code,address,,,,,,,,
DEV,GOV,1234,SS1,SS1,,,,,,,,
DEV,GOV,1234,ss4,ss4,,,,,,,,
```

### 3.2.5 Check heartbeat

In order to fetch X-Road Catalog heartbeat information, an HTTP request has to be sent to a respective REST endpoint:

```bash
curl "http://<SERVER_ADDRESS>:8070/api/heartbeat" -H "Content-Type: application/json"
```

The required request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Response in JSON:
```json
{"appWorking":true,
 "dbWorking":true,
 "appName":"X-Road Catalog Lister",
 "appVersion":"1.2.1",
 "systemTime":[2021,9,20,10,12,15,132000000],
 "lastCollectionData":
 {"membersLastFetched":[2021,9,20,10,8,51,380000000],
  "subsystemsLastFetched":[2021,9,20,10,8,51,380000000],
  "servicesLastFetched":[2021,9,1,15,32,51,123000000],
  "wsdlsLastFetched":[2021,9,1,15,32,53,87000000],
  "openapisLastFetched":[2020,11,22,22,12,32,202000000]
 }
}
```

The response has the following fields:

* `appWorking`
* `dbWorking`
* `appName`
* `appVersion`
* `systemTime`
* `lastCollectionData`
    * `membersLastFetched`
    * `subsystemsLastFetched`
    * `servicesLastFetched`
    * `wsdlsLastFetched`
    * `openapisLastFetched`

### 3.2.6 List distinct service statistics

In order to fetch information about distinct service statistics in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

```bash
curl "http://<SERVER_ADDRESS>:8070/api/getDistinctServiceStatistics?startDate=<START_DATE>&endDate=<END_DATE>" -H "Content-Type: application/json"
```

The required request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.
* `START_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.
* `END_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.

Response in JSON:
```json
{
   "distinctServiceStatisticsList":[
      {
         "created": "2020-11-20T00:00:00",
         "numberOfDistinctServices":3
      },
      {
         "created": "2020-11-21T00:00:00",
         "numberOfDistinctServices":3
      },
      {
         "created": "2020-11-22T00:00:00",
         "numberOfDistinctServices":3
      },
      {
         "created": "2020-11-23T00:00:00",
         "numberOfDistinctServices":3
      },
      {
         "created": "2020-11-24T00:00:00",
         "numberOfDistinctServices":3
      },
      {
         "created": "2020-11-25T00:00:00",
         "numberOfDistinctServices":3
      },
      {
         "created": "2020-11-26T00:00:00",
         "numberOfDistinctServices":3
      },
      {
         "created": "2020-11-27T00:00:00",
         "numberOfDistinctServices":3
      },
      {
         "created": "2020-11-28T00:00:00",
         "numberOfDistinctServices":3
      },
      {
         "created": "2020-11-29T00:00:00",
         "numberOfDistinctServices":3
      },
      {
         "created": "2020-11-30T00:00:00",
         "numberOfDistinctServices":3
      }
   ]
}
```

The response has the following fields:

* `serviceStatisticsList`
    * `created`
    * `numberOfDistinctServices`

### 3.2.7 List errors

In order to fetch information about errors during data harvesting in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

List errors for a given subsystem:
```bash
curl "http://<SERVER_ADDRESS>:8070/api/listErrors/<INSTANCE>/<MEMBER_CLASS>/<MEMBER_CODE>/<SUBSYSTEM_CODE>?startDate=<START_DATE>&endDate=<END_DATE>" -H "Content-Type: application/json"
```

List errors for a given member:
```bash
curl "http://<SERVER_ADDRESS>:8070/api/listErrors/<INSTANCE>/<MEMBER_CLASS>/<MEMBER_CODE>?startDate=<START_DATE>&endDate=<END_DATE>" -H "Content-Type: application/json"
```

List errors for a given member class:
```bash
curl "http://<SERVER_ADDRESS>:8070/api/listErrors/<INSTANCE>/<MEMBER_CLASS>?startDate=<START_DATE>&endDate=<END_DATE>" -H "Content-Type: application/json"
```

List errors for a given instance:
```bash
curl "http://<SERVER_ADDRESS>:8070/api/listErrors/<INSTANCE>?startDate=<START_DATE>&endDate=<END_DATE>" -H "Content-Type: application/json"
```

List errors for all the instances and members:
```bash
curl "http://<SERVER_ADDRESS>:8070/api/listErrors?startDate=<START_DATE>&endDate=<END_DATE>" -H "Content-Type: application/json"
```

List errors for a given subsystem with pagination:
```bash
curl "http://<SERVER_ADDRESS>:8070/api/listErrors/<INSTANCE>/<MEMBER_CLASS>/<MEMBER_CODE>/<SUBSYSTEM_CODE>?startDate=<START_DATE>&endDate=<END_DATE>&page=<PAGE_NUMBER>&limit=<NO_OF_ERRORS_PER_PAGE>" -H "Content-Type: application/json"
```

List errors for a given member with pagination:
```bash
curl "http://<SERVER_ADDRESS>:8070/api/listErrors/<INSTANCE>/<MEMBER_CLASS>/<MEMBER_CODE>?startDate=<START_DATE>&endDate=<END_DATE>&page=<PAGE_NUMBER>&limit=<NO_OF_ERRORS_PER_PAGE>" -H "Content-Type: application/json"
```

List errors for a given member class with pagination:
```bash
curl "http://<SERVER_ADDRESS>:8070/api/listErrors/<INSTANCE>/<MEMBER_CLASS>?startDate=<START_DATE>&endDate=<END_DATE>&page=<PAGE_NUMBER>&limit=<NO_OF_ERRORS_PER_PAGE>" -H "Content-Type: application/json"
```

List errors for a given instance with pagination:
```bash
curl "http://<SERVER_ADDRESS>:8070/api/listErrors/<INSTANCE>?startDate=<START_DATE>&endDate=<END_DATE>&page=<PAGE_NUMBER>&limit=<NO_OF_ERRORS_PER_PAGE>" -H "Content-Type: application/json"
```

List errors for all the instances and members with pagination:
```bash
curl "http://<SERVER_ADDRESS>:8070/api/listErrors?startDate=<START_DATE>&endDate=<END_DATE>&page=<PAGE_NUMBER>&limit=<NO_OF_ERRORS_PER_PAGE>" -H "Content-Type: application/json"
```

Example request:
```bash
curl "http://localhost:8900/api/listErrors/DEV/GOV/1234?startDate=2021-08-24&endDate=2021-08-25&page=0&limit=10" -H "Content-Type: application/json"
```

The request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.
* `INSTANCE` - name of X-Road instance, e.g., `DEV`.
* `MEMBER_CLASS` - member class, e.g., `GOV`.
* `MEMBER_CODE` - member code, e.g., `1234`.
* `SUBSYSTEM_CODE` - subsystem code, e.g., `TEST`.
* `START_DATE` - (*optional*) a string in format `YYYY-MM-DD`, if not used, today's date will be assumed.
* `END_DATE` - (*optional*) a string in format `YYYY-MM-DD`, if not used, today's date will be assumed.
* `PAGE_NUMBER` - the number of page of the fetched results.
* `NO_OF_ERRORS_PER_PAGE` - number of errors per fetched page.

Response in JSON:
```json
{
   "pageNumber":0,
   "pageSize":10,
   "numberOfPages":2,
   "errorLogList":[
      {
         "id":38,
         "message":"Fetch of REST services failed(url: http://ss3/r1/DEV/GOV/1234/TEST/listMethods): 500 Server Error",
         "code":"500",
         "created": "2020-08-24T16:21:13",
         "memberClass":"GOV",
         "memberCode":"1234",
         "subsystemCode":"TEST",
         "groupCode":"",
         "serviceCode":"",
         "serviceVersion":null,
         "securityCategoryCode":"",
         "serverCode":"",
         "xroadInstance":"DEV"
      },
      {
         "id":39,
         "message":"Fetch of REST services failed(url: http://ss3/r1/DEV/GOV/1234/TESTCLIENT/listMethods): 500 Server Error",
         "code":"500",
         "created": "2020-08-24T16:21:13",
         "memberClass":"GOV",
         "memberCode":"1234",
         "subsystemCode":"TESTCLIENT",
         "groupCode":"",
         "serviceCode":"",
         "serviceVersion":null,
         "securityCategoryCode":"",
         "serverCode":"",
         "xroadInstance":"DEV"
      },
      {
         "id":40,
         "message":"Fetch of REST services failed(url: http://ss3/r1/DEV/GOV/1234/MASTER/listMethods): 500 Server Error",
         "code":"500",
         "created": "2020-08-24T16:21:13",
         "memberClass":"GOV",
         "memberCode":"1234",
         "subsystemCode":"MASTER",
         "groupCode":"",
         "serviceCode":"",
         "serviceVersion":null,
         "securityCategoryCode":"",
         "serverCode":"",
         "xroadInstance":"DEV"
      },
      {
         "id":41,
         "message":"Fetch of REST services failed(url: http://ss3/r1/DEV/GOV/1234/MANAGEMENT/listMethods): 500 Server Error",
         "code":"500",
         "created": "2020-08-24T16:31:39",
         "memberClass":"GOV",
         "memberCode":"1234",
         "subsystemCode":"MANAGEMENT",
         "groupCode":"",
         "serviceCode":"",
         "serviceVersion":null,
         "securityCategoryCode":"",
         "serverCode":"",
         "xroadInstance":"DEV"
      },
      {
         "id":42,
         "message":"Fetch of REST services failed(url: http://ss3/r1/DEV/GOV/1234/TESTCLIENT/listMethods): 500 Server Error",
         "code":"500",
         "created": "2020-08-24T16:31:39",
         "memberClass":"GOV",
         "memberCode":"1234",
         "subsystemCode":"TESTCLIENT",
         "groupCode":"",
         "serviceCode":"",
         "serviceVersion":null,
         "securityCategoryCode":"",
         "serverCode":"",
         "xroadInstance":"DEV"
      },
      {
         "id":43,
         "message":"Fetch of REST services failed(url: http://ss3/r1/DEV/GOV/1234/MANAGEMENT/listMethods): 500 Server Error",
         "code":"500",
         "created": "2020-08-24T16:34:46",
         "memberClass":"GOV",
         "memberCode":"1234",
         "subsystemCode":"MANAGEMENT",
         "groupCode":"",
         "serviceCode":"",
         "serviceVersion":null,
         "securityCategoryCode":"",
         "serverCode":"",
         "xroadInstance":"DEV"
      },
      {
         "id":44,
         "message":"Fetch of REST services failed(url: http://ss3/r1/DEV/GOV/1234/TESTCLIENT/listMethods): 500 Server Error",
         "code":"500",
         "created": "2020-08-24T16:34:46",
         "memberClass":"GOV",
         "memberCode":"1234",
         "subsystemCode":"TESTCLIENT",
         "groupCode":"",
         "serviceCode":"",
         "serviceVersion":null,
         "securityCategoryCode":"",
         "serverCode":"",
         "xroadInstance":"DEV"
      },
      {
         "id":45,
         "message":"Fetch of REST services failed(url: http://ss3/r1/DEV/GOV/1234/TESTCLIENT/listMethods): 500 Server Error",
         "code":"500",
         "created": "2020-08-24T16:36:25",
         "memberClass":"GOV",
         "memberCode":"1234",
         "subsystemCode":"TESTCLIENT",
         "groupCode":"",
         "serviceCode":"",
         "serviceVersion":null,
         "securityCategoryCode":"",
         "serverCode":"",
         "xroadInstance":"DEV"
      },
      {
         "id":46,
         "message":"Fetch of REST services failed(url: http://ss3/r1/DEV/GOV/1234/TESTCLIENT/listMethods): 500 Server Error",
         "code":"500",
         "created": "2020-08-24T16:39:30",
         "memberClass":"GOV",
         "memberCode":"1234",
         "subsystemCode":"TESTCLIENT",
         "groupCode":"",
         "serviceCode":"",
         "serviceVersion":null,
         "securityCategoryCode":"",
         "serverCode":"",
         "xroadInstance":"DEV"
      },
      {
         "id":47,
         "message":"Fetch of REST services failed(url: http://ss1/r1/DEV/GOV/1234/TESTCLIENT/listMethods): 500 Server Error",
         "code":"500",
         "created": "2020-08-24T16:41:34",
         "memberClass":"GOV",
         "memberCode":"1234",
         "subsystemCode":"TESTCLIENT",
         "groupCode":"",
         "serviceCode":"",
         "serviceVersion":null,
         "securityCategoryCode":"",
         "serverCode":"",
         "xroadInstance":"DEV"
      }
   ]
}
```

The response has the following fields:

* `pageNumber`
* `pageSize`
* `numberOfPages`
* `errorLogList`
    * `id`
    * `message`
    * `code`
    * `created`
    * `memberClass`
    * `memberCode`
    * `subsystemCode`
    * `groupCode`
    * `serviceCode`
    * `serviceVersion`
    * `securityCategoryCode`
    * `serverCode`
    * `xroadInstance`

### 3.2.8 List Security Servers

In order to fetch information about Security Servers in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

```bash
curl "http://<SERVER_ADDRESS>:8070/api/listSecurityServers" -H "Content-Type: application/json"
```

The required request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Response:
```json
{
  "securityServerDataList": [
    {
      "owner": {
        "memberClass": "GOV",
        "memberCode": "1234",
        "name": "ACME",
        "subsystemCode": null
      },
      "serverCode": "SS1",
      "address": "SS1",
      "clients": [
        {
          "memberClass": "GOV",
          "memberCode": "1234",
          "name": "ACME",
          "subsystemCode": "MANAGEMENT"
        }
      ]
    },
    {
      "owner": {
        "memberClass": "GOV",
        "memberCode": "1234",
        "name": "ACME",
        "subsystemCode": null
      },
      "serverCode": "ss4",
      "address": "ss4",
      "clients": [
        {
          "memberClass": "GOV",
          "memberCode": "1234",
          "name": "ACME",
          "subsystemCode": "THESUBSYSTEM"
        },
        {
          "memberClass": "COM",
          "memberCode": "222",
          "name": "FRUIT",
          "subsystemCode": null
        }
      ]
    }
  ]
}
```

The response has the following fields:

* `securityServerDataList`
    * `owner`
        * `memberClass`
        * `memberCode`
        * `name`
        * `subsystemCode`
    * `serverCode`
    * `address`
    * `clients`
        * `memberClass`
        * `memberCode`
        * `name`
        * `subsystemCode`

The **owner** property indicates the owner member of the Security Server

The **clients** property provides a list of clients using the Security Server, where a client can be considered a member 
when their `subsystemCode` is `null`.


### 3.2.9 List descriptors

In order to fetch information about subsystem descriptions in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

```bash
curl "http://<SERVER_ADDRESS>:8070/api/listDescriptors" -H "Content-Type: application/json"
```

The required parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Response:

```json
[
    {
        "x_road_instance":"DEV",
        "subsystem_name":{
            "et":"Subsystem Name ET",
            "en":"Subsystem Name EN"
        },
        "email":[
            {
                "name":"Firstname Lastname",
                "email":"yourname@yourdomain"
            }
        ],
        "member_class":"GOV",
        "member_code":"1234",
        "member_name":"ACME",
        "subsystem_code":"MANAGEMENT"
    },
    {
        "x_road_instance":"DEV",
        "subsystem_name":{
            "et":"Subsystem Name ET",
            "en":"Subsystem Name EN"
        },
        "email":[
            {
                "name":"Firstname Lastname",
                "email":"yourname@yourdomain"
            }
        ],
        "member_class":"GOV",
        "member_code":"1234",
        "member_name":"ACME",
        "subsystem_code":"TEST"
    }
]
```

The response has the following fields:

A list of:

* `x_road_instance`
* `member_class`
* `member_code`
* `member_name`
* `subsystem_code`
* `subsystem_name`
* `et`
* `en`

A list of emails with:

* `name`
* `email address`

The **subsystem_name** property indicates a user-friendly name of the subsystem, in addition to the more technical 
`subsystem_code` property. In the current implementation, the `subsystem_name` property contains default values, because 
X-Road currently does not provide such information, but the fields are still required for the X-Road Metrics to operate 
correctly.

The **email** property is a list consisting of name of a contact person and their e-mail address. 
In the current implementation, the property contains default values, because X-Road currently does not provide such 
information, but the fields are still required for the X-Road Metrics to operate correctly.

### 3.2.10 Get endpoints

In order to fetch information about service endpoints belonging to a specific `OPENAPI3` or `REST` service in the X-Road Catalog, 
an HTTP request has to be sent to a respective REST endpoint:

```bash
curl "http://<SERVER_ADDRESS>:8070/api/getEndpoints/<INSTANCE>/<MEMBER_CLASS>/<MEMBER_CODE>/<SUBSYSTEM_CODE>/<SERVICE_CODE>" -H "Content-Type: application/json"
```

The required parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.
* `INSTANCE` - name of X-Road instance, e.g., `DEV`.
* `MEMBER_CLASS` - member class, e.g., `GOV`.
* `MEMBER_CODE` - member code, e.g., `1234`.
* `SUBSYSTEM_CODE` - subsystem code, e.g., `TEST`.
* `SERVICE_CODE` - service code, e.g., `CATALOG_HEARTBEAT`.

Example request:
```bash
curl "http://localhost:8070/api/getEndpoints/DEV/GOV/1234/TEST/CATALOG_HEARTBEAT" -H "Content-Type: application/json"
```

Response:

```json
{
  "listOfServices":[
    {
      "memberClass":"GOV",
      "memberCode":"1234",
      "subsystemCode":"TEST",
      "serviceCode":"CATALOG_HEARTBEAT",
      "serviceVersion":null,
      "endpointList":[
        {
          "method":"GET",
          "path":"/heartbeat"
        }
      ],
      "xroadInstance":"DEV"
    }
  ]
}
```

The response has the following fields:

* `xroadInstance`
* `memberClass`
* `memberCode`
* `subsystemCode`
* `serviceCode`
* `serviceVersion`
* `endpointList`
    * `method`
    * `path`

### 3.2.11 Get Rest

In order to fetch information about a specific `REST` service in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

```bash
curl "http://<SERVER_ADDRESS>:8070/api/getRest/<INSTANCE>/<MEMBER_CLASS>/<MEMBER_CODE>/<SUBSYSTEM_CODE>/<SERVICE_CODE>" -H "Content-Type: application/json"
```

The required request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.
* `INSTANCE` - name of X-Road instance, e.g., `DEV`.
* `MEMBER_CLASS` - member class, e.g., `GOV`.
* `MEMBER_CODE` - member code, e.g., `1234`.
* `SUBSYSTEM_CODE` - subsystem code, e.g., `TEST`.
* `SERVICE_CODE` - service code, e.g., `CATALOG_HEARTBEAT`.

Example request:
```bash
curl "http://localhost:8070/api/getRest/DEV/GOV/1234/TEST/CATALOG_HEARTBEAT" -H "Content-Type: application/json"
```

Response:

```json
{
  "listOfServices":[
    {
      "memberClass":"GOV",
      "memberCode":"1234",
      "subsystemCode":"TEST",
      "serviceCode":"CATALOG_HEARTBEAT",
      "serviceVersion":null,
      "endpointList":[
        {
          "method":"GET",
          "path":"/heartbeat"
        }
      ],
      "xroadInstance":"DEV"
    }
  ]
}
```


The response has the following fields:

* `xroadInstance`
* `memberClass`
* `memberCode`
* `subsystemCode`
* `serviceCode`
* `serviceVersion`
* `endpointList`
    * `method`
    * `path`

### 3.2.12 Get Organization

**Note!** Requires the `fi` [profile](../BUILD.md#profiles).

Request:
```bash
curl "http://<SERVER_ADDRESS>:8070/api/getOrganization/<BUSINESS_CODE>" -H "Content-Type: application/json"
```

The required request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.
* `BUSINESS_CODE` - businessCode of the organization.

Example request:
```bash
curl "http://localhost:8070/api/getOrganization/0130729-0" -H "Content-Type: application/json"
```

Response:

```json
{
  "organizationData": {
    "businessCode": "0130729-0",
    "created": "2021-08-24T16:21:53",
    "changed": "2021-08-24T16:21:53",
    "fetched": "2022-02-03T13:19:34",
    "removed": null,
    "organizationType": "Municipality",
    "publishingStatus": "Published",
    "guid": "37962bfb-07a1-4f07-bad8-2b5c77e85451",
    "organizationNames": [
      {
        "language": "fi",
        "type": "Name",
        "value": "Pukkilan kunta",
        "created": "2021-08-24T16:21:53",
        "changed": "2021-08-24T16:21:53",
        "fetched": "2022-02-03T13:19:34",
        "removed": null
      }
    ]
  },
  "companyData": null
}
```


The response has the following fields:

* `organizationData`
    * `organizationType`
    * `publishingStatus`
    * `businessCode`
    * `guid`
    * `organizationNames`
        * `organizationName`
        * `language`
        * `type`
        * `value`
        * `created`
        * `changed`
        * `fetched`
        * `removed`
    * `organizationDescriptions`
        * `organizationDescription`
        * `language`
        * `type`
        * `value`
        * `created`
        * `changed`
        * `fetched`
        * `removed`
    * `emails`
        * `email`
        * `language`
        * `type`
        * `value`
        * `created`
        * `changed`
        * `fetched`
        * `removed`
    * `phoneNumbers`
        * `phoneNumber`
            * `language`
        * `additionalInformation`
        * `serviceChargeType`
        * `chargeDescription`
        * `prefixNumber`
        * `number`
        * `isFinnishServiceNumber`
        * `created`
        * `changed`
        * `fetched`
        * `removed`
    * `webPages`
        * `webPage`
        * `language`
        * `url`
        * `value`
        * `created`
        * `changed`
        * `fetched`
        * `removed`
    * `addresses`
        * `address`
        * `country`
        * `type`
        * `subType`
        * `streetAddresses`
            * `streetAddress`
            * `postalCode`
            * `latitude`
            * `longitude`
            * `coordinateState`
            * `streets`
                * `street`
                * `language`
                * `value`
                * `created`
                * `changed`
                * `fetched`
                * `removed`
            * `postOffices`
              * `streetAddressPostOffice`
              * `language`
              * `value`
              * `created`
              * `changed`
              * `fetched`
              * `removed`
            * `municipalities`
                * `streetAddressMunicipality`
                    * `code`
                    * `streetAddressMunicipalityNames`
                    * `streetAddressMunicipalityName`
                    * `language`
                    * `value`
                    * `created`
                    * `changed`
                    * `fetched`
                    * `removed`
                * `created`
                * `changed`
                * `fetched`
                * `removed`
                * `additionalInformation`
                    * `streetAddressAdditionalInformation`
                        * `language`
                        * `value`
                        * `created`
                        * `changed`
                        * `fetched`
                        * `removed`
            * `created`
            * `changed`
            * `fetched`
            * `removed`
        * `created`
        * `changed`
        * `fetched`
        * `removed`
* `companyData`
    * `companyForm`
    * `detailsUri`
    * `businessCode`
    * `name`
    * `registrationDate`
    * `businessAddresses`
        * `businessAddress`
            * `source`
            * `version`
            * `careOf`
            * `street`
            * `postCode`
            * `city`
            * `language`
            * `type`
            * `country`
            * `registrationDate`
            * `created`
            * `changed`
            * `fetched`
            * `removed`
    * `businessAuxiliaryNames`
        * `businessAuxiliaryName`
            * `source`
            * `ordering`
            * `version`
            * `name`
            * `language`
            * `registrationDate`
            * `created`
            * `changed`
            * `fetched`
            * `removed`
    * `businessIdChanges`
        * `businessIdChange`
            * `source`
            * `description`
            * `reason`
            * `changeDate`
            * `change`
            * `oldBusinessId`
            * `newBusinessId`
            * `language`
            * `created`
            * `changed`
            * `fetched`
            * `removed`
    * `businessLines`
        * `businessLine`
            * `source`
            * `ordering`
            * `version`
            * `name`
            * `language`
            * `registrationDate`
            * `created`
            * `changed`
            * `fetched`
            * `removed`
    * `businessNames`
        * `businessName`
            * `companyId`
            * `source`
            * `ordering`
            * `version`
            * `name`
            * `language`
            * `registrationDate`
            * `endDate`
            * `created`
            * `changed`
            * `fetched`
            * `removed`
    * `companyForms`
        * `companyForm`
            * `source`
            * `version`
            * `name`
            * `language`
            * `type`
            * `registrationDate`
            * `created`
            * `changed`
            * `fetched`
            * `removed`
    * `contactDetails`
        * `contactDetail`
            * `source`
            * `version`
            * `language`
            * `value`
            * `type`
            * `registrationDate`
            * `created`
            * `changed`
            * `fetched`
    * `languages`
        * `language`
            * `source`
            * `version`
            * `name`
            * `language`
            * `registrationDate`
            * `created`
            * `changed`
            * `fetched`
    * `liquidations`
        * `liquidation`
            * `companyId`
            * `source`
            * `version`
            * `name`
            * `language`
            * `registrationDate`
            * `endDate`
            * `created`
            * `changed`
            * `fetched`
            * `removed`
    * `registeredEntries`
        * `registeredEntry`
            * `companyId`
            * `description`
            * `status`
            * `register`
            * `language`
            * `authority`
            * `registrationDate`
            * `endDate`
            * `created`
            * `changed`
            * `fetched`
            * `removed`
    * `registeredOffices`
        * `registeredOffice`
            * `companyId`
            * `source`
            * `ordering`
            * `version`
            * `name`
            * `language`
            * `registrationDate`
            * `endDate`
            * `created`
            * `changed`
            * `fetched`
            * `removed`

The **organizationData** property holds values for different data related to organization fetched from an external API.

The **companyData** property holds values for different data related to company fetched from another external API in case 
the organization with the given `businessCode` was not found among the data retrieved from the first API.

### 3.2.13 Get Organization changes

**Note!** Requires the `fi` [profile](../BUILD.md#profiles).

Request

```bash
curl "http://<SERVER_ADDRESS>:8070/api/getOrganizationChanges/<BUSINESS_CODE>?startDate=<START_DATE>&endDate=<END_DATE>" -H "Content-Type: application/json"
```

The required request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.
* `BUSINESS_CODE` - `businessCode` of the organization.
* `START_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.
* `END_DATE` - an optional parameter(a string in format `YYYY-MM-DD`), if not used, today's date will be assumed.

Example request:
```bash
curl "http://localhost:8070/api/getOrganizationChanges/0130729-0?startDate=2021-08-24&endDate=2021-08-25" -H "Content-Type: application/json"
```

Response:

```json
{
   "changed":true,
   "changedValueList":[
      {
         "name":"Organization"
      },
      {
         "name":"OrganizationName"
      },
      {
         "name":"OrganizationDescription"
      },
      {
         "name":"Email"
      },
      {
         "name":"PhoneNumber"
      },
      {
         "name":"WebPage"
      },
      {
         "name":"Address"
      },
      {
         "name":"Street"
      },
      {
         "name":"Street"
      },
      {
         "name":"Street"
      },
      {
         "name":"Street"
      },
      {
         "name":"Street"
      },
      {
         "name":"Street"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress Municipality"
      },
      {
         "name":"StreetAddress Municipality Name"
      },
      {
         "name":"StreetAddress Municipality Name"
      },
      {
         "name":"StreetAddress Municipality Name"
      },
      {
         "name":"Address"
      },
      {
         "name":"Street"
      },
      {
         "name":"Street"
      },
      {
         "name":"Street"
      },
      {
         "name":"Street"
      },
      {
         "name":"Street"
      },
      {
         "name":"Street"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress PostOffice"
      },
      {
         "name":"StreetAddress Municipality"
      },
      {
         "name":"StreetAddress Municipality Name"
      },
      {
         "name":"StreetAddress Municipality Name"
      },
      {
         "name":"StreetAddress Municipality Name"
      }
   ]
}
```


The response has the following fields:

* `changed`
* `changedValueList`
    * `name`

The **changed** property is a boolean value which indicates whether there were any changes in all the data.
The **changedValueList** property is a list of changed data fields.
The **name** property is the name of the data field which has changes.


### 3.2.14 Check organization heartbeat

**Note!** Requires the `fi` [profile](../BUILD.md#profiles).

In order to fetch X-Road Catalog organization heartbeat information, an HTTP request has to be sent to a respective REST endpoint:

```bash
curl "http://<SERVER_ADDRESS>:8070/api/organizationHeartbeat" -H "Content-Type: application/json"
```

The required request parameters are:

* `SERVER_ADDRESS` - the server address on which the X-Road Catalog Lister is running on, e.g., `localhost`.

Response in JSON:
```json
{"appWorking":true,
 "dbWorking":true,
 "appName":"X-Road Catalog Lister",
 "appVersion":"1.2.1",
 "systemTime":[2021,9,20,10,12,15,132000000],
 "lastCollectionData":
 {"organizationsLastFetched":[2021,9,20,10,10,55,153000000],
  "companiesLastFetched":null
 }
}
```

The response has the following fields:

* `appWorking`
* `dbWorking`
* `appName`
* `appVersion`
* `systemTime`
* `lastCollectionData`
    * `organizationsLastFetched`
    * `companiesLastFetched`

### 4. X-Road Catalog Persistence

The purpose of the module is to persist and read persisted data. Used by the X-Road Catalog Collector and X-Road 
Catalog Lister modules.

More information about the [X-Road Catalog Persistence](../xroad-catalog-persistence/README.md) module.
