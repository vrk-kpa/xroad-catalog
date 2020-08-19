# Changelog

All notable changes to this project will be documented in this file.

## 0.13.5 - 2020-08-19
- update version
- update changelogs and documentation

## 0.13.3 - 2020-08-11
- update version
- Fix GetService returning only active services
- Fix fetch of rest services and serviceVersion NULL issue

## 0.13.0 - 2020-07-13
- update version
- Solve memory issues by lazy fetching

## 0.12.7 - 2020-07-06
- update version
- Refacto of GetCompanies

## 0.11.9 - 2020-06-18
- update version
- Fix GetService returning duplicates
- Fix IsSoapService and IsRestService returning duplicates

## 0.11.8 - 2020-06-11
- update version

## 0.11.7 - 2020-06-09
- update version
- Add serviceVersion as input parameter to IsSoapService and IsRestService

## 0.11.6 - 2020-06-03
- update version
- add multiple languages support to GetOrganizations and GetCompanies

## 0.11.5 - 2020-05-20
- update version

## 0.11.4 - 2020-05-13
- update version
- update changelogs

## 0.11.4 - 2020-05-13
- update version
- update changelogs

## 0.11.3 - 2020-05-07
- update documentation
- removed time from date checks in tests to fix issues related to timezones

## 0.11.3 - 2020-05-06
- unit tests for new repositories and catalog services

## 0.11.3 - 2020-05-05
- test data and a few tests
- create unit test for new actor and small refacto

## 0.11.3 - 2020-05-05
- save company details to db

## 0.11.2 - 2020-05-01
- save company and related details to db
- company related entities method to catalogService
- new entities and repositories
- create new db tables

## 0.11.1 - 2020-04-24
- update documentation related to new services

## 0.11.1 - 2020-04-23
- unit tests for new endpoints

## 0.11.1 - 2020-04-20
- refacto db tables creation so that tables would be created every time if they do not exist

## 0.11.1 - 2020-04-19
- new endpoints HasOrganizationChanged

## 0.11.1 - 2020-04-17
- finalize organization jaxb conversions 
- new endpoint and related jaxb conversions
- finalize collection of organizational data to db

## 0.11.1 - 2020-04-15
- refacto CatalogService, FetchOrganizationUtil and sql script

## 0.11.1 - 2020-04-14
- save organizationAddress related data to db
- logic for fetching and saving organizations

## 0.11.1 - 2020-04-10
- new entities and repositories for fetching organizations

## 0.11.1 - 2020-04-03
- add unit tests for new interfaces to persistence and lister

## 0.11.0 - 2020-04-02
- add new endpoint IsProvider and change names of IsRestProvider and IsSoapProvider

## 0.11.0 - 2020-03-31
- add subsystemcode to getService query to avoid duplicates
