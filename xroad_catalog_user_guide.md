# X-Road Catalog User Guide
Version: 1.1.1
Doc. ID: XRDCAT-CONF

---

## Version history <!-- omit in toc -->
| Date       | Version     | Description                                                                  | Author             |
|------------|-------------|------------------------------------------------------------------------------|--------------------|
| 21.07.2021 | 1.0.0       | Initial draft                                                                | Bert Viikmäe       |
| 21.07.2021 | 1.0.1       | Add installation section                                                     | Bert Viikmäe       |
| 22.07.2021 | 1.0.2       | Add X-Road Catalog Collector section                                         | Bert Viikmäe       |
| 23.07.2021 | 1.0.3       | Add X-Road Catalog Lister section                                            | Bert Viikmäe       |
| 23.07.2021 | 1.0.4       | Add X-Road Catalog Persistence section                                       | Bert Viikmäe       |
| 25.08.2021 | 1.0.5       | Add list distinct services endpoint description                              | Bert Viikmäe       |
| 02.09.2021 | 1.0.6       | Add list errors endpoint description                                         | Bert Viikmäe       |
| 22.09.2021 | 1.0.7       | Update heartbeat endpoint description                                        | Bert Viikmäe       |
| 26.10.2021 | 1.0.8       | Update listErrors endpoint description                                       | Bert Viikmäe       |
| 27.10.2021 | 1.1.0       | Add listSecurityServers and listDescriptors endpoint descriptions            | Bert Viikmäe       |
| 15.12.2021 | 1.1.1       | Update listErrors endpoint description                                       | Bert Viikmäe       |

## Table of Contents <!-- omit in toc -->

<!-- toc -->
<!-- vim-markdown-toc GFM -->

* [License](#license)
* [1. Introduction](#1-introduction)
  * [1.1 Target Audience](#11-target-audience)
* [2. Installation](#2-installation)
    * [2.1 Prerequisites to Installation](#21-prerequisites-to-installation)
    * [2.2 Installation](#22-installation)
    * [2.3 SSL](#23-ssl)
    * [2.4 Status of services](#24-status-of-services)
    * [2.5 Logs](#25-logs)
* [3. Introduction](#3-introduction)
    * [3.1 X-Road Catalog Collector](#31-x-road-catalog-collector)
        * [3.1.1 Build](#311-build)
        * [3.1.2 Run](#312-run)
        * [3.1.3 Run against remote security server](#313-run-against-remote-security-server)
        * [3.1.4 Build RPM Packages on Non-RedHat Platform](#314-build-rpm-packages-on-non-redhat-platform)
    * [3.2 X-Road Catalog Lister](#32-x-road-catalog-lister)
        * [3.2.1 Build](#321-build)
        * [3.2.2 Run](#322-run)
        * [3.2.3 Main endpoints](#323-main-endpoints)
            * [3.2.3.1 List all members](#3231-list-all-members)
            * [3.2.3.2 Retrieve WSDL descriptions](#3232-retrieve-wsdl-descriptions)   
            * [3.2.3.3 Retrieve OPENAPI descriptions](#3233-retrieve-openapi-descriptions)
            * [3.2.3.4 Get service type](#3234-get-service-type) 
            * [3.2.3.5 Check if member is provider](#3235-check-if-member-is-provider)  
            * [3.2.3.6 List organizations](#3236-list-organizations)  
            * [3.2.3.7 List organization changes](#3237-list-organization-changes)
            * [3.2.3.8 List companies](#3238-list-companies) 
            * [3.2.3.9 List company changes](#3239-list-company-changes) 
            * [3.2.3.10 List errors](#32310-list-errors) 
            * [3.2.3.11 List service statistics](#32311-list-service-statistics) 
            * [3.2.3.12 List service statistics in CSV format](#32312-list-service-statistics-in-csv-format)
            * [3.2.3.13 List services](#32313-list-services)  
            * [3.2.3.14 List services in CSV format](#32314-list-services-in-csv-format)  
            * [3.2.3.15 Check heartbeat](#32315-check-heartbeat)  
            * [3.2.3.16 List distinct service statistics](#32316-list-distinct-service-statistics)  
            * [3.2.3.17 List errors](#32317-list-errors) 
            * [3.2.3.18 List security servers](#32318-list-security-servers) 
            * [3.2.3.19 List descriptors](#32319-list-descriptors)  
    * [3.3 X-Road Catalog Persistence](#33-x-road-catalog-persistence)  
        * [3.3.1 Create database](#331-create-database) 
        * [3.3.2 Build](#332-build)   
        * [3.3.3 Profiles](#333-profiles)  
        * [3.3.4 Run](#334-run)  
                     
<!-- vim-markdown-toc -->
<!-- tocstop -->

## License

This document is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License. To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.

## 1. Introduction

### 1.1 Target Audience

The intended audience of this user guide are system administrators responsible for managing and configuring systems and applicatons that use X-Road services.
The document is intended for readers with a good knowledge of Linux server management, computer networks, and the X-Road functioning principles.

## 2. Installation

## 2.1 Prerequisites to Installation

Running the X-Road Catalog software requires Linux (Ubuntu or RHEL). If you are using some other operating system (e.g. Windows or macOS), the easiest option is to first install Ubuntu 18.04 or RHEL 7.0 into a virtual machine.

*Required for building*
* OpenJDK / JDK version 8
* Gradle

*Recommended for development environment*
* Docker (for deb/rpm packaging)
* LXD (https://linuxcontainers.org/lxd/)
  * for setting up a local X-Road instance
* Ansible
  * for automating the X-Road instance installation

The development environment should have at least 8GB of memory and 20GB of free disk space (applies to a virtual machine as well), especially if you set up a local X-Road instance.


## 2.2 Installation

The installable software consists of xroad-catalog-collector and xroad-catalog-lister. Both are provided as RPM packages. 

```sudo yum install xroad-catalog-lister xroad-catalog-collector``` or ```rpm -i install xroad-catalog-lister xroad-catalog-collector```

Instructions on how to build the RPM packages using Docker can be found:
[here](xroad-catalog-collector/README.md#build-rpm-packages-on-non-redhat-platform)
and
[here](xroad-catalog-lister/README.md#build-rpm-packages-on-non-redhat-platform)

Configure parameters in /etc/xroad/xroad-catalog/collector-production.properties, especially X-Road instance information and URL of security server.
```
xroad-catalog.xroad-instance=FI
xroad-catalog.member-class=GOV
xroad-catalog.member-code=1945065-0
xroad-catalog.subsystem-code=VAAKKO1
xroad-catalog.security-server-host=http://gdev-rh1.i.palveluvayla.com
```

Change also the database password in /etc/xroad/xroad-catalog/catalogdb-production.properties.
```
spring.datasource.password=password
```
and in the DB:
```
sudo -u postgres psql -U postgres -d postgres -c "alter user xroad_catalog with password 'password';"
```

Make sure that the services are enabled on boot and restart services in order to make the changes to have effect.
```
# Enable the service to start on boot
sudo systemctl enable xroad-catalog-lister
sudo systemctl enable xroad-catalog-collector
sudo systemctl restart xroad-catalog-lister
sudo systemctl restart xroad-catalog-collector
```

## 2.3 SSL

If secure connection to the security server is required, add the server's cert for the JVM trust store, for example as follows.

```
sudo cp cert.pem /etc/pki/ca-trust/source/anchors/
sudo update-ca-trust extract
```

If you don't have the certificate, it can be asked as follows:

```
openssl s_client -showcerts -connect kapvlpt02.csc.fi:443  </dev/null
```

If listMethods requires authentication, create a certificate and add it to keystore file /etc/xroad/xroad-catalog/keystore as follows:
```
sudo keytool -alias xroad-catalog -genkeypair -keystore /etc/xroad/xroad-catalog/keystore -validity 7300 -keyalg RSA -keysize 2048 -sigalg SHA256withRSA -dname C=FI,CN=xroad-catalog

keytool -keystore /etc/xroad/xroad-catalog/keystore -exportcert -rfc -alias xroad-catalog > xroad-catalog.cer
```

Created xroad-catalog.cer must be added to security server (Through UI: Security Server Clients > SELECT SERVICE > Internal Servers > Internal TLS Certificates > ADD)

The keystore password can be configured in /etc/xroad/xroad-catalog/collector-production.properties
```
xroad-catalog.ssl-keystore-password=changeit
```


## 2.4 Status of Services

This instruction expects that xroad-catalog-collector and xroad-catalog-lister are installed on the same server. It is also possible to install them on different servers but then database settings need to be set for both services. For server of xroad-catalog-lister file /etc/xroad/xroad-catalog/catalogdb-production.properties must be manually created.

```
[root@ip-172-31-128-199 xroad-catalog]# systemctl | grep xroad
xroad-async.service                              loaded active running   X-Road Proxy
xroad-catalog-collector.service                  loaded active running   X-Road Catalog Collector
xroad-catalog-lister.service                     loaded active running   X-Road Catalog Lister
xroad-confclient.service                         loaded active running   X-Road Proxy
xroad-jetty9.service                             loaded active running   X-Road Jetty server
xroad-monitor.service                            loaded active running   X-Road Monitor
xroad-proxy.service                              loaded active running   X-Road Proxy
xroad-signer.service                             loaded active running   X-Road signer
```

```
[root@ip-172-31-128-199 xroad-catalog]# service xroad-catalog-collector status
Redirecting to /bin/systemctl status  xroad-catalog-collector.service
● xroad-catalog-collector.service - X-Road Catalog Collector
   Loaded: loaded (/usr/lib/systemd/system/xroad-catalog-collector.service; disabled; vendor preset: enabled)
   Active: active (running) since Thu 2016-04-07 11:00:42 EEST; 3min 11s ago
 Main PID: 7298 (java)
   CGroup: /system.slice/xroad-catalog-collector.service
           └─7298 /bin/java -Dspring.profiles.active=production -jar /usr/lib/xroad-catalog/xroad-catalog-collector.jar -...
Apr 07 11:01:12 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-collector[7298]: Hibernate: select subsystem0..._
Apr 07 11:01:12 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-collector[7298]: Hibernate: select wsdls0_.ser...
Apr 07 11:01:12 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-collector[7298]: Hibernate: select nextval ('...)
Apr 07 11:01:12 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-collector[7298]: Hibernate: insert into wsdl ...)
Apr 07 11:01:12 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-collector[7298]: 2016-04-07 11:01:12.211  INF...y
Apr 07 11:01:13 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-collector[7298]: Hibernate: select service0_....s
Apr 07 11:01:13 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-collector[7298]: Hibernate: select subsystem0..._
Apr 07 11:01:13 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-collector[7298]: Hibernate: select wsdls0_.ser...
Apr 07 11:01:13 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-collector[7298]: Hibernate: update wsdl set d...?
Apr 07 11:01:13 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-collector[7298]: 2016-04-07 11:01:13.766  INF...y
Hint: Some lines were ellipsized, use -l to show in full.
```

```
[root@ip-172-31-128-199 xroad-catalog]# service xroad-catalog-lister status
Redirecting to /bin/systemctl status  xroad-catalog-lister.service
● xroad-catalog-lister.service - X-Road Catalog Lister
   Loaded: loaded (/usr/lib/systemd/system/xroad-catalog-lister.service; enabled; vendor preset: enabled)
   Active: active (running) since Thu 2016-04-07 07:06:03 EEST; 3h 58min ago
 Main PID: 763 (java)
   CGroup: /system.slice/xroad-catalog-lister.service
           └─763 /bin/java -Dserver.port=8070 -Dspring.profiles.active=production -jar /usr/lib/xroad-catalog/xroad-catal...
Apr 07 11:01:11 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-lister[763]: 2016-04-07 11:01:11.084 DEBUG 76...t
Apr 07 11:01:11 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-lister[763]: 2016-04-07 11:01:11.101 DEBUG 76...t
Apr 07 11:01:11 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-lister[763]: 2016-04-07 11:01:11.102 DEBUG 76...l
Apr 07 11:01:11 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-lister[763]: 2016-04-07 11:01:11.105 DEBUG 76...]
Apr 07 11:01:11 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-lister[763]: 2016-04-07 11:01:11.105  INFO 76...s
Apr 07 11:01:11 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-lister[763]: 2016-04-07 11:01:11.105 DEBUG 76...y
Apr 07 11:01:11 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-lister[763]: 2016-04-07 11:01:11.600 DEBUG 76...]
Apr 07 11:01:11 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-lister[763]: 2016-04-07 11:01:11.611 DEBUG 76...]
Apr 07 11:01:11 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-lister[763]: 2016-04-07 11:01:11.691 DEBUG 76...t
Apr 07 11:01:11 ip-172-31-128-199.eu-west-1.compute.internal xroad-catalog-lister[763]: 2016-04-07 11:01:11.692 DEBUG 76...t
Hint: Some lines were ellipsized, use -l to show in full.
```

## 2.5 Logs

```
sudo journalctl -fu xroad-catalog-collector --since="2016-04-07 10:50 --output=cat"
```

## 3. Introduction

The purpose of this piece of software is to collect information, more specifically members, subsystems and services, from an X-Road Catalog instance and offer an interface where the information can be read.

The software consists of three parts:
- [xroad-catalog-collector](xroad-catalog-collector/README.md)
  * Collects members, subsystems and services from the X-Road instance and stores them to the postgresql database. 
  * Implemented using concurrent Akka actors. 
- [xroad-catalog-lister](xroad-catalog-lister/README.md)
  * SOAP interface that offers information collected by collector. 
  * Can be used as an X-Road service (X-Road headers are in place)
- [xroad-catalog-persistence](xroad-catalog-persistence/README.md)
  * Library used to persist and read persisted data. Used by both of the above.
  
![X-Road Catalog overview](architecture.png)

### 3.1 X-Road Catalog Collector

The purpose of this piece of software is to collect members, subsystems and services from the X-Road instance and store them to the PostgreSQL database. 

### 3.1.1 Build

X-Road Catalog Collector can be built by running:

``` $ ./gradlew clean build ```


### 3.1.2 Run

X-Road Catalog Collector can be run by using Gradle:

``` $ ./gradlew bootRun ```
    
or by running it from a JAR file:

``` $ java -jar target/xroad-catalog-collector-1.0-SNAPSHOT.jar ```

### 3.1.3 Run against remote security server

First create an ssh tunnel to local port 9000, for example

``` $ ssh -nNT -L 9000:gdev-rh1.i.palveluvayla.com:80 dev-is.palveluvayla.com ```

Then run the collector with profile sshtest:

``` $ java -Dspring.profiles.active=sshtest -jar build/libs/xroad-catalog-collector.jar --spring.config.name=collector,catalogdb ```

### 3.1.4 Build RPM Packages on Non-RedHat Platform

First make sure that xroad-catalog-persistence is located next to xroad-catalog-collector. The RPM build uses sql files from xroad-catalog-persistence/src/main/sql.

```
    $ ../gradlew clean build
    $ docker build -t collector-rpm packages/xroad-catalog-collector/docker
    $ docker run -v $PWD/..:/workspace collector-rpm
```
    
### 3.2 X-Road Catalog Lister

The purpose of this piece of software is to provide a webservice which lists all the X-Road members and the services they provide together with service descriptions

### 3.2.1 Build

X-Road Catalog Lister can be built by running:

``` $ sh ./gradlew clean build ```

### 3.2.2 Run

X-Road Catalog Lister can be run using Gradle:

``` $ sh ./gradlew bootRun ```

or running it from a JAR file:

``` $ java -jar build/libs/xroad-catalog-lister.jar --spring.config.name=lister,catalogdb ```

### 3.2.3 Main endpoints

The main endpoints this software provides: 
* ListMembers - SOAP endpoint to list all the members the Catalog Collector has stored to the db
* GetWsdl - SOAP endpoint to retrieve a WSDL description for a given service
* GetOpenAPI - SOAP endpoint to retrieve an OpenAPI description for a given service
* GetServiceType - SOAP endpoint for requesting whether a given service is of type SOAP, REST or OPENAPI
* IsProvider - SOAP endpoint for requesting whether a given member is a provider
* GetOrganizations - SOAP endpoint for requesting public organization details
* HasOrganizationChanged - SOAP endpoint for requesting whether given public organization has some of its details changed
* GetCompanies - SOAP endpoint for requesting private company details
* HasCompanyChanged - SOAP endpoint for requesting whether given private company has some of its details changed
* GetErrors - SOAP endpoint for requesting a list of errors related to fetching data from different apis and security servers
* GetServiceStatistics - REST endpoint for requesting a list of statistics, consisting of numbers of SOAP/REST services over time
* GetServiceStatisticsCSV - REST endpoint for requesting a list of statistics in CSV format, consisting of numbers of SOAP/REST services over time
* GetListOfServices - REST endpoint for requesting a list of members and related subsystems, services and security servers over time
* GetListOfServicesCSV - REST endpoint for requesting a list of members and related subsystems, services and security servers in CSV format
* heartbeat - REST endpoint for requesting the heartbeat of X-Road Catalog

### 3.2.3.1 List all members

In order to list all members and related subsystems and services, a request in XML format has to be sent to the respective SOAP endpoint:

``` $ curl -k -d @servicerequest.xml --header "Content-Type: text/xml" -X POST http://localhost:8080/ws/ListMembers ```

Contents of the example ```servicerequest.xml``` file:
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
         <xrcl:changedAfter>2011-01-01</xrcl:changedAfter>
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
                  <ns2:created>2020-03-20T10:25:51.632+02:00</ns2:created>
                  <ns2:changed>2020-03-20T10:25:51.632+02:00</ns2:changed>
                  <ns2:fetched>2020-03-20T12:31:07.223+02:00</ns2:fetched>
                </ns2:service>
                <ns2:service>
                  <ns2:serviceCode>authCertDeletion</ns2:serviceCode>
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

The XML response has a ```<SOAP-ENV:Body>``` element with the following structure:

* ListMembersResponse
  * memberList
    * member
      * subsystems
        * subsystem
          * subsystemCode
          * services
            * service
            * serviceCode
            * wsdl (if the given service description is a WSDL description)
              * externalId

In addition each subsystem, service and wsdl contains also fields ```created```, ```changed```, ```fetched``` and ```removed```, 
reflecting the creation, change, fetch and removal (when a subsystem/service/wsdl was fetched by X-Road Catalog Collector to the DB) dates

### 3.2.3.2 Retrieve WSDL descriptions

In order to retrieve a WSDL service description, a request in XML format has to be sent to the respective SOAP endpoint:

``` $ curl -k -d @wsdlrequest.xml --header "Content-Type: text/xml" -X POST http://localhost:8080/ws/GetWsdl ```

Contents of the example ```wsdlrequest.xml``` file:
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

In the request there is a field ```externalId```, indicating the id of the WSDL to be retrieved

The response of the given request is in XML format, containing the WSDL service description. 
A longer example provided in [xroad-catalog-lister](xroad-catalog-lister/README.md)

### 3.2.3.3 Retrieve OPENAPI descriptions

In order to retrieve an OPENAPI service descriptions, a request in XML format has to be sent to the respective SOAP endpoint:

``` $ curl -k -d @openapirequest.xml --header "Content-Type: text/xml" -X POST http://localhost:8080/ws/GetOpenAPI ```

Contents of the example ```openapirequest.xml``` file:
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

In the request there is a field ```externalId```, indicating the id of the OPENAPI to be retrieved

The response of the given request is in XML format, containing the OPENAPI service description. 
A longer example provided in [xroad-catalog-lister](xroad-catalog-lister/README.md)

### 3.2.3.4 Get service type

In order to retrieve service type information, a request in XML format has to be sent to the respective SOAP endpoint:

``` $ curl -k -d @GetServiceTypeRequest.xml --header "Content-Type: text/xml" -X POST http://localhost:8080/ws/GetServiceType ```

Contents of the example ```GetServiceTypeRequest.xml``` file:
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

In the request there are the following fields that need to be filled:
 ```xRoadInstance``` - X-Road Instance name, e.g. DEV
 ```memberClass``` - member class, e.g. GOV
 ```memberCode``` -  member code, e.g. 1234
 ```serviceCode``` - service code, e.g. authCertDeletion
 ```subsystemCode``` - subsystem code, e.g. MANAGEMENT
 ```serviceVersion``` - service version, e.g. v1

The XML response has a ```<SOAP-ENV:Body>``` element with the following structure:

* GetServiceTypeResponse
  * type (values: REST/OPENAPI3/WSDL)

A longer example provided in [xroad-catalog-lister](xroad-catalog-lister/README.md)
                   
### 3.2.3.5 Check if member is provider

In order to check if a given X-Road member (security server) is a provider, a request in XML format has to be sent to the respective SOAP endpoint:

``` $ curl -k -d @IsProviderRequest.xml --header "Content-Type: text/xml" -X POST http://localhost:8080/ws/IsProvider ```

Contents of the example ```IsProviderRequest.xml``` file:
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

In the request there are the following fields that need to be filled:
 ```xRoadInstance``` - X-Road Instance name, e.g. DEV
 ```memberClass``` - member class, e.g. GOV
 ```memberCode``` -  member code, e.g. 1234

The XML response has a ```<SOAP-ENV:Body>``` element with the following structure:

* IsProviderResponse
  * provider (values: true/false)

A longer example provided in [xroad-catalog-lister](xroad-catalog-lister/README.md)

### 3.2.3.6 List organizations

In order to fetch information about organizations, a request in XML format has to be sent to the respective SOAP endpoint:

``` $ curl -k -d @GetOrganizationsRequest.xml --header "Content-Type: text/xml" -X POST http://localhost:8080/ws/GetOrganizations ```

Contents of the example ```GetOrganizationsRequest.xml``` file:
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

In the request there are the following fields that need to be filled:
 ```businessCode``` - business code of the searchable organization, e.g. 0181367-9

The XML response has a ```<SOAP-ENV:Body>``` element with the following structure:

* GetOrganizationsResponse
  * organizationList
    * organization
        * organizationType
        * publishingStatus
        * businessCode
        * guid
        * organizationNames
	      * organizationName
            * language
        	* type
        	* value
        	* created
        	* changed
        	* fetched
        	* removed
        * organizationDescriptions
          * organizationDescription
            * language
            * type
            * value
            * created
            * changed
            * fetched
            * removed
        * emails
          * email
            * language
        	* type
        	* value
        	* created
        	* changed
        	* fetched
        	* removed
        * phoneNumbers
	      * phoneNumber
       	    * language
            * additionalInformation
            * serviceChargeType
            * chargeDescription
            * prefixNumber
            * number
            * isFinnishServiceNumber
        	* created
        	* changed
        	* fetched
        	* removed
        * webPages
	      * webPage
            * language
            * url
            * value
        	* created
        	* changed
        	* fetched
        	* removed
        * addresses
	      * address
            * country
            * type
            * subType
            * streetAddresses
              * streetAddress
                * postalCode
                * latitude
                * longitude
                * coordinateState
                * streets
                  * street
        		  * language
                  * value
        		  * created
        		  * changed
        		  * fetched
        		  * removed
              * postOffices
               	  * streetAddressPostOffice
        		    * language
                	* value
        			* created
        			* changed
        			* fetched
        			* removed
              * municipalities
                * streetAddressMunicipality
                  * code
                  * streetAddressMunicipalityNames
                  * streetAddressMunicipalityName
        		  * language
                  * value
        		  * created
        		  * changed
        		  * fetched
        		  * removed
        		* created
        	    * changed
        	    * fetched
        	    * removed
		      * additionalInformation
        		  * streetAddressAdditionalInformation
               	    * language
                	* value
        			* created
        			* changed
        			* fetched
        			* removed
        	* created
        	* changed
        	* fetched
        	* removed
        * created
        * changed
        * fetched
        * removed
        
In addition, most sections also contain fields ```created```, ```changed```, ```fetched``` and ```remoced```, 
reflecting the creation, change, fetch and removal (when the respective data was fetched by X-Road Catalog Collector to the DB) dates

A longer example provided in [xroad-catalog-lister](xroad-catalog-lister/README.md)

### 3.2.3.7 List organization changes

In order to fetch information about changed organization fields, a request in XML format has to be sent to the respective SOAP endpoint:

``` $ curl -k -d @HasOrganizationChangedRequest.xml --header "Content-Type: text/xml" -X POST http://localhost:8080/ws/HasOrganizationChanged ```

Contents of the example ```HasOrganizationChangedRequest.xml``` file:
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
         <xrcl:changedAfter>2011-01-01</xrcl:changedAfter>
      </xrcl:HasOrganizationChanged>
   </soapenv:Body>
</soapenv:Envelope>
```

In the request there are the following fields that need to be filled:
 ```guid``` - unique id of the searchable organization, e.g. e6b33f11-bb47-496e-98c5-6a736dae6014
 ```changedAfter``` - date to check against after which an organization may have changed its field values, e.g. 2011-01-01

The XML response has a ```<SOAP-ENV:Body>``` element with the following structure:

* HasOrganizationChangedResponse
  * changed (values true/false)
  * changedValueList
    * changedValue
      * name (values e.g. OrganizationName, Email, Address etc)
 
A longer example provided in [xroad-catalog-lister](xroad-catalog-lister/README.md)

### 3.2.3.8 List companies

In order to fetch information about companies, a request in XML format has to be sent to the respective SOAP endpoint:

``` $ curl -k -d @GetCompaniesRequest.xml --header "Content-Type: text/xml" -X POST http://localhost:8080/ws/GetCompanies ```

Contents of the example ```GetCompaniesRequest.xml``` file:
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

In the request there are the following fields that need to be filled:
 ```businessId``` - business code of the searchable organization, e.g. 1710128-9

The XML response has a ```<SOAP-ENV:Body>``` element with the following structure:

* GetCompaniesResponse
  * companyList
    * company
      * companyForm
      * detailsUri
      * businessId
      * name
      * registrationDate
      * businessAddresses 
        * businessAddress
          * source
          * version
          * careOf
          * street
          * postCode
          * city
          * language
          * type
          * country
          * registrationDate
          * created
          * changed
          * fetched
          * removed
      * businessAuxiliaryNames
        * businessAuxiliaryName
          * source
          * ordering
          * version
          * name
          * language
          * registrationDate
          * created
          * changed
          * fetched
          * removed
      * businessIdChanges
        * businessIdChange
          * source
          * description
          * reason
          * changeDate
          * change
          * oldBusinessId
          * newBusinessId
          * language
          * created
          * changed
          * fetched
          * removed
      * businessLines
        * businessLine
          * source
          * ordering
          * version
          * name
          * language
          * registrationDate
          * created
          * changed
          * fetched
          * removed
      * businessNames
        * businessName
          * companyId
          * source
          * ordering
          * version
          * name
          * language
          * registrationDate
          * endDate
          * created
          * changed
          * fetched 
          * removed         
      * companyForms
        * companyForm
          * source
          * version
          * name
          * language
          * type
          * registrationDate
          * created
          * changed
          * fetched
          * removed
      * contactDetails
        * contactDetail
          * source
          * version
          * language
          * value
          * type
          * registrationDate
          * created
          * changed
          * fetched
      * languages
        * language
          * source
          * version
          * name
          * language
          * registrationDate
          * created
          * changed
          * fetched
      * liquidations
        * liquidation
          * companyId
          * source
          * version
          * name
          * language
          * registrationDate
          * endDate
          * created
          * changed
          * fetched 
          * removed  
      * registeredEntries
        * registeredEntry
          * companyId
          * description
          * status
          * register
          * language
          * authority
          * registrationDate
          * endDate
          * created
          * changed
          * fetched 
          * removed 
      * registeredOffices
        * registeredOffice
          * companyId
          * source
          * ordering
          * version
          * name
          * language
          * registrationDate
          * endDate
          * created
          * changed
          * fetched 
          * removed   

A longer example provided in [xroad-catalog-lister](xroad-catalog-lister/README.md)
                             
### 3.2.3.9 List company changes

In order to fetch information about changed company fields, a request in XML format has to be sent to the respective SOAP endpoint:

``` $ curl -k -d @HasCompanyChangedRequest.xml --header "Content-Type: text/xml" -X POST http://localhost:8080/ws/HasCompanyChanged ```

Contents of the example ```HasCompanyChangedRequest.xml``` file:
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
            <xrcl:changedAfter>2011-01-01</xrcl:changedAfter>
        </xrcl:HasCompanyChanged>
    </soapenv:Body>
</soapenv:Envelope>
```

In the request there are the following fields that need to be filled:
 ```businessId``` - business id of the searchable company, e.g. 1710128-9
 ```changedAfter``` - date to check against after which a company may have changed its field values, e.g. 2011-01-01

The XML response has a ```<SOAP-ENV:Body>``` element with the following structure:

* HasCompanyChangedResponse
  * changed (values true/false)
  * changedValueList
    * changedValue
      * name (values e.g. Company, ContactDetail etc)
 
A longer example provided in [xroad-catalog-lister](xroad-catalog-lister/README.md)

### 3.2.3.10 List errors

In order to fetch information about errors in the X-Road Catalog, a request in XML format has to be sent to the respective SOAP endpoint:

``` $ curl -k -d @GetErrorsRequest.xml --header "Content-Type: text/xml" -X POST http://localhost:8080/ws/GetErrors ```

Contents of the example ```GetErrorsRequest.xml``` file:
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
            <xrcl:since>2020-01-01</xrcl:since>
        </xrcl:GetErrors>
    </soapenv:Body>
</soapenv:Envelope>
```
In the request there are the following fields that need to be filled:
 ```since``` - date after which to list errors, e.g. 2020-01-01

The XML response has a ```<SOAP-ENV:Body>``` element with the following structure:

* GetErrorsResponse
  * errorLogList
    * errorLog
      * message
      * code
      * created
 
A longer example provided in [xroad-catalog-lister](xroad-catalog-lister/README.md)

### 3.2.3.11 List service statistics

In order to fetch information about service statistics in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

``` $ curl "http://<SERVER_ADDRESS>:8080/api/getServiceStatistics/<HISTORY_AMOUNT_IN_DAYS>" -H "Content-Type: application/json" ```

* SERVER_ADDRESS please use the server address, on which the X-Road Catalog Lister is running on, e.g. localhost
* HISTORY_AMOUNT_IN_DAYS length of the statistics to show in days, e.g. 2

Response in JSON:
```json
{"serviceStatisticsList":[{"created":[2021,2,8,13,24,43,734000000],"numberOfSoapServices":0,"numberOfRestServices":0,"numberOfOpenApiServices":0},{"created":[2021,2,9,13,24,43,734000000],"numberOfSoapServices":0,"numberOfRestServices":0,"numberOfOpenApiServices":0}]}
```

The response has the following fields:

* serviceStatisticsList
  * created
  * numberOfSoapServices
  * numberOfRestServices
  * numberOfOpenApiServices 

### 3.2.3.12 List service statistics in CSV format

In order to fetch information about service statistics in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

``` $ curl "http://<SERVER_ADDRESS>:8080/api/getServiceStatistics/<HISTORY_AMOUNT_IN_DAYS>" -H "Content-Type: text/csv" --output service_statistics.csv ```

* SERVER_ADDRESS please use the server address, on which the X-Road Catalog Lister is running on, e.g. localhost
* HISTORY_AMOUNT_IN_DAYS length of the statistics to show in days, e.g. 2

Response is a file ```service_statistics.csv``` with the following content:
```
Date,Number of REST services,Number of SOAP services,Number of OpenApi services
2021-02-08T13:23:46.062,0,0,0,0
2021-02-09T13:23:46.062,0,0,0,0
```

### 3.2.3.13 List services

In order to fetch information about services in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

``` $ curl "http://<SERVER_ADDRESS>:8080/api/getListOfServices/<HISTORY_AMOUNT_IN_DAYS>" -H "Content-Type: application/json ```

* SERVER_ADDRESS please use the server address, on which the X-Road Catalog Lister is running on, e.g. localhost
* HISTORY_AMOUNT_IN_DAYS length of the statistics to show in days, e.g. 2

Response in JSON:

```json
{"memberData":[{"date":{"hour":11,"minute":55,"second":54,"nano":66000000,"dayOfYear":279,"dayOfWeek":"MONDAY","month":"OCTOBER","dayOfMonth":5,"year":2020,"monthValue":10,"chronology":{"calendarType":"iso8601","id":"ISO"}},"memberDataList":[{"created":{"hour":10,"minute":44,"second":30,"nano":896000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"memberClass":"GOV","memberCode":"1234","name":"ACME","subsystemList":[{"created":{"hour":10,"minute":44,"second":30,"nano":896000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"subsystemCode":"MANAGEMENT","serviceList":[{"created":{"hour":10,"minute":44,"second":33,"nano":871000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"serviceCode":"authCertDeletion","serviceVersion":null},{"created":{"hour":10,"minute":44,"second":33,"nano":871000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"serviceCode":"clientReg","serviceVersion":null},{"created":{"hour":10,"minute":44,"second":33,"nano":871000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"serviceCode":"clientDeletion","serviceVersion":null},{"created":{"hour":10,"minute":44,"second":33,"nano":871000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"serviceCode":"ownerChange","serviceVersion":null},{"created":{"hour":10,"minute":44,"second":33,"nano":871000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"serviceCode":"PetStore","serviceVersion":""}]}],"xroadInstance":"DEV"},{"created":{"hour":10,"minute":44,"second":30,"nano":896000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"memberClass":"COM","memberCode":"1710128-9","name":"GOFORE","subsystemList":[],"xroadInstance":"DEV"},{"created":{"hour":13,"minute":30,"second":1,"nano":896000000,"dayOfYear":255,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":11,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"memberClass":"COM","memberCode":"1951458-9","name":"HKScan","subsystemList":[],"xroadInstance":"DEV"}]},{"date":{"hour":11,"minute":55,"second":54,"nano":66000000,"dayOfYear":280,"dayOfWeek":"TUESDAY","month":"OCTOBER","dayOfMonth":6,"year":2020,"monthValue":10,"chronology":{"calendarType":"iso8601","id":"ISO"}},"memberDataList":[{"created":{"hour":10,"minute":44,"second":30,"nano":896000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"memberClass":"GOV","memberCode":"1234","name":"ACME","subsystemList":[{"created":{"hour":10,"minute":44,"second":30,"nano":896000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"subsystemCode":"MANAGEMENT","serviceList":[{"created":{"hour":10,"minute":44,"second":33,"nano":871000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"serviceCode":"authCertDeletion","serviceVersion":null},{"created":{"hour":10,"minute":44,"second":33,"nano":871000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"serviceCode":"clientReg","serviceVersion":null},{"created":{"hour":10,"minute":44,"second":33,"nano":871000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"serviceCode":"clientDeletion","serviceVersion":null},{"created":{"hour":10,"minute":44,"second":33,"nano":871000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"serviceCode":"ownerChange","serviceVersion":null},{"created":{"hour":10,"minute":44,"second":33,"nano":871000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"serviceCode":"PetStore","serviceVersion":""}]}],"xroadInstance":"DEV"},{"created":{"hour":10,"minute":44,"second":30,"nano":896000000,"dayOfYear":248,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":4,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"memberClass":"COM","memberCode":"1710128-9","name":"GOFORE","subsystemList":[],"xroadInstance":"DEV"},{"created":{"hour":13,"minute":30,"second":1,"nano":896000000,"dayOfYear":255,"dayOfWeek":"FRIDAY","month":"SEPTEMBER","dayOfMonth":11,"year":2020,"monthValue":9,"chronology":{"calendarType":"iso8601","id":"ISO"}},"memberClass":"COM","memberCode":"1951458-9","name":"HKScan","subsystemList":[],"xroadInstance":"DEV"}]}],"securityServerData":[{"serverCode":"SS1","address":"10.18.150.48","memberClass":"GOV","memberCode":"1234"}]}
```

The response has the following fields:

* memberData
  * date
  * memberDataList
    * created
    * memberClass
    * memberCode
    * name
    * subsystemList
      * created
      * subsystemCode
      * active (values: true/false)
      * serviceList
        * created
        * serviceCode
        * serviceVersion
        * active (values: true/false)
    * xroadInstance
* securityServerData
  * serverCode
  * address
  * memberClass
  * memberCode
  
### 3.2.3.14 List services in CSV format

In order to fetch information about services in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

``` $ curl "http://<SERVER_ADDRESS>:8080/api/getListOfServices/<HISTORY_AMOUNT_IN_DAYS>" -H "Content-Type: text/csv" --output list_of_services.csv ```

* SERVER_ADDRESS please use the server address, on which the X-Road Catalog Lister is running on, e.g. localhost
* HISTORY_AMOUNT_IN_DAYS length of the list to show in days, e.g. 2

Response is a file ```list_of_services.csv``` with the following content:

```list_of_services.csv file with content:
Date,XRoad instance,Member class,Member code,Member name,Member created,Subsystem code,Subsystem created,Service code,Service version,Service created
2020-10-05T11:57:05.072,,,,,,,,,,
"",DEV,GOV,1234,ACME,2020-09-04T10:44:30.896,MANAGEMENT,2020-09-04T10:44:30.896,clientDeletion,,2020-09-04T10:44:33.871
"",DEV,GOV,1234,ACME,2020-09-04T10:44:30.896,MANAGEMENT,2020-09-04T10:44:30.896,clientReg,,2020-09-04T10:44:33.871
"",DEV,GOV,1234,ACME,2020-09-04T10:44:30.896,MANAGEMENT,2020-09-04T10:44:30.896,ownerChange,,2020-09-04T10:44:33.871
"",DEV,GOV,1234,ACME,2020-09-04T10:44:30.896,MANAGEMENT,2020-09-04T10:44:30.896,PetStore,,2020-09-04T10:44:33.871
"",DEV,GOV,1234,ACME,2020-09-04T10:44:30.896,MANAGEMENT,2020-09-04T10:44:30.896,authCertDeletion,,2020-09-04T10:44:33.871
"",DEV,COM,1710128-9,GOFORE,2020-09-04T10:44:30.896,,,,,
"",DEV,COM,1951458-9,HKScan,2020-09-11T13:30:01.896,,,,,
2020-10-06T11:57:05.072,,,,,,,,,,
"",DEV,GOV,1234,ACME,2020-09-04T10:44:30.896,MANAGEMENT,2020-09-04T10:44:30.896,clientDeletion,,2020-09-04T10:44:33.871
"",DEV,GOV,1234,ACME,2020-09-04T10:44:30.896,MANAGEMENT,2020-09-04T10:44:30.896,clientReg,,2020-09-04T10:44:33.871
"",DEV,GOV,1234,ACME,2020-09-04T10:44:30.896,MANAGEMENT,2020-09-04T10:44:30.896,ownerChange,,2020-09-04T10:44:33.871
"",DEV,GOV,1234,ACME,2020-09-04T10:44:30.896,MANAGEMENT,2020-09-04T10:44:30.896,PetStore,,2020-09-04T10:44:33.871
"",DEV,GOV,1234,ACME,2020-09-04T10:44:30.896,MANAGEMENT,2020-09-04T10:44:30.896,authCertDeletion,,2020-09-04T10:44:33.871
"",DEV,COM,1710128-9,GOFORE,2020-09-04T10:44:30.896,,,,,
"",DEV,COM,1951458-9,HKScan,2020-09-11T13:30:01.896,,,,,
"",Security server (SS) info:,,,,,,,,,
member class,member code,server code,address,,,,,,,
GOV,1234,SS1,10.18.150.48,,,,,,,
```

### 3.2.3.15 Check heartbeat

In order to fetch X-Road Catalog heartbeat information, an HTTP request has to be sent to a respective REST endpoint:

``` $ curl "http://<SERVER_ADDRESS>:8080/api/heartbeat" -H "Content-Type: application/json" ```

* SERVER_ADDRESS please use the server address, on which the X-Road Catalog Lister is running on, e.g. localhost

Response in JSON:
```
{"appWorking":true,
 "dbWorking":true,
 "appName":"X-Road Catalog Lister",
 "appVersion":"1.2.1",
 "systemTime":[2021,9,20,10,12,15,132000000],
 "lastCollectionData":
 {"organizationsLastFetched":[2021,9,20,10,10,55,153000000],
  "companiesLastFetched":null,
  "membersLastFetched":[2021,9,20,10,8,51,380000000],
  "subsystemsLastFetched":[2021,9,20,10,8,51,380000000],
  "servicesLastFetched":[2021,9,1,15,32,51,123000000],
  "wsdlsLastFetched":[2021,9,1,15,32,53,87000000],
  "openapisLastFetched":[2020,11,22,22,12,32,202000000]
 }
}
```

The response has the following fields:

* appWorking
* dbWorking
* appName
* appVersion
* systemTime
* lastCollectionData
  * organizationsLastFetched
  * companiesLastFetched
  * membersLastFetched
  * subsystemsLastFetched
  * servicesLastFetched
  * wsdlsLastFetched
  * openapisLastFetched

### 3.2.3.16 List distinct service statistics

In order to fetch information about distinct service statistics in the X-Road Catalog, an HTTP request has to be sent to a respective REST endpoint:

``` $ curl "http://<SERVER_ADDRESS>:8080/api/getDistinctServiceStatistics/<HISTORY_AMOUNT_IN_DAYS>" -H "Content-Type: application/json" ```

* SERVER_ADDRESS please use the server address, on which the X-Road Catalog Lister is running on, e.g. localhost
* HISTORY_AMOUNT_IN_DAYS length of the statistics to show in days, e.g. 2

Response in JSON:
```json
{"distinctServiceStatisticsList":[{"created":[2021,8,24,14,58,11,165000000],"numberOfDistinctServices":5},{"created":[2021,8,25,14,58,11,165000000],"numberOfDistinctServices":6}]}
```

The response has the following fields:

* serviceStatisticsList
  * created
  * numberOfDistinctServices

### 3.2.3.17 List errors

Requests

List errors for a given subsystem:
curl "http://<SERVER_ADDRESS>:8080/api/listErrors/<INSTANCE>/<MEMBER_CLASS>/<MEMBER_CODE>/<SUBSYSTEM_CODE>/<HISTORY_AMOUNT_IN_DAYS>" -H "Content-Type: application/json"

List errors for a given member:
curl "http://<SERVER_ADDRESS>:8080/api/listErrors/<INSTANCE>/<MEMBER_CLASS>/<MEMBER_CODE>/<HISTORY_AMOUNT_IN_DAYS>" -H "Content-Type: application/json"

List errors for a given member class:
curl "http://<SERVER_ADDRESS>:8080/api/listErrors/<INSTANCE>/<MEMBER_CLASS>/<HISTORY_AMOUNT_IN_DAYS>" -H "Content-Type: application/json"

List errors for a given instance:
curl "http://<SERVER_ADDRESS>:8080/api/listErrors/<INSTANCE>/<HISTORY_AMOUNT_IN_DAYS>" -H "Content-Type: application/json"

List errors for all the instances and members:
curl "http://<SERVER_ADDRESS>:8080/api/listErrors/<HISTORY_AMOUNT_IN_DAYS>" -H "Content-Type: application/json"

List errors for a given subsystem with pagination:
curl "http://<SERVER_ADDRESS>:8080/api/listErrors/<INSTANCE>/<MEMBER_CLASS>/<MEMBER_CODE>/<SUBSYSTEM_CODE>/<HISTORY_AMOUNT_IN_DAYS>?page=<PAGE_NUMBER>&limit=<NO_OF_ERRORS_PER_PAGE>" -H "Content-Type: application/json"

List errors for a given member with pagination:
curl "http://<SERVER_ADDRESS>:8080/api/listErrors/<INSTANCE>/<MEMBER_CLASS>/<MEMBER_CODE>/<HISTORY_AMOUNT_IN_DAYS>?page=<PAGE_NUMBER>&limit=<NO_OF_ERRORS_PER_PAGE>" -H "Content-Type: application/json"

List errors for a given member class with pagination:
curl "http://<SERVER_ADDRESS>:8080/api/listErrors/<INSTANCE>/<MEMBER_CLASS>/<HISTORY_AMOUNT_IN_DAYS>?page=<PAGE_NUMBER>&limit=<NO_OF_ERRORS_PER_PAGE>" -H "Content-Type: application/json"

List errors for a given instance with pagination:
curl "http://<SERVER_ADDRESS>:8080/api/listErrors/<INSTANCE>/<HISTORY_AMOUNT_IN_DAYS>?page=<PAGE_NUMBER>&limit=<NO_OF_ERRORS_PER_PAGE>" -H "Content-Type: application/json"

List errors for all the instances and members with pagination:
curl "http://<SERVER_ADDRESS>:8080/api/listErrors/<HISTORY_AMOUNT_IN_DAYS>?page=<PAGE_NUMBER>&limit=<NO_OF_ERRORS_PER_PAGE>" -H "Content-Type: application/json"

Example request
curl "http://localhost:8080/api/listErrors/DEV/GOV/1234/TEST/29?page=0&limit=10" -H "Content-Type: application/json"

* SERVER_ADDRESS please use the server address, on which the X-Road Catalog Lister is running on, e.g. localhost
* INSTANCE name of X-Road instance, e.g. DEV
* MEMBER_CLASS member class, e.g. GOV
* MEMBER_CODE member code, e.g. 1234
* SUBSYSTEM_CODE subsystem code, e.g. TEST
* HISTORY_AMOUNT_IN_DAYS length of the statistics to show in days, e.g. 2
* PAGE_NUMBER the number of page of the fetched results
* NO_OF_ERRORS_PER_PAGE number of errors per fetched page

Response in JSON:
```json
{
  "pageNumber": 0,
  "pageSize": 10,
  "numberOfPages": 3,
  "errorLogList": [
    {
      "id": 158,
      "message": "Fetch of SOAP services failed: Client (SUBSYSTEM:DEV/GOV/1234/MANAGEMENT) specifies HTTPS NO AUTH but client made  plaintext connection",
      "code": "500",
      "created": [
        2021,
        9,
        28,
        10,
        35,
        33,
        255000000
      ],
      "memberClass": "GOV",
      "memberCode": "1234",
      "subsystemCode": "TEST",
      "groupCode": "",
      "serviceCode": "",
      "serviceVersion": null,
      "securityCategoryCode": "",
      "serverCode": "",
      "xroadInstance": "DEV"
    },
    {}
  ]
}
```

The response has the following fields:

* pageNumber
* pageSize
* numberOfPages
* errorLogList
  * id
  * message
  * code
  * created
  * memberClass
  * memberCode
  * subsystemCode
  * groupCode
  * serviceCode
  * serviceVersion
  * securityCategoryCode
  * serverCode
  * xroadInstance 

### 3.2.3.18 List security servers

Request

curl "http://localhost:8080/api/listSecurityServers" -H "Content-Type: application/json"

Response
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

* securityServerDataList
    * owner
        * memberClass
        * memberCode
        * name
        * subsystemCode
    * serverCode
    * address
    * clients
        * memberClass
        * memberCode
        * name
        * subsystemCode

**owner** indicates the owner of the security server

**clients** provides a list of clients using the security server, where a client can be considered a member when their subsystemCode is null


### 3.2.3.19 List descriptors

Request

curl "http://localhost:8080/api/listDescriptors" -H "Content-Type: application/json"

Response

```
{
  "descriptorInfoList": [
    {
      "x_road_instance": "DEV",
      "subsystem_name": {
        "et": "Subsystem Name ET",
        "en": "Subsystem Name EN"
      },
      "email": {
        "name": "Firstname Lastname",
        "email": "yourname@yourdomain"
      },
      "member_class": "GOV",
      "member_code": "1234",
      "member_name": "ACME",
      "subsystem_code": "MANAGEMENT"
    },
    {
      "x_road_instance": "DEV",
      "subsystem_name": {
        "et": "Subsystem Name ET",
        "en": "Subsystem Name EN"
      },
      "email": {
        "name": "Firstname Lastname",
        "email": "yourname@yourdomain"
      },
      "member_class": "GOV",
      "member_code": "1234",
      "member_name": "ACME",
      "subsystem_code": "TEST"
    },
    {
      "x_road_instance": "DEV",
      "subsystem_name": {
        "et": "Subsystem Name ET",
        "en": "Subsystem Name EN"
      },
      "email": {
        "name": "Firstname Lastname",
        "email": "yourname@yourdomain"
      },
      "member_class": "GOV",
      "member_code": "1234",
      "member_name": "ACME",
      "subsystem_code": "MASTER"
    },
    {
      "x_road_instance": "DEV",
      "subsystem_name": {
        "et": "Subsystem Name ET",
        "en": "Subsystem Name EN"
      },
      "email": {
        "name": "Firstname Lastname",
        "email": "yourname@yourdomain"
      },
      "member_class": "GOV",
      "member_code": "1234",
      "member_name": "ACME",
      "subsystem_code": "TESTCLIENT"
    },
    {
      "x_road_instance": "DEV",
      "subsystem_name": {
        "et": "Subsystem Name ET",
        "en": "Subsystem Name EN"
      },
      "email": {
        "name": "Firstname Lastname",
        "email": "yourname@yourdomain"
      },
      "member_class": "GOV",
      "member_code": "1234",
      "member_name": "ACME",
      "subsystem_code": "THESUBSYSTEM"
    }
  ]
}
```


The response has the following fields:

* descriptorInfoList
    * x_road_instance
    * member_class
    * member_code
    * member_name
    * subsystem_code
    * subsystem_name
        * et
        * en
    * email
        * name
        * email

**subsystem_name** indicates a user-friendly name of the subsystem, in addition to not so friendly subsystem_code, 
in the current implementation contains default values, because X-Road currently does not provide such information, but
the fields are still required for the X-Road Metrics to operate correctly

**email** indicates e-mail and name of a contact person,  
in the current implementation contains default values, because X-Road currently does not provide such information, but
the fields are still required for the X-Road Metrics to operate correctly
        
### 3.3 X-Road Catalog Persistence

The purpose of this piece of software is to persist and read persisted data. Used by the Collector and Lister

### 3.3.1 Create database

The database and tables required for X-Road Catalog can be created with the following:

```sh
sudo -u postgres psql --file=src/main/sql/init_database.sql
sudo -u postgres psql --file=src/main/sql/create_tables.sql
```

### 3.3.2 Build

X-Road persistence can be built with:

```sh gradle clean build ```

### 3.3.3 Profiles

There are two spring boot profiles. Default profile connects to in-memory H2 database. 
This has schema autogenerated based on JPA entities, and data populated from data.sql.
"Production" profile connects to a real PostgreSQL instance (currently defined in src/main/resources/application-production.properties)
and does not auto-create anything.

### 3.3.4 Run

X-Road persistence can be run with Gradle:

``` sh ./gradlew bootRun ```

or run from a JAR file:

``` sh ./gradlew bootRun -Dspring.profiles.active=production ```
