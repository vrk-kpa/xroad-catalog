# Introduction to X-Road Catalog Collector

The purpose of this module is to collect members, subsystems and services from the X-Road ecosystem and store them to 
a database. 

The module is implemented using concurrent Akka actors: 

* `FetchWsdlActor` - fetches WSDL descriptions of SOAP services from the X-Road instance and stores them to the db.
* `FetchOpenApiActor` - fetches OpenAPI descriptions of Rest services from the X-Road instance and stores them to the db.
* `ListClientsActor` - fetches a list of clients from the X-Road instance and stores them to the db.
* `ListMethodsActor` - fetches a list of services from the X-Road instance and stores them to the db.
* `FetchOrganizationsActor` - fetches a list of public organizations from an external API and stores them to the db.
* `FetchCompaniesActor` - fetches a list of private companies from an external API and stores them to the db.

See also the [Installation Guide](../doc/xroad_catalog_installation_guide.md) and
[User Guide](../doc/xroad_catalog_user_guide.md).

## Build

X-Road Catalog Collector can be built by running:

```bash
$ ../gradlew clean build
```

## Build RPM Packages on Non-RedHat Platform

First make sure that `xroad-catalog-persistence` is located next to `xroad-catalog-collector`. The RPM build uses sql
files from `xroad-catalog-persistence/src/main/sql`. If the `default` profile is used, the `CATALOG_PROFILE` argument
can be omitted. More information about profiles is available in the [build instructions](../BUILD.md#profiles).

```bash
../gradlew clean build
docker build -t collector-rpm packages/xroad-catalog-collector/docker --build-arg CATALOG_PROFILE=<PROFILE>
docker run -v $PWD/..:/workspace collector-rpm
```

## Run

X-Road Catalog Collector can be run by using Gradle:

```bash
../gradlew bootRun
```

or by running it from a JAR file:

```bash
java -jar target/xroad-catalog-collector-1.0-SNAPSHOT.jar
```

## Run against a remote Security Server over an SSH tunnel

First create an ssh tunnel to a local port:

```bash
ssh -nNT -L <LOCAL_PORT>:<DESTINATION>:<DESTINATION_PORT> [USER@]SSH_SERVER
```

For example, there's a Security Server running on a machine `my-security-server.com` on an internal private network on
port `80`. The Security Server is accessible from the machine `my-ssh-server.com`. To connect to the Security Server from
the local machine using the local port `9000`, forward the connection using the following command:

```bash
ssh -nNT -L 9000:my-security-server.com:80 my-user@my-ssh-server.com
```

Then run the collector with profile `sshtest`:

```bash
java -Dspring.profiles.active=sshtest -jar build/libs/xroad-catalog-collector.jar --spring.config.name=collector,catalogdb
```
