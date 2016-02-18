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

## Test

| Command                                                                                |           Result            |
|----------------------------------------------------------------------------------------|-----------------------------|
| curl --header "content-type: text/xml" -d @servicerequest.xml http://localhost:8080/ws |  All services in the system |
| curl http://localhost:8080/ws/services.wsdl                                            |  Get WSDL                   |




