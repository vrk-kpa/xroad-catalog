#!/bin/sh

cd workspace/xroad-catalog-collector/packages/xroad-catalog-collector/redhat
rpmbuild --define "_topdir `pwd`" -ba SPECS/xroad-catalog-collector.spec
