# Introduction to X-Road Catalog Lister

The purpose of this piece of software is to provide a webservice which lists all the XRoad members and the services they provide together with services descriptions

A class diagram illustrating X-Road Catalog Lister implementation with ```default``` and ```FI``` profiles

![Catalog Service class diagram](class_diagram.png)

See also the [User Guide](../xroad_catalog_user_guide.md#license)

## Build

X-Road Catalog Lister can be built by running:

``` $ ../gradlew clean build ```

## Profiles

There are four spring boot profiles.

* default (a profile used for default operation of X-Road Catalog, without any country-specific features)
* FI (an extra profile used in addition to the default profile, which has country specific features)
* production (a profile used in the production)
* sshtest (a profile used to test SSH tunneling with X-Road Catalog)

## Run

X-Road Catalog Lister can be run using Gradle:

``` $ ../gradlew bootRun ```

or running it from a JAR file:

``` $ java -jar build/libs/xroad-catalog-lister.jar --spring.config.name=lister,catalogdb ```

## Build RPM Packages on Non-RedHat Platform

    $ ../gradlew clean build
    $ docker build -t lister-rpm packages/xroad-catalog-lister/docker
    $ docker run -v $PWD/..:/workspace lister-rpm