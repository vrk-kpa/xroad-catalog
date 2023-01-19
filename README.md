## About the repository 

This repository contains information about the X-Road Catalog, source codes, its development, installation and documentation.

## Introduction to X-Road Catalog

The purpose of this piece of software is to collect information, more specifically members, subsystems and services, from an X-RoadC instance and offer an interface where the information can be read.

The software consists of three parts:
- [xroad-catalog-collector](xroad-catalog-collector/README.md)
  * Collects information from the X-Road instance (possibly also from external apis) and stores it to the postgresql database. 
  * Implemented using concurrent Akka actors. 
- [xroad-catalog-lister](xroad-catalog-lister/README.md)
  * SOAP and REST interfaces that offer information collected by the Collector. 
  * Can be used as an X-Road service (X-Road headers are in place)
- [xroad-catalog-persistence](xroad-catalog-persistence/README.md)
  * Library used to persist and read persisted data. Used by both of the above.
  
![X-Road Catalog overview](architecture.png)


## Version management
For versioning, [GitHub Flow](https://guides.github.com/introduction/flow/) is used

* Anything in the master branch is deployable
* To work on something new, create a branch off from master and given a descriptive name(ie: new-oauth2-scopes)
* Commit to that branch locally and regularly push your work to the same named branch on the server
* When you need feedback or help, or you think the branch is ready for merging, open a pull request
* After someone else has reviewed and signed off on the feature, you can merge it into master
* Once it is merged and pushed to master, you can and should deploy immediately

## Tools
Running the X-Road Catalog software requires Linux (Ubuntu or RHEL). If you are using some other operating system (e.g. Windows or macOS), the easiest option is to first install Ubuntu 20.04 or RHEL 7.0 into a virtual machine.


*Required for building*
* OpenJDK / JDK version 11
* Gradle

*Recommended for development environment*
* Docker (for deb/rpm packaging)
* LXD (https://linuxcontainers.org/lxd/)
  * for setting up a local X-Road instance
* Ansible
  * for automating the X-Road instance installation

The development environment should have at least 8GB of memory and 20GB of free disk space (applies to a virtual machine as well), especially if you set up a local X-Road instance.

