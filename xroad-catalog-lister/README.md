# xroad-catalog-lister

WebService to produce list of xroad clients

## Build
```sh
gradle clean build
```

## Run
```sh
gradle bootRun
```

Or

    $ java -jar build/libs/xroad-catalog-lister.jar --spring.config.name=lister,catalogdb


## Test

| Command                                                                                |           Result            |
|----------------------------------------------------------------------------------------|-----------------------------|
| curl --header "content-type: text/xml" -d @src/main/doc/servicerequest.xml http://localhost:8080/ws |  All services in the system |
| curl http://localhost:8080/ws/services.wsdl                                            |  Get WSDL                   |


## Build RPM Packages on Non-RedHat Platform

    $ ./gradlew clean build
    $ cd 
    $ docker build -t lister-rpm packages/xroad-catalog-lister/docker
    $ docker run -v $PWD/..:/workspace  -v /etc/passwd:/etc/passwd:ro -v /etc/group:/etc/group:ro lister-rpm


