# Collector for X-Road Clients in System

## Build


    $ gradle clean package


## Run

    $ gradle bootRun

Or

    $ java -jar target/xroad-catalog-collector-1.0-SNAPSHOT.jar

## Build RPM Packages on Non-RedHat Platform

First make sure that xroad-catalog-persistence is located next to xroad-catalog-collector. The RPM build
 uses sql files from xroad-catalog-persistence/src/main/sql.
 
    $ gradle clean build
    $ docker build -t collector-rpm packages/xroad-catalog-collector/docker
    $ docker run -v $PWD/..:/workspace  -v /etc/passwd:/etc/passwd:ro -v /etc/group:/etc/group:ro collector-rpm
