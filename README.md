# xroad-catalog
The purpose of this piece of software is to collect information, more specifically members, subsystems and services, from a X-RoadCatalog instance and offer an interface where the information can be read.

The software consists of theree parts:
- [xroad-catalog-collector](xroad-catalog-collector/README.md)
  * Collects members, subsystems and servcies from the x-Road instance and stores them to the postgresql database. 
  * Implemented using concurrent Akka actors. 
- [xroad-catalog-lister](xroad-catalog-lister/README.md)
  * SOAP interface that offers information collected by collector. 
  * Can be used as an X-Road service (X-Road headers are in place)
- [xroad-catalog-persistence](xroad-catalog-persistence/README.md)
  * Library used to persist and read persisted data. Used by both of the above.
  
