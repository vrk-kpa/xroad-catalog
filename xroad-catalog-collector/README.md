# Collector for X-Road Clients in System

## Build


    $ gradle clean build


## Run

    $ gradle bootRun

Or

    $ java -jar target/xroad-catalog-collector-1.0-SNAPSHOT.jar

## Run against remote security server

First create an ssh tunnel to local port 9000, for example

    $ ssh -nNT -L 9000:gdev-rh1.i.palveluvayla.com:80 dev-is.palveluvayla.com

Then run the collector with profile sshtest

    $ java -Dspring.profiles.active=sshtest -jar build/libs/xroad-catalog-collector.jar --spring.config.name=collector,catalogdb




## Build RPM Packages on Non-RedHat Platform

First make sure that xroad-catalog-persistence is located next to xroad-catalog-collector. The RPM build
 uses sql files from xroad-catalog-persistence/src/main/sql.
 
    $ ./gradlew clean build
    $ cd xroad-catalog-collector
    $ docker build -t collector-rpm packages/xroad-catalog-collector/docker
    $ docker run -v $PWD/..:/workspace  -v /etc/passwd:/etc/passwd:ro -v /etc/group:/etc/group:ro collector-rpm
