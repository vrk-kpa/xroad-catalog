# Building X-Road Catalog

## License <!-- omit in toc -->

This document is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License. 
To view a copy of this license, visit <http://creativecommons.org/licenses/by-sa/3.0/>.

## About

Developing and building the X-Road Catalog software requires an Ubuntu or a RHEL host. If you are using some other
operating system (e.g. Windows or MacOS), the easiest option is to first install Ubuntu 22.04 or RHEL7 into a virtual
machine.

**Tools**

Required for building:
* OpenJDK / JDK version 11
* Gradle
* Docker

Recommended for development environment:
* Docker
* [LXD](https://linuxcontainers.org/lxd/)
    * For setting up a local X-Road instance.
* Ansible
    * For automating the [X-Road ecosystem installation](https://github.com/nordic-institute/X-Road/tree/develop/ansible).

The development environment should have at least 8GB of memory and 20GB of free disk space (applies to a virtual machine
as well), especially if you set up a local X-Road ecosystem.

**Prerequisites**

* Checkout the `x-road-catalog` repository.
* The directory structure should look like this:

    ```
    - <BASE_DIR>
     |-- xroad-catalog-collector
     |-- xroad-catalog-lister
     |-- xroad-catalog-persistence
    ```
* The build scripts assumes the above directory structure.

## Build X-Road Catalog Collector

See [xroad-catalog-collector/README.md](xroad-catalog-collector/README.md#build) for details.

## Build X-Road Catalog Lister

See [xroad-catalog-lister/README.md](xroad-catalog-lister/README.md#build) for details.

## Build X-Road Catalog Persistence

See [xroad-catalog-persistence/README.md](xroad-catalog-persistence/README.md#build) for details.

## Profiles

Profiles can be used to configure different features in X-Road Catalog. By default, X-Road Catalog includes four different
profiles:

* `default` - a profile used for default operation of X-Road Catalog, without any country-specific features.
  * The default profile can be set with `spring.profiles.active=`.
* `fi` - an extra profile used in addition to the default profile, which has country-specific (Finland) features, e.g.,
  fetching additional data from a national business registry. Other country-specific profiles can be added if needed.
  * The profile can be set with `spring.profiles.active=fi`.
* `production` - a profile used in the production deployment.
  * The profile can be set with `spring.profiles.active=production`.
* `sshtest` - a profile used to test SSH tunneling with X-Road Catalog.
  * The profile can be set with `spring.profiles.active=sshtest`.

Multiple profiles can be activated at the same time by separating them with a comma, e.g., `spring.profiles.active=production,fi`.

X-Road Catalog supports adding new profiles. For example, new country-specific features should be added by creating a
new country-specific profile.

### How Profiles Are Used During the Build?

* First, a Docker image for building the X-Road Catalog rpm packages is built.
  * For X-Road Catalog Collector:
    ```bash
    docker build -t collector-rpm packages/xroad-catalog-collector/docker --build-arg CATALOG_PROFILE=fi
    ```
  * For X-Road Catalog Lister:
    ```bash
    docker build -t lister-rpm packages/xroad-catalog-lister/docker --build-arg CATALOG_PROFILE=fi
    ```
* Profile value is provided to the respective `Dockerfile` with an argument `--build-arg CATALOG_PROFILE=fi`. Then within
  the `Dockerfile` an environment variable `CATALOG_PROFILE` is initalized with that profile value.
* A shell script `build_rpm.sh` is run within a Docker container, which takes a `p` (profile) parameter as input, which is read from the environment variable `CATALOG_PROFILE`.
* The shell script `build_rpm.sh` passes on the profile parameter (the script assumes the profile is `default` when no value is given with the `profile` parameter)
  to `xroad-catalog-collector.spec` or `xroad-catalog-lister.spec` which configures a prepares the creation Systemd service for X-Road Catalog Collector or X-Road Catalog Lister.
* Within that `.spec` file the catalog profile value will be written to a properties file:
  ```bash
  echo "CATALOG_PROFILE=%{profile}" >> catalog-profile.properties
  ```
* Then that file will be copied to `/etc/xroad/xroad-catalog` among other properties files.
* In addition, specific db scripts will be run within that `.spec` file according to the value of the `profile`:
  ```bash
  sudo -u postgres psql --file=/usr/share/xroad/sql/create_tables_%{profile}.sql
  ```
* Finally, the profile value will be read from that properties file:
  ```bash
  source /etc/xroad/xroad-catalog/catalog-profile.properties
  ```
* Then a Systemd service will be created with the following content:
  * For X-Road Catalog Collector:
    ```bash
    exec ${JAVA_HOME}/bin/java -Xms128m -Xmx2g -Dspring.profiles.active=base,production -Dspring.profiles.include=$CATALOG_PROFILE -jar /usr/lib/xroad-catalog/xroad-catalog-collector.jar --spring.config.location=/etc/xroad/xroad-catalog/ --spring.config.name=collector,catalogdb
    ```
  * For X-Road Catalog Lister:
    ```bash
    exec ${JAVA_HOME}/bin/java -Xms128m -Xmx2g -Dserver.port=8070 -Dspring.profiles.active=production -Dspring.profiles.include=$CATALOG_PROFILE -jar /usr/lib/xroad-catalog/xroad-catalog-lister.jar --spring.config.location=/etc/xroad/xroad-catalog/ --spring.config.name=lister,catalogdb
    ```
