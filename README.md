# xroad-catalog
Catalog information of members, subsystems and services on X-Road

## xroad-catalog-lister

WebService to produce c

## Build
```sh
cd xroad-catalog-lister
gradle clean build
```

## Run
```sh
gradle bootRun
```

## Test

| Command  |     Result  |
|----------|-------------|
| curl --header "content-type: text/xml" -d @servicerequest.xml http://localhost:8080/ws |  All services in the system |



